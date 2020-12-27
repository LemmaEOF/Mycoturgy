package space.bbkr.mycoturgy.spell;

import java.util.ArrayList;
import java.util.List;

import space.bbkr.mycoturgy.component.HaustorComponent;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface Spell {
	//TODO: literally anything else
	public static final List<Spell> SPELLS = new ArrayList<>();

	boolean canCast(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity caster, HaustorComponent haustor);

	void cast(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity caster, HaustorComponent haustor);
}
