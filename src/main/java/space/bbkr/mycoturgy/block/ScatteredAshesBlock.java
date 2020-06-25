package space.bbkr.mycoturgy.block;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dev.emi.trinkets.api.TrinketsApi;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.init.MycoturgyItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ScatteredAshesBlock extends Block {
	public static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2D, 16.0D);
	public static final Map<Block, Spell> SPELLS = new HashMap<>();

	public ScatteredAshesBlock(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) return ActionResult.SUCCESS;
		ItemStack stack = player.getStackInHand(hand);
		if (TrinketsApi.getTrinketComponent(player).getStack("hand:ring").getItem() == MycoturgyItems.HAUSTORAL_BAND && stack.isEmpty()) {
			BlockState down = world.getBlockState(pos.down());
			if (SPELLS.containsKey(down.getBlock())) {
				Spell spell = SPELLS.get(down.getBlock());
				if (spell.perform(down, (ServerWorld)world, pos.down(), player, Mycoturgy.HAUSTOR_COMPONENT.get(world.getChunk(pos)))) {
					if (world.getBlockState(pos).equals(state)) world.breakBlock(pos, false);
					return ActionResult.SUCCESS;
				}
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	public interface Spell {
		boolean perform(BlockState state, ServerWorld world, BlockPos pos, PlayerEntity caster, HaustorComponent component);
	}

	static {
		SPELLS.put(Blocks.MYCELIUM, (state, world, pos, caster, component) -> {
			Random random = new Random();
			if (component.getHypha() >= 10) {
				component.spawnLamella(2);
				BlockPos upPos = pos.up();
				world.setBlockState(upPos, random.nextBoolean()? Blocks.RED_MUSHROOM.getDefaultState() : Blocks.BROWN_MUSHROOM.getDefaultState());
				BlockState mushState = world.getBlockState(upPos);
				if (((MushroomPlantBlock)state.getBlock()).trySpawningBigMushroom(world, upPos, mushState, random)) {
					component.spawnLamella(3);
					return true;
				}
			}
			return false;
		});
		//TODO: more simple spells
	}
}
