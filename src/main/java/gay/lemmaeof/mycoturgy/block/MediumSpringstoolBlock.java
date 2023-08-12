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

public class MediumSpringstoolBlock extends SpringstoolBlock{
	private static final IntProperty X = IntProperty.of("x", 0, 1);
	private static final IntProperty Z = IntProperty.of("z", 0, 1);
	public static final VoxelShape[] SHAPES = VoxelMath.rotationsOf(VoxelShapes.union(
			Block.createCuboidShape(1, 8, 1, 16, 16, 16),
			Block.createCuboidShape(9, 0, 9, 16, 16, 16)
	));

	public MediumSpringstoolBlock( Settings settings) {
		super(1, X, Z, settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(X, Z);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (getX(state) == 0 && getZ(state) == 0) return SHAPES[0];
		else if (getX(state) == 0 && getZ(state) == 1) return SHAPES[2];
		else if (getX(state) == 1 && getZ(state) == 0) return SHAPES[3];
		else return SHAPES[1]; //getX(state) == 1 && getY(state) == 1
	}
}
