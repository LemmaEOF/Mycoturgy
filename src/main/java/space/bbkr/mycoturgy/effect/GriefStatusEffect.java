package space.bbkr.mycoturgy.effect;

import space.bbkr.mycoturgy.Mycoturgy;

import net.minecraft.advancement.Advancement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class GriefStatusEffect extends StatusEffect {
	private static final Identifier FEEL_CORRUPTION = new Identifier(Mycoturgy.MODID, "research/feel_grief");

	public GriefStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % 30 == 5;
	}

	@Override
	public void applyUpdateEffect(LivingEntity player, int amplifier) {
		if (player instanceof ServerPlayerEntity) {
			player.damage(GriefDamageSource.INSTANCE, 1f);
			MinecraftServer server = player.world.getServer();
			Advancement advancement = server.getAdvancementLoader().get(FEEL_CORRUPTION);
			server.getPlayerManager().getAdvancementTracker((ServerPlayerEntity) player).grantCriterion(advancement, "feel_grief");
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
