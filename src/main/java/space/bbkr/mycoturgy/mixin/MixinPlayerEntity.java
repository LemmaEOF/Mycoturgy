package space.bbkr.mycoturgy.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.bbkr.mycoturgy.item.CustomBlockingItem;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "damageShield", at = @At("HEAD"), cancellable = true)
	private void hookCustomBlock(float amount, CallbackInfo info) {
		if (this.activeItemStack.getItem() instanceof CustomBlockingItem blocking) {
			blocking.damageStack(this, this.activeItemStack, amount);
			info.cancel();
		}
	}
}
