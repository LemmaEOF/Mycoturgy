package gay.lemmaeof.mycoturgy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PaddleRhizomeBlock extends Block {
	public static final BooleanProperty NORTH = Properties.NORTH;
	public static final BooleanProperty SOUTH = Properties.SOUTH;
	public static final BooleanProperty EAST = Properties.EAST;
	public static final BooleanProperty WEST = Properties.WEST;
	public static final BooleanProperty UP = Properties.UP;
	public static final BooleanProperty DOWN = Properties.DOWN;
	private static final Map<Direction, BooleanProperty> DIRECTIONS_TO_PROPS = new HashMap<>();

	public PaddleRhizomeBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = getDefaultState();
		for (Direction dir : Direction.values()) {
			FluidState neighbor = ctx.getWorld().getFluidState(ctx.getBlockPos().offset(dir));
			state = state.with(DIRECTIONS_TO_PROPS.get(dir), !neighbor.isEmpty());
		}
		return state;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		FluidState neighbor = world.getFluidState(pos.offset(direction));
		return state.with(DIRECTIONS_TO_PROPS.get(direction), !neighbor.isEmpty());
	}

	static {
		DIRECTIONS_TO_PROPS.put(Direction.NORTH, NORTH);
		DIRECTIONS_TO_PROPS.put(Direction.SOUTH, SOUTH);
		DIRECTIONS_TO_PROPS.put(Direction.EAST, EAST);
		DIRECTIONS_TO_PROPS.put(Direction.WEST, WEST);
		DIRECTIONS_TO_PROPS.put(Direction.UP, UP);
		DIRECTIONS_TO_PROPS.put(Direction.DOWN, DOWN);
	}
}
