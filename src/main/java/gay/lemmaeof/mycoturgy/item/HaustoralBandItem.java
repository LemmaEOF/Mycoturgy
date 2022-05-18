package gay.lemmaeof.mycoturgy.item;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.init.MycoturgyItems;
import gay.lemmaeof.mycoturgy.init.MycoturgyEffects;

import net.minecraft.advancement.Advancement;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HaustoralBandItem extends TrinketItem {
	private static final UUID SLOT_ADD_UUID = UUID.fromString("e51bea70-52a2-496a-abb6-f84d9cadafdb");
	private static final Identifier EQUIP_BAND = new Identifier(Mycoturgy.MODID, "research/equip_haustoral_band");
	public HaustoralBandItem(Settings settings) {
		super(settings);
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
	public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		return !stack.getOrCreateNbt().getBoolean("Bound") || (entity instanceof PlayerEntity player && player.isCreative());
	}

	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (!entity.world.isClient && entity instanceof ServerPlayerEntity player) {
			MinecraftServer server = entity.world.getServer();
			Advancement advancement = server.getAdvancementLoader().get(EQUIP_BAND);
			server.getPlayerManager().getAdvancementTracker(player).grantCriterion(advancement, "equip_band");
			stack.getOrCreateNbt().putBoolean("Bound", true);
		}
	}

	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (entity.hasStatusEffect(MycoturgyEffects.GRIEF)) {
			entity.removeStatusEffect(MycoturgyEffects.GRIEF);
		}
	}

	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (entity instanceof PlayerEntity player) {
			if (player.world.isClient) return;
			//TODO: way to resist this
			for (int i = 0; i < player.getInventory().size(); i++) {
				ItemStack invStack = player.getInventory().getStack(i);
				if (invStack.isIn(MycoturgyItems.NETHERITE_COMPOSED)) {
					if (!player.hasStatusEffect(MycoturgyEffects.GRIEF)) {
						player.addStatusEffect(new StatusEffectInstance(MycoturgyEffects.GRIEF, 120000, 0, false, false, true));
					} else {
						StatusEffectInstance effect = player.getStatusEffect(MycoturgyEffects.GRIEF);
						if (effect.getDuration() <= 10000) {
							player.addStatusEffect(new StatusEffectInstance(MycoturgyEffects.GRIEF, 120000, 0, false, false, true));
						}
					}
					return;
				}
			}
			player.removeStatusEffect(MycoturgyEffects.GRIEF);
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		if (stack.getOrCreateNbt().getBoolean("Bound")) {
			tooltip.add(new LiteralText("tooltip.mycoturgy.haustoral_band.bound").styled(style -> style.withColor(0x00CC55)));
		}
	}
}
