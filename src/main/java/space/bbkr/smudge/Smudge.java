package space.bbkr.smudge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import space.bbkr.smudge.block.CustomCropBlock;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.AliasedBlockItem;
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

public class Smudge implements ModInitializer {
	public static final String MODID = "smudge";

	public static final Logger logger = LogManager.getLogger();

	public static Block SAGE_CROP;

	public static Item SMUDGE_STICK;
	public static Item SAGE;
	public static Item SAGE_SEEDS;

	public static ItemGroup SMUDGE_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(SMUDGE_STICK));

	@Override
	public void onInitialize() {
		SAGE_CROP = register("sage", new CustomCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT).breakByHand(true)));
		SMUDGE_STICK = register("smudge_stick", new Item(new Item.Settings().group(SMUDGE_GROUP)));
		SAGE = register("sage", new Item(new Item.Settings().group(SMUDGE_GROUP)));
		SAGE_SEEDS = register("sage_seeds", new AliasedBlockItem(SAGE_CROP, new Item.Settings().group(SMUDGE_GROUP)));

		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, builder, table) -> {
			if (id.equals(new Identifier("blocks/grass"))) {
				builder.withPool(FabricLootPoolBuilder.builder()
						.withCondition(RandomChanceLootCondition.builder(0.1f)
								.build()
						)
				.withEntry(ItemEntry.builder(SAGE_SEEDS)
						.build()
				).build());
			}
		});
	}

	private static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(MODID, name), item);
	}

	private static Block register(String name, Block block) {
		return Registry.register(Registry.BLOCK, new Identifier(MODID, name), block);
	}
}
