package space.bbkr.mycoturgy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.bbkr.mycoturgy.init.MycoturgyEffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
	@Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

	public MixinLivingEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "heal", at = @At("HEAD"), cancellable = true)
	private void cancelHealing(float amount, CallbackInfo info) {
		if (this.hasStatusEffect(MycoturgyEffects.GRIEF)) {
			info.cancel();
		}
	}
}
