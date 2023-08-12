package gay.lemmaeof.mycoturgy.block.entity;

import com.unascribed.lib39.waypoint.api.AbstractHaloBlockEntity;
import gay.lemmaeof.mycoturgy.block.SturdyGlowcapBlock;
import gay.lemmaeof.mycoturgy.init.MycoturgyBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class GlowcapBlockEntity extends AbstractHaloBlockEntity {
	public GlowcapBlockEntity(BlockPos pos, BlockState state) {
		super(MycoturgyBlocks.GLOWCAP_BLOCK_ENTITY, pos, state);
	}

	@Override
	public boolean shouldRenderHalo() {
		if (getCachedState().getBlock() instanceof SturdyGlowcapBlock b) {
			return b.getY(getCachedState()) == 2;
		}
		return true;
	}
}
