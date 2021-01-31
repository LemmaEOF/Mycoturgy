package space.bbkr.mycoturgy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import space.bbkr.mycoturgy.init.MycoturgyBlocks;

import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

@Mixin(FlowableFluid.class)
public class MixinFlowableFluid {
	@Inject(method = "getVelocity", at = @At("RETURN"), cancellable = true)
	private void invertFlowDirection(BlockView world, BlockPos pos, FluidState state, CallbackInfoReturnable<Vec3d> info) {
		for (Direction dir : Direction.values()) {
			if (world.getBlockState(pos.offset(dir)).getBlock() == MycoturgyBlocks.PADDLE_RHIZOME) {
				info.setReturnValue(info.getReturnValue().multiply(-1));
				return;
			}
		}
	}
}
