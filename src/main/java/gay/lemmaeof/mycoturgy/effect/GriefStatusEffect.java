package gay.lemmaeof.mycoturgy.effect;

import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.init.MycoturgyCriteria;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class GriefStatusEffect extends StatusEffect {
	private static final RegistryKey<DamageType> GRIEF_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Mycoturgy.MODID, "grief"));

	public GriefStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % 60 == 0;
	}

	@Override
	public void applyUpdateEffect(LivingEntity player, int amplifier) {
		player.damage(player.getWorld().getDamageSources().create(GRIEF_DAMAGE), 1f);
		if (player instanceof ServerPlayerEntity p) {
			MycoturgyCriteria.FEEL_GRIEF.trigger(p);
		}
	}
}
