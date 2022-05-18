package gay.lemmaeof.mycoturgy.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class MushroomShieldItem extends CustomBlockingItem {
	public MushroomShieldItem(Settings settings) {
		super(settings);
	}

	@Override
	public void takeHit(LivingEntity user, ItemStack stack, LivingEntity attacker) {
		attacker.takeKnockback(3.0F, user.getX() - attacker.getX(), user.getZ() - attacker.getZ());
	}

	@Override
	public void damageStack(LivingEntity user, ItemStack stack, float amount) {
		//TODO
	}
}
