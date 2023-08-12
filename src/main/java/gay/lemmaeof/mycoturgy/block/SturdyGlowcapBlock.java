package gay.lemmaeof.mycoturgy.block;

import com.unascribed.lib39.waypoint.api.AbstractHaloBlockEntity;
import com.unascribed.lib39.weld.api.BigBlock;
import gay.lemmaeof.mycoturgy.block.entity.GlowcapBlockEntity;
import gay.lemmaeof.mycoturgy.component.HaustorComponent;
import gay.lemmaeof.mycoturgy.init.MycoturgyComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SturdyGlowcapBlock extends BigBlock implements BlockEntityProvider {
	public static final IntProperty Y = IntProperty.of("y", 0, 2);
	private static final VoxelShape LOWER_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 19, 10);
	private static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 19, 10);
	private static final VoxelShape UPPER_SHAPE = VoxelShapes.union(
			Block.createCuboidShape(4, 6, 4, 12, 13, 12),
			Block.createCuboidShape(5, 3, 5, 11, 6, 11),
			Block.createCuboidShape(3, 13, 3, 13, 16, 13),
			Block.createCuboidShape(1, 13, 1, 15, 13, 15)
	);

	public SturdyGlowcapBlock(Settings settings) {
		super(null, Y, null, settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Y);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (getY(state) == 1) return MIDDLE_SHAPE;
		return getY(state) == 0? LOWER_SHAPE : UPPER_SHAPE;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		super.randomDisplayTick(state, world, pos, random);
		if (getY(state) == 2) {
			for (int i = 0; i < 2; i++) {
				double d = (double) pos.getX() + 9/16D + (7/16D - random.nextDouble());
				double e = (double) pos.getY() + 1/2D + (1/4D - random.nextDouble());
				double f = (double) pos.getZ() + 9/16D + (7/16D - random.nextDouble());
				double g = (double) random.nextFloat() * -0.04D;
				world.addParticle(ParticleTypes.END_ROD, d, e, f, 0.0D, g, 0.0D);
			}
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

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new GlowcapBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? AbstractHaloBlockEntity::tick : null;
	}
}
