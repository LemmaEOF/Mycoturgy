package gay.lemmaeof.mycoturgy.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import gay.lemmaeof.mycoturgy.init.MycoturgyEffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.world.World;
import gay.lemmaeof.mycoturgy.item.CustomBlockingItem;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
	@Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

	@Shadow protected ItemStack activeItemStack;

	public MixinLivingEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "heal", at = @At("HEAD"), cancellable = true)
	private void cancelHealing(float amount, CallbackInfo info) {
		if (this.hasStatusEffect(MycoturgyEffects.GRIEF)) {
			info.cancel();
		}
	}

	@Inject(method = "takeShieldHit", at = @At("HEAD"), cancellable = true)
	private void hookCustomBlock(LivingEntity attacker, CallbackInfo info) {
		if (this.activeItemStack.getItem() instanceof CustomBlockingItem blocking) {
			blocking.takeHit((LivingEntity) (Object) this, this.activeItemStack, attacker);
			info.cancel();
		}
	}
}
