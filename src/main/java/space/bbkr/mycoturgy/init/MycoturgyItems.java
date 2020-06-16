package space.bbkr.mycoturgy.init;

import space.bbkr.mycoturgy.Mycoturgy;

import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;

public class MycoturgyItems {
	public static Item SPORE_BUNDLE;
	public static Item SPOREBRUSH;
	public static Item GLITTERING_SPORES;
	public static Item SPOREBRUSH_ASH;

	public static final ItemGroup MYCOTURGY_GROUP = FabricItemGroupBuilder.build(new Identifier(Mycoturgy.MODID, Mycoturgy.MODID), () -> new ItemStack(SPORE_BUNDLE));

	public static void init() {
		SPORE_BUNDLE = register("spore_bundle", new Item(new Item.Settings().group(MYCOTURGY_GROUP)));
		SPOREBRUSH = register("sporebrush", new Item(new Item.Settings().group(MYCOTURGY_GROUP)));
		GLITTERING_SPORES = register("glimmering_spores", new AliasedBlockItem(MycoturgyBlocks.SPOREBRUSH_CROP, new Item.Settings().group(MYCOTURGY_GROUP)));
		SPOREBRUSH_ASH = register("sporebrush_ash", new Item(new Item.Settings().group(MYCOTURGY_GROUP)));
	}

	private static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(Mycoturgy.MODID, name), item);
	}
}
