package gay.lemmaeof.mycoturgy.effect;

import gay.lemmaeof.mycoturgy.init.MycoturgyCriteria;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.server.network.ServerPlayerEntity;

public class GriefStatusEffect extends StatusEffect {

	public GriefStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % 60 == 0;
	}

	@Override
	public void applyUpdateEffect(LivingEntity player, int amplifier) {
		player.damage(GriefDamageSource.INSTANCE, 1f);
		if (player instanceof ServerPlayerEntity p) {
			MycoturgyCriteria.FEEL_GRIEF.trigger(p);
		}
	}

	public static class GriefDamageSource extends DamageSource {
		public static final GriefDamageSource INSTANCE = new GriefDamageSource();

		private GriefDamageSource() {
			super("mycoturgy.grief");
			this.setBypassesArmor();
			this.setUnblockable();
			this.setUsesMagic();
		}
	}
}
