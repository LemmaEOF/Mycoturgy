package space.bbkr.mycoturgy.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.tag.TagKey;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.block.BouncePadBlock;
import space.bbkr.mycoturgy.block.CookingPotBlock;
import space.bbkr.mycoturgy.block.CustomCropBlock;
import space.bbkr.mycoturgy.block.HaustorSequesterBlock;
import space.bbkr.mycoturgy.block.MasonJarBlock;
import space.bbkr.mycoturgy.block.MushrooomLampBlock;
import space.bbkr.mycoturgy.block.ScatteredAshesBlock;
import space.bbkr.mycoturgy.block.entity.CookingPotBlockEntity;
import space.bbkr.mycoturgy.block.entity.HaustorSequesterBlockEntity;
import space.bbkr.mycoturgy.block.entity.MasonJarBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MycoturgyBlocks {
	public static Block SPOREBRUSH_CROP;
	public static Block HAUSTOR_SEQUESTER;
	public static Block MASON_JAR;
	public static Block SCATTERED_ASHES;
	public static Block PADDLE_RHIZOME_SPORES;
	public static Block TEST_BOUNCE_PAD;
	public static Block COOKING_POT;
	public static Block PADDLE_RHIZOME;
	public static Block LIGHTSHROOM;

	public static BlockEntityType<HaustorSequesterBlockEntity> HAUSTOR_SEQUESTER_BLOCK_ENTITY;
	public static BlockEntityType<MasonJarBlockEntity> MASON_JAR_BLOCK_ENTITY;
	public static BlockEntityType<CookingPotBlockEntity> COOKING_POT_BLOCK_ENTITY;

	public static TagKey<Block> SPELL_CASTABLE;

	public static void init() {
		SPOREBRUSH_CROP = register("sporebrush",
				new CustomCropBlock(QuiltBlockSettings.copyOf(Blocks.WHEAT)
				)
		);
		HAUSTOR_SEQUESTER = register("haustor_sequester",
				new HaustorSequesterBlock(
						QuiltBlockSettings.copyOf(Blocks.GRASS)
				), new Item.Settings()
						.group(MycoturgyItems.MYCOTURGY_GROUP)
		);
		MASON_JAR = register("mason_jar",
				//break by pickaxes
				new MasonJarBlock(QuiltBlockSettings.of(Material.GLASS)
						.nonOpaque()
				), new Item.Settings()
						.group(MycoturgyItems.MYCOTURGY_GROUP)
		);
		SCATTERED_ASHES = register("scattered_ashes",
				new ScatteredAshesBlock(QuiltBlockSettings.of(Material.DECORATION)
						.breakInstantly()
						.nonOpaque()
						.noCollision()
						.sounds(BlockSoundGroup.SAND)
				)
		);
		PADDLE_RHIZOME_SPORES = register("paddle_rhizome_spores",
				new ScatteredAshesBlock(QuiltBlockSettings.of(Material.DECORATION)
						.breakInstantly()
						.nonOpaque()
						.noCollision()
						.sounds(BlockSoundGroup.LILY_PAD)
				), new Item.Settings()
						.group(MycoturgyItems.MYCOTURGY_GROUP)
		);
		TEST_BOUNCE_PAD = register("test_bounce_pad",
				new BouncePadBlock(1.5,
						QuiltBlockSettings.of(Material.ORGANIC_PRODUCT)
				), new Item.Settings()
						.group(MycoturgyItems.MYCOTURGY_GROUP)
		);
		COOKING_POT = register("cooking_pot",
				//break by pickaxes
				new CookingPotBlock(QuiltBlockSettings.of(Material.METAL)
						.strength(0.5F)
						.nonOpaque()
				), new Item.Settings()
						.group(MycoturgyItems.MYCOTURGY_GROUP)
		);
		//thanks lovelymimic for the request!
		PADDLE_RHIZOME = register("paddle_rhizome",
				new Block(QuiltBlockSettings.of(Material.ORGANIC_PRODUCT)
				), new Item.Settings()
						.group(MycoturgyItems.MYCOTURGY_GROUP)
		);
		LIGHTSHROOM = register("lightshroom",
				new MushrooomLampBlock(QuiltBlockSettings.of(Material.ORGANIC_PRODUCT)
						.luminance(12)
						.ticksRandomly()
				), new Item.Settings()
						.group(MycoturgyItems.MYCOTURGY_GROUP)
		);

		HAUSTOR_SEQUESTER_BLOCK_ENTITY = register("haustor_sequester",
				HaustorSequesterBlockEntity::new,
				HAUSTOR_SEQUESTER
		);
		MASON_JAR_BLOCK_ENTITY = register("mason_jar",
				MasonJarBlockEntity::new,
				MASON_JAR
		);
		COOKING_POT_BLOCK_ENTITY = register("cooking_pot",
				CookingPotBlockEntity::new,
				COOKING_POT
		);

		SPELL_CASTABLE = TagKey.of(Registry.BLOCK_KEY, new Identifier(Mycoturgy.MODID, "spell_castable"));
	}

	private static Block register(String name, Block block, Item.Settings settings) {
		Registry.register(Registry.BLOCK, new Identifier(Mycoturgy.MODID, name), block);
		Registry.register(Registry.ITEM, new Identifier(Mycoturgy.MODID, name), new BlockItem(block, settings));
		return block;
	}

	private static Block register(String name, Block block) {
		return Registry.register(Registry.BLOCK, new Identifier(Mycoturgy.MODID, name), block);
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<T> be, Block...blocks) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Mycoturgy.MODID, name), FabricBlockEntityTypeBuilder.create(be, blocks).build(null));
	}
}
