package gay.lemmaeof.mycoturgy.spell;

import java.util.ArrayList;
import java.util.List;

import gay.lemmaeof.mycoturgy.component.HaustorComponent;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

//TODO: rethink, probably
public interface Spell {
	//TODO: literally anything else
	public static final List<Spell> SPELLS = new ArrayList<>();

	boolean canCast(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity caster, HaustorComponent haustor);

	void cast(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity caster, HaustorComponent haustor);
}
