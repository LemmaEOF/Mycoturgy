package space.bbkr.mycoturgy.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import space.bbkr.mycoturgy.init.MycoturgyBlocks;

import net.minecraft.block.entity.BlockEntity;

public class HaustorSequesterBlockEntity extends BlockEntity {
	public HaustorSequesterBlockEntity(BlockPos pos, BlockState state) {
		super(MycoturgyBlocks.HAUSTOR_SEQUESTER_BLOCK_ENTITY, pos, state);
	}
	//TODO: do we want to actually store haustor here? Or is it just for bookkeeping?
}
