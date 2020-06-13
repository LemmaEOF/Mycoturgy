package space.bbkr.mycoturge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.CampfireBlockEntity;

@Mixin(CampfireBlockEntity.class)
public class MixinCampfireBlockEntity {
	@Inject(method = "tick", at = @At("TAIL"))
	private void tickMycoturgy(CallbackInfo info) {

	}
}
