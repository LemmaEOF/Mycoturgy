package space.bbkr.mycoturgy.item;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketItem;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.init.MycoturgyItems;

import net.minecraft.advancement.Advancement;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class HaustoralBandItem extends TrinketItem {
	private static final Identifier EQUIP_BAND = new Identifier(Mycoturgy.MODID, "research/equip_haustoral_band");
	private static final Identifier FEEL_CORRUPTION = new Identifier(Mycoturgy.MODID, "research/feel_corruption");
	public HaustoralBandItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.HAND) && slot.equals(Slots.RING);
	}

	@Override
	public void onEquip(PlayerEntity player, ItemStack stack) {
		if (!player.world.isClient) {
			MinecraftServer server = player.world.getServer();
			Advancement advancement = server.getAdvancementLoader().get(EQUIP_BAND);
			server.getPlayerManager().getAdvancementTracker((ServerPlayerEntity)player).grantCriterion(advancement, "equip_band");
		}
	}

	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		if (player.world.isClient || player.world.getTime() % 30 != 0) return;
		//TODO: way to resist this
		for (int i = 0; i < player.inventory.size(); i++) {
			ItemStack invStack = player.inventory.getStack(i);
			if (invStack.getItem().isIn(MycoturgyItems.NETHERITE_COMPOSED)) {
				player.damage(CorruptionDamageSource.INSTANCE, 1f);
				MinecraftServer server = player.world.getServer();
				Advancement advancement = server.getAdvancementLoader().get(FEEL_CORRUPTION);
				server.getPlayerManager().getAdvancementTracker((ServerPlayerEntity)player).grantCriterion(advancement, "feel_corruption");
				return;
			}
		}
	}

	public static class CorruptionDamageSource extends DamageSource {
		public static final CorruptionDamageSource INSTANCE = new CorruptionDamageSource();

		private CorruptionDamageSource() {
			super("mycoturgy.corruption");
			this.setBypassesArmor();
			this.setUnblockable();
			this.setUsesMagic();
		}
	}
}
