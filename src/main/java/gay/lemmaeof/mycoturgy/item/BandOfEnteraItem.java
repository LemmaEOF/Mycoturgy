package gay.lemmaeof.mycoturgy.item;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketEnums;
import dev.emi.trinkets.api.TrinketItem;
import gay.lemmaeof.mycoturgy.Mycoturgy;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.UUID;

public class BandOfEnteraItem extends TrinketItem {
	private static final UUID SLOT_ADD_UUID = UUID.fromString("e51bea70-52a2-496a-abb6-f84d9cadafdb");
	private static final Identifier EQUIP_BAND = new Identifier(Mycoturgy.MODID, "research/equip_band_of_entera");

	public BandOfEnteraItem(Settings settings) {
		super(settings);
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return Rarity.EPIC;
	}

	@Override
	public TrinketEnums.DropRule getDropRule(ItemStack stack, SlotReference slot, LivingEntity entity) {
		return TrinketEnums.DropRule.KEEP;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> parent = super.getModifiers(stack, slot, entity, uuid);
		SlotAttributes.addSlotModifier(parent, "hand/ring", SLOT_ADD_UUID, 1, EntityAttributeModifier.Operation.ADDITION);
		return parent;
	}

	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (!entity.world.isClient && entity instanceof ServerPlayerEntity player) {
			MinecraftServer server = entity.world.getServer();
			Advancement advancement = server.getAdvancementLoader().get(EQUIP_BAND);
			server.getPlayerManager().getAdvancementTracker(player).grantCriterion(advancement, "equip_band");
		}
	}
}
