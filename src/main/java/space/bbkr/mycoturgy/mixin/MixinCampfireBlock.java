package space.bbkr.mycoturgy.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.init.MycoturgyItems;

import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(CampfireBlock.class)
public class MixinCampfireBlock {
	//TODO: custom particles?
	@Inject(method = "spawnSmokeParticle", at = @At("HEAD"))
	private static void injectParticles(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke, CallbackInfo info) {
		Random random = world.getRandom();
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof CampfireBlockEntity) {
			CampfireBlockEntity campfire = (CampfireBlockEntity)be;
			for (ItemStack stack : campfire.getItemsBeingCooked()) {
				if (stack.getItem() == MycoturgyItems.SPORE_BUNDLE) {
					world.addParticle(ParticleTypes.WITCH, (double)pos.getX() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), (double)pos.getY() + random.nextDouble() + random.nextDouble(), (double)pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
				}
			}
		}
	}
}
