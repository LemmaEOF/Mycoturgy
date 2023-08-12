package gay.lemmaeof.mycoturgy.block;

import com.unascribed.lib39.weld.api.BigBlock;
import gay.lemmaeof.mycoturgy.init.MycoturgySounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class SpringstoolBlock extends BigBlock {
	private final double bounceHeight;

	public SpringstoolBlock(double bounceHeight, IntProperty x, IntProperty z, Settings settings) {
		super(x, null, z, settings);
		this.bounceHeight = bounceHeight;
	}

	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (entity.bypassesLandingEffects()) {
			super.onLandedUpon(world, state, pos, entity, fallDistance);
		} else {
			this.bounce(entity);
		}
	}

	@Override
	public void onEntityLand(BlockView world, Entity entity) {
		//no-op - we do our velocity change in onLandedUpon because this happens *any* time an entity touches
	}

	private void bounce(Entity entity) {
		Vec3d vec3d = entity.getVelocity();
		if (vec3d.y < 0.0D) {
			double d = entity instanceof LivingEntity ? 1.0D : 0.8D;
			entity.setVelocity(vec3d.x, bounceHeight * d, vec3d.z);
			entity.playSound(MycoturgySounds.SPRINGSTOOL_BOUNCE, 1.0F, (float)(1.375F - bounceHeight/2 - (entity.getWorld().random.nextFloat() * 0.2F - 0.1F)));
		}
	}
}
