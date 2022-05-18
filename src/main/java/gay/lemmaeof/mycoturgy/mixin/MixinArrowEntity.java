package gay.lemmaeof.mycoturgy.mixin;

import gay.lemmaeof.mycoturgy.init.MycoturgyEffects;
import gay.lemmaeof.mycoturgy.init.MycoturgyItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowEntity.class)
public abstract class MixinArrowEntity {
	@Shadow public abstract void addEffect(StatusEffectInstance effect);

	@Shadow private Potion potion;

	@Inject(method = "initFromStack", at = @At("HEAD"), cancellable = true)
	private void injectGriefArrow(ItemStack stack, CallbackInfo info) {
		if (stack.getItem() == MycoturgyItems.GRIEF_ARROW) {
			this.potion = Potions.EMPTY;
			this.addEffect(new StatusEffectInstance(MycoturgyEffects.GRIEF, 100));
			info.cancel();
		}
	}
}
