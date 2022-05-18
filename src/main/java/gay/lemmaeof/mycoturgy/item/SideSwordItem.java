package gay.lemmaeof.mycoturgy.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import gay.lemmaeof.mycoturgy.init.MycoturgyItems;

public class SideSwordItem extends CustomBlockingItem {
	public SideSwordItem(Settings settings) {
		super(settings);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (user instanceof PlayerEntity player) {
			player.getItemCooldownManager().set(MycoturgyItems.SIDE_SWORD, 40);
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
			if (player.getStackInHand(Hand.MAIN_HAND) == stack) {
				player.swingHand(Hand.MAIN_HAND, true);
			} else {
				player.swingHand(Hand.OFF_HAND, true);
			}
			player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1f, 1f);
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
