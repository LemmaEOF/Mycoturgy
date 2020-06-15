package space.bbkr.mycoturgy.block;

import javax.annotation.Nullable;

import space.bbkr.mycoturgy.block.entity.HaustorSequesterBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class HaustorSequesterBlock extends Block implements BlockEntityProvider {
	public HaustorSequesterBlock(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.union(Block.createCuboidShape(2, 0, 2, 14, 1, 14), Block.createCuboidShape(6, 1, 6, 8, 2, 8));
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new HaustorSequesterBlockEntity();
	}
}
