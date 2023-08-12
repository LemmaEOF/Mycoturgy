package gay.lemmaeof.mycoturgy.block;

import com.unascribed.lib39.waypoint.api.AbstractHaloBlockEntity;
import gay.lemmaeof.mycoturgy.block.entity.GlowcapBlockEntity;
import gay.lemmaeof.mycoturgy.component.HaustorComponent;
import gay.lemmaeof.mycoturgy.init.MycoturgyComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class ClingyGlowcapBlock extends Block implements BlockEntityProvider {
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

	public ClingyGlowcapBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(FACING, ctx.getPlayerFacing());
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new GlowcapBlockEntity(pos, state);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		super.randomDisplayTick(state, world, pos, random);
		for (int i = 0; i < 2; i++) {
			double d = (double) pos.getX() + 9/16D + (7/16D - random.nextDouble());
			double e = (double) pos.getY() + 1/2D + (1/4D - random.nextDouble());
			double f = (double) pos.getZ() + 9/16D + (7/16D - random.nextDouble());
			double g = (double) random.nextFloat() * -0.04D;
			world.addParticle(ParticleTypes.END_ROD, d, e, f, 0.0D, g, 0.0D);
		}
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		super.randomTick(state, world, pos, random);
		if (random.nextInt(10) == 0) {
			HaustorComponent component = MycoturgyComponents.HAUSTOR_COMPONENT.get(world.getChunk(pos));
			if (component.getPrimordia() > 2) {
				component.changePrimordia(-2);
				component.changeHypha(1);
			}
		}
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? AbstractHaloBlockEntity::tick : null;
	}
}
