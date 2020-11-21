package space.bbkr.mycoturgy.block;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dev.emi.trinkets.api.TrinketsApi;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.init.MycoturgyComponents;
import space.bbkr.mycoturgy.init.MycoturgyItems;

import net.minecraft.advancement.Advancement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;

public class ScatteredAshesBlock extends Block {
	public static final Identifier CAST_SPELL = new Identifier(Mycoturgy.MODID, "research/cast_spell");
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
				if (spell.perform(down, (ServerWorld)world, pos.down(), player, MycoturgyComponents.HAUSTOR_COMPONENT.get(world.getChunk(pos)))) {
					world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1f, 1f);
					if (world.getBlockState(pos).equals(state)) world.breakBlock(pos, false);
					MinecraftServer server = player.world.getServer();
					Advancement advancement = server.getAdvancementLoader().get(CAST_SPELL);
					server.getPlayerManager().getAdvancementTracker((ServerPlayerEntity)player).grantCriterion(advancement, "cast_spell");
					return ActionResult.SUCCESS;
				}
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.down();
		return hasTopRim(world, blockPos) || sideCoversSmallSquare(world, blockPos, Direction.UP);
	}

	public interface Spell {
		boolean perform(BlockState state, ServerWorld world, BlockPos pos, PlayerEntity caster, HaustorComponent component);
	}

	static {
		SPELLS.put(Blocks.PODZOL, (state, world, pos, caster, component) -> {
			Random random = new Random();
			if (component.getHypha() >= 10) {
				BlockPos upPos = pos.up();
				BlockState upState = world.getBlockState(upPos);
				world.setBlockState(upPos, Blocks.AIR.getDefaultState());
				System.out.println(world.getBlockState(upPos));
				ConfiguredFeature<?, ?> feature = random.nextBoolean()? ConfiguredFeatures.HUGE_BROWN_MUSHROOM : ConfiguredFeatures.HUGE_RED_MUSHROOM;
				if (feature.generate(world, world.getChunkManager().getChunkGenerator(), random, upPos)) {
					world.setBlockState(pos, Blocks.MYCELIUM.getDefaultState());
					for (int i = -2; i <=2; i++) {
						for (int j = -2; j <= 2; j++) {
							BlockPos newPos = pos.add(i, 0, j);
							if (Feature.isSoil(world, newPos)) {
								world.setBlockState(newPos, Blocks.MYCELIUM.getDefaultState());
							}
						}
					}
					component.spawnLamella(5);
					return true;
				}
				world.setBlockState(upPos, upState);
			}
			return false;
		});
		//TODO: more simple spells
	}
}
