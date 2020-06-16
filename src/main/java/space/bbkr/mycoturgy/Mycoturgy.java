package space.bbkr.mycoturgy;

import java.util.function.Supplier;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.ChunkComponentCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import space.bbkr.mycoturgy.block.CustomCropBlock;
import space.bbkr.mycoturgy.block.HaustorSequesterBlock;
import space.bbkr.mycoturgy.block.MasonJarBlock;
import space.bbkr.mycoturgy.block.entity.HaustorSequesterBlockEntity;
import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.init.MycoturgyBlocks;
import space.bbkr.mycoturgy.init.MycoturgyItems;
import space.bbkr.mycoturgy.recipe.PatchouliBookRecipe;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
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
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;

public class Mycoturgy implements ModInitializer {
	public static final String MODID = "mycoturgy";

	public static final Logger logger = LogManager.getLogger();

	//TODO: config
	public static final int HAUSTOR_TICK_SPEED = 10;

	//TODO: static reg?
	public static final ComponentType<HaustorComponent> HAUSTOR_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(MODID, "haustor"), HaustorComponent.class);

	@Override
	public void onInitialize() {
		MycoturgyBlocks.init();
		MycoturgyItems.init();

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

		//TODO: static reg
		ChunkComponentCallback.register(HAUSTOR_COMPONENT, HaustorComponent::new);

		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MODID, "patchouli_book"), new PatchouliBookRecipe.Serializer());
	}
}
