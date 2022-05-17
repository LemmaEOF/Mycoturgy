package space.bbkr.mycoturgy.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.init.MycoturgyItems;
import space.bbkr.mycoturgy.init.MycoturgyEffects;

import net.minecraft.advancement.Advancement;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class HaustoralBandItem extends TrinketItem {
	private static final Identifier EQUIP_BAND = new Identifier(Mycoturgy.MODID, "research/equip_haustoral_band");
	public HaustoralBandItem(Settings settings) {
		super(settings);
	}


	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (!entity.world.isClient && entity instanceof ServerPlayerEntity player) {
			MinecraftServer server = entity.world.getServer();
			Advancement advancement = server.getAdvancementLoader().get(EQUIP_BAND);
			server.getPlayerManager().getAdvancementTracker(player).grantCriterion(advancement, "equip_band");
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

}
