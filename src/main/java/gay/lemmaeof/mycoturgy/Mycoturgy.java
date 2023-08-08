package gay.lemmaeof.mycoturgy;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.emi.trinkets.api.TrinketsApi;
import gay.lemmaeof.mycoturgy.component.HaustorComponent;
import gay.lemmaeof.mycoturgy.data.meditate.MeditationManager;
import gay.lemmaeof.mycoturgy.init.*;
import gay.lemmaeof.mycoturgy.spell.BouncePadSpell;
import gay.lemmaeof.mycoturgy.spell.GrowMushroomSpell;
import gay.lemmaeof.mycoturgy.spell.PaddleRhizomeSpell;
import gay.lemmaeof.mycoturgy.spell.Spell;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.BlockState;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.item.content.registry.api.ItemContentRegistries;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.util.Map;

public class Mycoturgy implements ModInitializer {
	public static final String MODID = "mycoturgy";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	//TODO: config
	public static final int HAUSTOR_TICK_SPEED = 10;

	@Override
	public void onInitialize(ModContainer container) {
		MycoturgyBlocks.init();
		MycoturgyItems.init();
		MycoturgyRecipes.init();
		MycoturgyEffects.init();
		MycoturgyNetworking.init();
		MycoturgyCriteria.init();

		ResourceLoader.get(ResourceType.SERVER_DATA).registerReloader(MeditationManager.INSTANCE);

		//TODO: infested spawner drops
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, builder, table) -> {
			if (id.equals(new Identifier("blocks/grass"))) {
				builder.pool(new LootPool.Builder()
						.conditionally(RandomChanceLootCondition.builder(0.1f)
								.build()
						)
						.with(ItemEntry.builder(MycoturgyItems.GLITTERING_SPORES)
						.build()
				).build());
			}
		});

		ItemContentRegistries.COMPOST_CHANCES.put(MycoturgyItems.GLITTERING_SPORES, 0.3f);
		ItemContentRegistries.COMPOST_CHANCES.put(MycoturgyItems.SPOREBRUSH, 0.65f);
		ItemContentRegistries.COMPOST_CHANCES.put(MycoturgyItems.SPORE_BUNDLE, 0.85f);

		Spell.SPELLS.add(new BouncePadSpell());
		Spell.SPELLS.add(new GrowMushroomSpell());
		Spell.SPELLS.add(new PaddleRhizomeSpell());

		UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
			if (
					!world.isClient()
					&& world.getBlockState(hit.getBlockPos()).isIn(MycoturgyBlocks.SPELL_CASTABLE)
					&& player.getStackInHand(hand).isEmpty()
					&& TrinketsApi.getTrinketComponent(player).get().isEquipped(stack -> stack.isIn(MycoturgyItems.CASTING_BANDS))
			) {
				for (Spell spell : Spell.SPELLS) {
					ServerWorld sworld = (ServerWorld) world;
					BlockPos pos = hit.getBlockPos();
					BlockState state = world.getBlockState(pos);
					HaustorComponent haustor = MycoturgyComponents.HAUSTOR_COMPONENT.get(world.getChunk(pos));
					if (spell.canCast(sworld, pos, state, player, haustor)) {
						spell.cast(sworld, pos, state, player, haustor);
						//TODO: new sound, particles
						world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1f, 1f);
						if (world.getBlockState(pos).equals(state)) world.breakBlock(pos, false);
						MycoturgyCriteria.CAST_SPELL.trigger((ServerPlayerEntity) player);
						return ActionResult.SUCCESS;
					}
				}
			}
			return ActionResult.PASS;
		});

		//commands reload before RLLs so you gotta /reload for this :waaaaa:
		//luckily this is temporary for testing!
		CommandRegistrationCallback.EVENT.register((dispatcher, integrated, dedicated) -> {
			LiteralCommandNode<ServerCommandSource> root = CommandManager.literal("meditate").build();
			Map<Identifier, MeditationManager.MeditationStep> meditations = MeditationManager.INSTANCE.getRoots();
			for (Identifier id : meditations.keySet()) {
				MeditationManager.MeditationStep step = meditations.get(id);
				root.addChild(getNodeFor(id.getPath(), step));
			}

			dispatcher.getRoot().addChild(root);
		});
	}

	private LiteralCommandNode<ServerCommandSource> getNodeFor(String name, MeditationManager.MeditationStep step) {
		LiteralCommandNode<ServerCommandSource> node = CommandManager.literal(name).executes(context -> {
			if (step.description() != null) {
				context.getSource().sendFeedback(step::description, false);
			}
			if (step.destination() != null) {
				context.getSource().sendFeedback(() -> Text.literal("Reached destination " + step.destination()), false);
				MycoturgyCriteria.MEDITATE.trigger(context.getSource().getPlayer(), step.destination());
			}
			if (step.paths().size() > 0) {
				context.getSource().sendFeedback(() -> Text.literal("Options:"), false);
			}
			for (String path : step.paths().keySet()) {
				MutableText text = Text.literal("  - " + path);
				ClickEvent event = new ClickEvent(ClickEvent.Action.RUN_COMMAND, context.getInput() + " " + path);
				text.styled(style -> style.withClickEvent(event).withColor(Formatting.GREEN));
				context.getSource().sendFeedback(() -> text, false);
			}
			return 1;
		}).build();
		for (String path : step.paths().keySet()) {
			MeditationManager.MeditationStep pathStep = step.paths().get(path);
			node.addChild(getNodeFor(path, pathStep));
		}
		return node;
	}
}
