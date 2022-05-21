package gay.lemmaeof.mycoturgy.init;

import net.minecraft.item.*;
import net.minecraft.tag.TagKey;
import org.lwjgl.system.CallbackI;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.item.*;

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
	public static Item GRIEF_ARROW;
	public static Item BAND_OF_ENTERA;
	public static Item SPOREBRUSH_PIPE;
	public static Item SPOREBRUSH_LEAVES;

	//items which are made of netherite and impervious to flame - lodestones aren't included
	public static TagKey<Item> NETHERITE_COMPOSED;
	public static TagKey<Item> SPORE_POUCH_HOLDABLE;
	public static TagKey<Item> CASTING_BANDS;
	public static TagKey<Item> PIPE_LIGHTS;

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
		GRIEF_ARROW = register("grief_arrow", new ArrowItem(new Item.Settings().group(MYCOTURGY_GROUP)));
		BAND_OF_ENTERA = register("band_of_entera", new BandOfEnteraItem(new Item.Settings().group(MYCOTURGY_GROUP)));
		SPOREBRUSH_PIPE = register("sporebrush_pipe", new SporebrushPipeItem(new Item.Settings().maxCount(1).group(MYCOTURGY_GROUP)));
		SPOREBRUSH_LEAVES = register("sporebrush_leaves", new Item(new Item.Settings().group(MYCOTURGY_GROUP)));

		NETHERITE_COMPOSED = TagKey.of(Registry.ITEM_KEY, new Identifier(Mycoturgy.MODID, "netherite_composed"));
		SPORE_POUCH_HOLDABLE = TagKey.of(Registry.ITEM_KEY, new Identifier(Mycoturgy.MODID, "spore_pouch_holdable"));
		CASTING_BANDS = TagKey.of(Registry.ITEM_KEY, new Identifier(Mycoturgy.MODID, "casting_bands"));
		PIPE_LIGHTS = TagKey.of(Registry.ITEM_KEY, new Identifier(Mycoturgy.MODID, "pipe_lights"));
	}

	private static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(Mycoturgy.MODID, name), item);
	}
}
