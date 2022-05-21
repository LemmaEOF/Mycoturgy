package gay.lemmaeof.mycoturgy.mixin;

import gay.lemmaeof.mycoturgy.init.MycoturgyItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;

@Mixin(CrossbowItem.class)
public class MixinCrossbowItem {
	@Inject(method = "getPullTime", at = @At("HEAD"), cancellable = true)
	private static void injectPullTime(ItemStack stack, CallbackInfoReturnable<Integer> info) {
		if (stack.getItem() == MycoturgyItems.SPOREBRUSH_PIPE) {
			info.setReturnValue(200);
//			info.setReturnValue(stack.getOrCreateNbt().getInt("PipeFill"));
		}
	}
}
