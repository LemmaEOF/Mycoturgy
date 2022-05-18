package gay.lemmaeof.mycoturgy.block;

import gay.lemmaeof.mycoturgy.block.entity.InfestedSpawnerBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class InfestedSpawnerBlock extends BlockWithEntity {
	public InfestedSpawnerBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new InfestedSpawnerBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		//FIXME REGISTER A BLOCK ENTITY TYPE!!!!
		return checkType(type, null, world.isClient ? InfestedSpawnerBlockEntity::clientTick : InfestedSpawnerBlockEntity::serverTick);
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
}
