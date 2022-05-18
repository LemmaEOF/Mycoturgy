package gay.lemmaeof.mycoturgy.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import gay.lemmaeof.mycoturgy.init.MycoturgyBlocks;

import net.minecraft.block.entity.BlockEntity;

public class HaustorSequesterBlockEntity extends BlockEntity {
	public HaustorSequesterBlockEntity(BlockPos pos, BlockState state) {
		super(MycoturgyBlocks.HAUSTOR_SEQUESTER_BLOCK_ENTITY, pos, state);
	}
	//TODO: do we want to actually store haustor here? Or is it just for bookkeeping?
}
