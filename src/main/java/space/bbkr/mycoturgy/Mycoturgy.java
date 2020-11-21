package space.bbkr.mycoturgy;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import space.bbkr.mycoturgy.init.MycoturgyBlocks;
import space.bbkr.mycoturgy.init.MycoturgyItems;
import space.bbkr.mycoturgy.init.MycoturgyRecipes;

import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;

public class Mycoturgy implements ModInitializer {
	public static final String MODID = "mycoturgy";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	//TODO: config
	public static final int HAUSTOR_TICK_SPEED = 10;

	@Override
	public void onInitialize() {
		MycoturgyBlocks.init();
		MycoturgyItems.init();
		MycoturgyRecipes.init();
		TrinketSlots.addSlot(SlotGroups.HAND, Slots.RING, new Identifier("trinkets",
				"textures/item/empty_trinket_slot_ring.png"));

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

	}
}
