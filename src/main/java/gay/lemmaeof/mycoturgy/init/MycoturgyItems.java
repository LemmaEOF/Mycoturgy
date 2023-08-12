package gay.lemmaeof.mycoturgy.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.item.*;

import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class MycoturgyItems {
	public static Item SPORE_BUNDLE;
	public static Item SPOREBRUSH;
	public static Item GLITTERING_SPORES;
	public static Item SPOREBRUSH_ASH;
	public static Item HAUSTORAL_BAND;
	public static Item MYCOTURGE_JOURNAL;
	public static Item SPORE_POUCH;
	public static Item LACED_DAGGER;
	public static Item SPRINGSTOOL_SHIELD;
	public static Item GRIEF_ARROW;
	public static Item BAND_OF_ENTERA;
	public static Item SPOREBRUSH_PIPE;
	public static Item SPOREBRUSH_LEAVES;

	//items which are made of netherite and impervious to flame - lodestones aren't included
	public static TagKey<Item> NETHERITE_COMPOSED;
	public static TagKey<Item> SPORE_POUCH_HOLDABLE;
	public static TagKey<Item> CASTING_BANDS;
	public static TagKey<Item> PIPE_LIGHTS;

	public static final ItemGroup MYCOTURGY_GROUP = FabricItemGroup.builder()
			.name(Text.translatable("group.mycoturgy.mycoturgy"))
			.icon(() -> new ItemStack(SPORE_BUNDLE))
			.build();
	public static final RegistryKey<ItemGroup> MYCOTURGY_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Mycoturgy.MODID, Mycoturgy.MODID));

	public static void init() {
		SPORE_BUNDLE = register("spore_bundle", new Item(new Item.Settings()));
		SPOREBRUSH = register("sporebrush", new Item(new Item.Settings()));
		GLITTERING_SPORES = register("glimmering_spores", new AliasedBlockItem(MycoturgyBlocks.SPOREBRUSH_CROP, new Item.Settings()));
		SPOREBRUSH_ASH = register("sporebrush_ash", new AliasedBlockItem(MycoturgyBlocks.SCATTERED_ASHES, new Item.Settings()));
		HAUSTORAL_BAND = register("haustoral_band", new HaustoralBandItem(new Item.Settings().maxCount(1).rarity(Rarity.RARE)));
		MYCOTURGE_JOURNAL = register("mycoturge_journal", new MycoturgeJournalItem(new Item.Settings().maxCount(1)));
		SPORE_POUCH = register("spore_pouch", new SporePouchItem(new Item.Settings().maxCount(1)));
		LACED_DAGGER = register("laced_dagger", new LacedDaggerItem(new Item.Settings().maxCount(1)));
		SPRINGSTOOL_SHIELD = register("springstool_shield", new SpringstoolShieldItem(new Item.Settings().maxCount(1)));
		GRIEF_ARROW = register("grief_arrow", new ArrowItem(new Item.Settings()));
		BAND_OF_ENTERA = register("band_of_entera", new BandOfEnteraItem(new Item.Settings()));
		SPOREBRUSH_PIPE = register("sporebrush_pipe", new SporebrushPipeItem(new Item.Settings().maxCount(1)));
		SPOREBRUSH_LEAVES = register("sporebrush_leaves", new Item(new Item.Settings()));

		NETHERITE_COMPOSED = TagKey.of(Registries.ITEM.getKey(), new Identifier(Mycoturgy.MODID, "netherite_composed"));
		SPORE_POUCH_HOLDABLE = TagKey.of(Registries.ITEM.getKey(), new Identifier(Mycoturgy.MODID, "spore_pouch_holdable"));
		CASTING_BANDS = TagKey.of(Registries.ITEM.getKey(), new Identifier(Mycoturgy.MODID, "casting_bands"));
		PIPE_LIGHTS = TagKey.of(Registries.ITEM.getKey(), new Identifier(Mycoturgy.MODID, "pipe_lights"));

		Registry.register(Registries.ITEM_GROUP, MYCOTURGY_GROUP_KEY, MYCOTURGY_GROUP);

		ItemGroupEvents.modifyEntriesEvent(MYCOTURGY_GROUP_KEY).register(entries -> {
			//TODO: re-order eventually, this is quick and dirty for modfest
			entries.addItem(MycoturgyBlocks.HAUSTOR_SEQUESTER);
			entries.addItem(MycoturgyBlocks.MASON_JAR);
			entries.addItem(MycoturgyBlocks.PADDLE_RHIZOME_SPORES);
			entries.addItem(MycoturgyBlocks.SMALL_SPRINGSTOOL);
			entries.addItem(MycoturgyBlocks.MEDIUM_SPRINGSTOOL);
			entries.addItem(MycoturgyBlocks.LARGE_SPRINGSTOOL);
			entries.addItem(MycoturgyBlocks.COOKING_POT);
			entries.addItem(MycoturgyBlocks.PADDLE_RHIZOME);
			entries.addItem(MycoturgyBlocks.STURDY_GLOWCAP);
			entries.addItem(MycoturgyBlocks.CLINGY_GLOWCAP);
			entries.addItem(MycoturgyBlocks.INFESTED_MOB_SPAWNER);
			entries.addItem(SPORE_BUNDLE);
			entries.addItem(SPOREBRUSH);
			entries.addItem(GLITTERING_SPORES);
			entries.addItem(SPOREBRUSH_ASH);
			entries.addItem(HAUSTORAL_BAND);
			entries.addItem(MYCOTURGE_JOURNAL);
			entries.addItem(SPORE_POUCH);
			entries.addItem(LACED_DAGGER);
			entries.addItem(SPRINGSTOOL_SHIELD);
			entries.addItem(GRIEF_ARROW);
			entries.addItem(BAND_OF_ENTERA);
			entries.addItem(SPOREBRUSH_PIPE);
			entries.addItem(SPOREBRUSH_LEAVES);
		});
	}

	private static Item register(String name, Item item) {
		return Registry.register(Registries.ITEM, new Identifier(Mycoturgy.MODID, name), item);
	}
}
