package space.bbkr.mycoturgy.init;

import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.effect.GriefStatusEffect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MycoturgyEffects {
	public static final StatusEffect GRIEF = register("grief", new GriefStatusEffect(StatusEffectType.HARMFUL, 0x25C6CD));

	public static void init() {

	}

	private static StatusEffect register(String name, StatusEffect effect) {
		return Registry.register(Registry.STATUS_EFFECT, new Identifier(Mycoturgy.MODID, name), effect);
	}
}
