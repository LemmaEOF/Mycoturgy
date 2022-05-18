package gay.lemmaeof.mycoturgy;

import dev.emi.trinkets.api.TrinketsApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import gay.lemmaeof.mycoturgy.component.HaustorComponent;
import gay.lemmaeof.mycoturgy.init.MycoturgyBlocks;
import gay.lemmaeof.mycoturgy.init.MycoturgyComponents;
import gay.lemmaeof.mycoturgy.init.MycoturgyItems;
import gay.lemmaeof.mycoturgy.init.MycoturgyNetworking;
import gay.lemmaeof.mycoturgy.init.MycoturgyRecipes;
import gay.lemmaeof.mycoturgy.init.MycoturgyEffects;
import gay.lemmaeof.mycoturgy.spell.BouncePadSpell;
import gay.lemmaeof.mycoturgy.spell.GrowMushroomSpell;
import gay.lemmaeof.mycoturgy.spell.PaddleRhizomeSpell;
import gay.lemmaeof.mycoturgy.spell.Spell;

import net.minecraft.advancement.Advancement;
import net.minecraft.block.BlockState;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;

public class Mycoturgy implements ModInitializer {
	public static final String MODID = "mycoturgy";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	//TODO: config
	public static final int HAUSTOR_TICK_SPEED = 10;

	public static final Identifier CAST_SPELL = new Identifier(Mycoturgy.MODID, "research/cast_spell");

	@Override
	public void onInitialize(ModContainer container) {
		MycoturgyBlocks.init();
		MycoturgyItems.init();
		MycoturgyRecipes.init();
		MycoturgyEffects.init();
		MycoturgyNetworking.init();

		//TODO: infested spawner drops
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, builder, table) -> {
			if (id.equals(new Identifier("blocks/grass"))) {
				builder.withPool(FabricLootPoolBuilder.builder()
						.withCondition(RandomChanceLootCondition.builder(0.1f)
								.build()
						)
				.withEntry(ItemEntry.builder(MycoturgyItems.GLITTERING_SPORES)
						.build()
				).build());
			}
		});

		CompostingChanceRegistry.INSTANCE.add(MycoturgyItems.GLITTERING_SPORES, 0.3f);
		CompostingChanceRegistry.INSTANCE.add(MycoturgyItems.SPOREBRUSH, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(MycoturgyItems.SPORE_BUNDLE, 0.85f);

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
						MinecraftServer server = sworld.getServer();
						Advancement advancement = server.getAdvancementLoader().get(CAST_SPELL);
						server.getPlayerManager().getAdvancementTracker((ServerPlayerEntity)player).grantCriterion(advancement, "cast_spell");
						return ActionResult.SUCCESS;
					}
				}
			}
			return ActionResult.PASS;
		});

	}
}
