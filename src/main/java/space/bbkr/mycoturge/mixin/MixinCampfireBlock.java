package space.bbkr.mycoturge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.CampfireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(CampfireBlock.class)
public class MixinCampfireBlock {
	@Inject(method = "spawnSmokeParticle", at = @At("HEAD"))
	private static void injectParticles(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke, CallbackInfo info) {

	}
}
