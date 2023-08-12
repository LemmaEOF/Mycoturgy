package gay.lemmaeof.mycoturgy.block;

import gay.lemmaeof.mycoturgy.util.VoxelMath;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class LargeSpringstoolBlock extends SpringstoolBlock {
	private static final IntProperty X = IntProperty.of("x", 0, 2);
	private static final IntProperty Z = IntProperty.of("z", 0, 2);
	private static final VoxelShape[] CORNER_SHAPES = VoxelMath.rotationsOf(VoxelShapes.union(
			Block.createCuboidShape(1, 8, 1, 16, 16, 16),
			Block.createCuboidShape(12, 0, 12, 16, 8, 16)
	));
	private static final VoxelShape[] SIDE_SHAPES = VoxelMath.rotationsOf(VoxelShapes.union(
			Block.createCuboidShape(0, 8, 1, 16, 16, 16),
			Block.createCuboidShape(0, 0, 12, 16, 8, 16)
	));

	public LargeSpringstoolBlock( Settings settings) {
		super(1.25, X, Z, settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(X, Z);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (getX(state) == 0 && getZ(state) == 0) return CORNER_SHAPES[0]; //northwest
		else if (getX(state) == 0 && getZ(state) == 1) return SIDE_SHAPES[2]; //west
		else if (getX(state) == 0 && getZ(state) == 2) return CORNER_SHAPES[2]; //southwest
		else if (getX(state) == 1 && getZ(state) == 0) return SIDE_SHAPES[0]; //north
		else if (getX(state) == 1 && getZ(state) == 1) return VoxelShapes.fullCube(); //center
		else if (getX(state) == 1 && getZ(state) == 2) return SIDE_SHAPES[1]; //south
		else if (getX(state) == 2 && getZ(state) == 0) return CORNER_SHAPES[3]; //northeast
		else if (getX(state) == 2 && getZ(state) == 1) return SIDE_SHAPES[3]; //east
		else return CORNER_SHAPES[1]; //getX(state) == 2 && getY(state) == 2 //southeast
	}
}
