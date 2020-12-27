package space.bbkr.mycoturgy.spell;

import java.util.Random;

import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.init.MycoturgyBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;

public class GrowMushroomSpell implements Spell {
	@Override
	public boolean canCast(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity caster, HaustorComponent haustor) {
		return state.getBlock() == MycoturgyBlocks.SCATTERED_ASHES && world.getBlockState(pos.down()).getBlock() == Blocks.PODZOL && haustor.getHypha() >= 10;
	}

	@Override
	public void cast(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity caster, HaustorComponent haustor) {
		Random random = new Random();
		world.setBlockState(pos, Blocks.AIR.getDefaultState());
		ConfiguredFeature<?, ?> feature = random.nextBoolean()? ConfiguredFeatures.HUGE_BROWN_MUSHROOM : ConfiguredFeatures.HUGE_RED_MUSHROOM;
		if (feature.generate(world, world.getChunkManager().getChunkGenerator(), random, pos)) {
			world.setBlockState(pos.down(), Blocks.MYCELIUM.getDefaultState());
			for (int i = -2; i <=2; i++) {
				for (int j = -2; j <= 2; j++) {
					BlockPos newPos = pos.down().add(i, 0, j);
					if (Feature.isSoil(world, newPos)) {
						world.setBlockState(newPos, Blocks.MYCELIUM.getDefaultState());
					}
				}
			}
			haustor.spawnLamella(5);
		} else {
			world.setBlockState(pos, state);
		}
	}
}
