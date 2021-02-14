package space.bbkr.mycoturgy.init;

import java.util.function.Supplier;

import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.block.BouncePadBlock;
import space.bbkr.mycoturgy.block.CookingPotBlock;
import space.bbkr.mycoturgy.block.CustomCropBlock;
import space.bbkr.mycoturgy.block.HaustorSequesterBlock;
import space.bbkr.mycoturgy.block.MasonJarBlock;
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
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;

public class MycoturgyBlocks {
	public static Block SPOREBRUSH_CROP;
	public static Block HAUSTOR_SEQUESTER;
	public static Block MASON_JAR;
	public static Block SCATTERED_ASHES;
	public static Block PADDLE_RHIZOME_SPORES;
	public static Block TEST_BOUNCE_PAD;
	public static Block COOKING_POT;
	public static Block PADDLE_RHIZOME;

	public static BlockEntityType<HaustorSequesterBlockEntity> HAUSTOR_SEQUESTER_BLOCK_ENTITY;
	public static BlockEntityType<MasonJarBlockEntity> MASON_JAR_BLOCK_ENTITY;
	public static BlockEntityType<CookingPotBlockEntity> COOKING_POT_BLOCK_ENTITY;

	public static Tag<Block> SPELL_CASTABLE;

	public static void init() {
		SPOREBRUSH_CROP = register("sporebrush",
				new CustomCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT)
						.breakByHand(true)
				)
		);
		HAUSTOR_SEQUESTER = register("haustor_sequester",
				new HaustorSequesterBlock(
						FabricBlockSettings.copyOf(Blocks.GRASS)
								.breakByHand(true)
				), new Item.Settings()
						.group(MycoturgyItems.MYCOTURGY_GROUP)
		);
		MASON_JAR = register("mason_jar",
				new MasonJarBlock(FabricBlockSettings.of(Material.GLASS)
						.breakByTool(FabricToolTags.PICKAXES)
						.nonOpaque()
				), new Item.Settings()
						.group(MycoturgyItems.MYCOTURGY_GROUP)
		);
		SCATTERED_ASHES = register("scattered_ashes",
				new ScatteredAshesBlock(FabricBlockSettings.of(Material.SUPPORTED)
						.breakByHand(true)
						.breakInstantly()
						.nonOpaque()
						.noCollision()
						.sounds(BlockSoundGroup.SAND)
				)
		);
		PADDLE_RHIZOME_SPORES = register("paddle_rhizome_spores",
				new ScatteredAshesBlock(FabricBlockSettings.of(Material.SUPPORTED)
						.breakByHand(true)
						.breakInstantly()
						.nonOpaque()
						.noCollision()
						.sounds(BlockSoundGroup.LILY_PAD)
				), new Item.Settings()
						.group(MycoturgyItems.MYCOTURGY_GROUP)
		);
		TEST_BOUNCE_PAD = register("test_bounce_pad",
				new BouncePadBlock(1.5,
						FabricBlockSettings.of(Material.ORGANIC_PRODUCT)
				), new Item.Settings()
						.group(MycoturgyItems.MYCOTURGY_GROUP)
		);
		COOKING_POT = register("cooking_pot",
				new CookingPotBlock(FabricBlockSettings.of(Material.METAL)
						.strength(0.5F)
						.breakByTool(FabricToolTags.PICKAXES)
						.nonOpaque()
				), new Item.Settings()
						.group(MycoturgyItems.MYCOTURGY_GROUP)
		);
		//thanks lovelymimic for the request!
		PADDLE_RHIZOME = register("paddle_rhizome",
				new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT)
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

		SPELL_CASTABLE = TagRegistry.block(new Identifier(Mycoturgy.MODID, "spell_castable"));
	}

	private static Block register(String name, Block block, Item.Settings settings) {
		Registry.register(Registry.BLOCK, new Identifier(Mycoturgy.MODID, name), block);
		Registry.register(Registry.ITEM, new Identifier(Mycoturgy.MODID, name), new BlockItem(block, settings));
		return block;
	}

	private static Block register(String name, Block block) {
		return Registry.register(Registry.BLOCK, new Identifier(Mycoturgy.MODID, name), block);
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(String name, Supplier<T> be, Block...blocks) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Mycoturgy.MODID, name), BlockEntityType.Builder.create(be, blocks).build(null));
	}
}
