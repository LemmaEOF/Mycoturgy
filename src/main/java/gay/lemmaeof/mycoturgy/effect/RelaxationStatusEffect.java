package gay.lemmaeof.mycoturgy.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class RelaxationStatusEffect extends StatusEffect {
	public RelaxationStatusEffect(StatusEffectType type, int color) {
		super(type, color);
		this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "e22e3d43-0561-4639-a312-2fafbdf272f7", -0.05F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		//only *barely* faster than Grief - make it pretty much just cancel out Grief with health returns not suitable for combat
		//that way you still can't really use netherite for fighting with Grief!

		return amplifier == 1? duration % 49 == 0 : duration % 59 == 0;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity.getHealth() > 0F) {
			//bypass `heal` because Grief blocks that!
			entity.setHealth(entity.getHealth() + 1);
		}
	}

	@Override
	public double adjustModifierAmount(int amplifier, EntityAttributeModifier modifier) {
		//don't slow down for a non-full hit
		return modifier.getValue() * (amplifier);
	}
}
