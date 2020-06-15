package space.bbkr.mycoturgy;

import java.util.function.Supplier;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.ChunkComponentCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import space.bbkr.mycoturgy.block.CustomCropBlock;
import space.bbkr.mycoturgy.block.HaustorSequesterBlock;
import space.bbkr.mycoturgy.block.entity.HaustorSequesterBlockEntity;
import space.bbkr.mycoturgy.component.HaustorComponent;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;

public class Mycoturgy implements ModInitializer {
	public static final String MODID = "mycoturgy";

	public static final Logger logger = LogManager.getLogger();

	//TODO: config
	public static final int HAUSTOR_TICK_SPEED = 10;

	public static Block SPOREBRUSH_CROP;
	public static Block HAUSTOR_SEQUESTER;

	public static BlockEntityType<HaustorSequesterBlockEntity> HAUSTOR_SEQUESTER_BLOCK_ENTITY;

	public static Item SPORE_BUNDLE;
	public static Item SPOREBRUSH;
	public static Item GLITTERING_SPORES;
	public static Item SPOREBRUSH_ASH;

	public static final ItemGroup MYCOTURGE_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(SPORE_BUNDLE));

	//TODO: static reg?
	public static final ComponentType<HaustorComponent> HAUSTOR_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(MODID, "haustor"), HaustorComponent.class);

	@Override
	public void onInitialize() {
		SPOREBRUSH_CROP = register("sporebrush", new CustomCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT).breakByHand(true)));
		HAUSTOR_SEQUESTER = register("haustor_sequester", new HaustorSequesterBlock(FabricBlockSettings.copyOf(Blocks.GRASS)), new Item.Settings().group(MYCOTURGE_GROUP));
		HAUSTOR_SEQUESTER_BLOCK_ENTITY = register("haustor_sequester", HaustorSequesterBlockEntity::new, HAUSTOR_SEQUESTER);
		SPORE_BUNDLE = register("spore_bundle", new Item(new Item.Settings().group(MYCOTURGE_GROUP)));
		SPOREBRUSH = register("sporebrush", new Item(new Item.Settings().group(MYCOTURGE_GROUP)));
		GLITTERING_SPORES = register("glimmering_spores", new AliasedBlockItem(SPOREBRUSH_CROP, new Item.Settings().group(MYCOTURGE_GROUP)));
		SPOREBRUSH_ASH = register("sporebrush_ash", new Item(new Item.Settings().group(MYCOTURGE_GROUP)));

		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, builder, table) -> {
			if (id.equals(new Identifier("blocks/grass"))) {
				builder.withPool(FabricLootPoolBuilder.builder()
						.withCondition(RandomChanceLootCondition.builder(0.1f)
								.build()
						)
				.withEntry(ItemEntry.builder(GLITTERING_SPORES)
						.build()
				).build());
			}
		});

		CompostingChanceRegistry.INSTANCE.add(GLITTERING_SPORES, 0.3f);
		CompostingChanceRegistry.INSTANCE.add(SPOREBRUSH, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(SPORE_BUNDLE, 0.85f);

		//TODO: static reg
		ChunkComponentCallback.register(HAUSTOR_COMPONENT, HaustorComponent::new);
	}

	private static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(MODID, name), item);
	}

	private static Block register(String name, Block block, Item.Settings settings) {
		Registry.register(Registry.BLOCK, new Identifier(MODID, name), block);
		Registry.register(Registry.ITEM, new Identifier(MODID, name), new BlockItem(block, settings));
		return block;
	}

	private static Block register(String name, Block block) {
		return Registry.register(Registry.BLOCK, new Identifier(MODID, name), block);
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(String name, Supplier<T> be, Block...blocks) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, name), BlockEntityType.Builder.create(be, blocks).build(null));
	}
}
