package gay.lemmaeof.mycoturgy.spell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gay.lemmaeof.mycoturgy.component.HaustorComponent;
import gay.lemmaeof.mycoturgy.init.MycoturgyBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PaddleRhizomeSpell implements Spell {
	@Override
	public boolean canCast(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity caster, HaustorComponent haustor) {
		return state.isOf(MycoturgyBlocks.PADDLE_RHIZOME_SPORES) && haustor.getHypha() >= 1;
	}

	@Override
	public void cast(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity caster, HaustorComponent haustor) {
		Set<BlockPos> seen = new HashSet<>();
		List<BlockPos> members = new ArrayList<>();
		List<BlockPos> queue = new ArrayList<>();
		queue.add(pos);

		while (!queue.isEmpty()) {
			//TODO: This only checks Hypha on the source chunk, do we want that?
			if (members.size() > haustor.getHypha() || members.size() >= 64) {
				break;
			}
			BlockPos checkPos = queue.remove(0);
			seen.add(checkPos);
			if (world.getBlockState(checkPos).isOf(MycoturgyBlocks.PADDLE_RHIZOME_SPORES)) {
				for (Direction dir : Direction.values()) {
					BlockPos nextPos = checkPos.offset(dir);
					if (seen.contains(nextPos)) continue;
					seen.add(nextPos);
					queue.add(nextPos);

				}
				members.add(checkPos);
			}
		}

		//TODO: convert Hypha
		for (BlockPos member : members) {
			world.setBlockState(member, MycoturgyBlocks.PADDLE_RHIZOME.getDefaultState());
		}
	}
}
