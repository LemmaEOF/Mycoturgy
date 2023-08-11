package gay.lemmaeof.mycoturgy.spell;

import gay.lemmaeof.mycoturgy.component.HaustorComponent;
import gay.lemmaeof.mycoturgy.init.MycoturgyBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

//TODO: impl
public class SpringstoolSpell implements Spell {
	@Override
	public boolean canCast(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity caster, HaustorComponent haustor) {
		return false;
	}

	@Override
	public void cast(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity caster, HaustorComponent haustor) {
		//TODO: discovery for 2x2 and 3x3 springstools
		world.setBlockState(pos, MycoturgyBlocks.SMALL_SPRINGSTOOL.getDefaultState());
		haustor.spawnLamella(5);
	}
}
