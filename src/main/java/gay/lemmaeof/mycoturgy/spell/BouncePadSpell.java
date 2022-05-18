package gay.lemmaeof.mycoturgy.spell;

import gay.lemmaeof.mycoturgy.component.HaustorComponent;
import gay.lemmaeof.mycoturgy.init.MycoturgyBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

//TODO: impl
public class BouncePadSpell implements Spell {
	@Override
	public boolean canCast(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity caster, HaustorComponent haustor) {
		return false;
	}

	@Override
	public void cast(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity caster, HaustorComponent haustor) {
		world.setBlockState(pos, MycoturgyBlocks.TEST_BOUNCE_PAD.getDefaultState());
		haustor.spawnLamella(5);
	}
}
