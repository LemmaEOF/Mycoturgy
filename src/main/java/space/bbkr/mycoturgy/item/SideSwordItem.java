package space.bbkr.mycoturgy.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import space.bbkr.mycoturgy.init.MycoturgyItems;

public class SideSwordItem extends CustomBlockingItem {
	public SideSwordItem(Settings settings) {
		super(settings);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (user instanceof PlayerEntity player) {
			player.getItemCooldownManager().set(MycoturgyItems.SIDE_SWORD, 60);
		}
		return stack;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 15;
	}

	@Override
	public void takeHit(LivingEntity user, ItemStack stack, LivingEntity attacker) {
		if (user instanceof PlayerEntity player) {
			attacker.damage(DamageSource.player(player), 4);
			player.clearActiveItem();
			player.getItemCooldownManager().set(MycoturgyItems.SIDE_SWORD, 5);
		}
	}

	@Override
	public void damageStack(LivingEntity user, ItemStack stack, float amount) {
		//TODO
	}
}
