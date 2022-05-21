package gay.lemmaeof.mycoturgy.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class RelaxationStatusEffect extends StatusEffect {
	public RelaxationStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % 50 == 0;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity.getHealth() > 0F) {
			//bypass `heal` because Grief blocks that!
			entity.setHealth(entity.getHealth() + 1);
		}
	}
}
