package gay.lemmaeof.mycoturgy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class SmallSpringstoolBlock extends SpringstoolBlock {
	private static final VoxelShape SHAPE = VoxelShapes.union(
			Block.createCuboidShape(1, 4, 1, 15, 12, 15),
			Block.createCuboidShape(4, 0, 4, 12, 4, 12)
	);

	public SmallSpringstoolBlock(Settings settings) {
		super(0.75, null, null, settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
}
