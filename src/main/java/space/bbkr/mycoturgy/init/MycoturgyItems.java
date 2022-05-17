package space.bbkr.mycoturgy.init;

import net.minecraft.tag.TagKey;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.item.*;

import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class MycoturgyItems {
	public static Item SPORE_BUNDLE;
	public static Item SPOREBRUSH;
	public static Item GLITTERING_SPORES;
	public static Item SPOREBRUSH_ASH;
	public static Item HAUSTORAL_BAND;
	public static Item MYCOTURGE_JOURNAL;
	public static Item SPORE_POUCH;
	public static Item SIDE_SWORD;
	public static Item MUSHROOM_SHIELD;

	//items which are made of netherite and impervious to flame - lodestones aren't included
	public static TagKey<Item> NETHERITE_COMPOSED;

	public static final ItemGroup MYCOTURGY_GROUP = QuiltItemGroup.createWithIcon(new Identifier(Mycoturgy.MODID, Mycoturgy.MODID), () -> new ItemStack(SPORE_BUNDLE));

	public static void init() {
		SPORE_BUNDLE = register("spore_bundle", new Item(new Item.Settings().group(MYCOTURGY_GROUP)));
		SPOREBRUSH = register("sporebrush", new Item(new Item.Settings().group(MYCOTURGY_GROUP)));
		GLITTERING_SPORES = register("glimmering_spores", new AliasedBlockItem(MycoturgyBlocks.SPOREBRUSH_CROP, new Item.Settings().group(MYCOTURGY_GROUP)));
		SPOREBRUSH_ASH = register("sporebrush_ash", new AliasedBlockItem(MycoturgyBlocks.SCATTERED_ASHES, new Item.Settings().group(MYCOTURGY_GROUP)));
		HAUSTORAL_BAND = register("haustoral_band", new HaustoralBandItem(new Item.Settings().maxCount(1).rarity(Rarity.RARE).group(MYCOTURGY_GROUP)));
		MYCOTURGE_JOURNAL = register("mycoturge_journal", new MycoturgeJournalItem(new Item.Settings().maxCount(1).group(MYCOTURGY_GROUP)));
		SPORE_POUCH = register("spore_pouch", new SporePouchItem(new Item.Settings().maxCount(1).group(MYCOTURGY_GROUP)));
		SIDE_SWORD = register("side_sword", new SideSwordItem(new Item.Settings().maxCount(1).group(MYCOTURGY_GROUP)));
		MUSHROOM_SHIELD = register("mushroom_shield", new MushroomShieldItem(new Item.Settings().maxCount(1).group(MYCOTURGY_GROUP)));

		NETHERITE_COMPOSED = TagKey.of(Registry.ITEM_KEY, new Identifier(Mycoturgy.MODID, "netherite_composed"));
	}

	private static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(Mycoturgy.MODID, name), item);
	}
}
