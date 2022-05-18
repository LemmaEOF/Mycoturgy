package gay.lemmaeof.mycoturgy.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BouncePadBlock extends SlimeBlock {
	private final double bounceHeight;

	public BouncePadBlock(double bounceHeight, Settings settings) {
		super(settings);
		this.bounceHeight = bounceHeight;
	}

	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (entity.bypassesLandingEffects()) {
			super.onLandedUpon(world, state, pos, entity, fallDistance);
		}
	}

	@Override
	public void onEntityLand(BlockView world, Entity entity) {
		if (entity.bypassesLandingEffects()) {
			super.onEntityLand(world, entity);
		} else {
			this.bounce(entity);
		}
	}

	private void bounce(Entity entity) {
		Vec3d vec3d = entity.getVelocity();
		if (vec3d.y < 0.0D) {
			double d = entity instanceof LivingEntity ? 1.0D : 0.8D;
			entity.setVelocity(vec3d.x, bounceHeight * d, vec3d.z);
		}

	}
}
