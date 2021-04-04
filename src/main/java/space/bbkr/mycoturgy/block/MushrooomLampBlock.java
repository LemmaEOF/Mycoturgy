package space.bbkr.mycoturgy.block;

import java.util.Random;

import org.jetbrains.annotations.Nullable;
import space.bbkr.mycoturgy.block.property.BlockThird;
import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.init.MycoturgyComponents;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class MushrooomLampBlock extends Block {
	public static final EnumProperty<BlockThird> THIRD = EnumProperty.of("third", BlockThird.class);
	public static final VoxelShape LOWER_SHAPE;
	public static final VoxelShape MIDDLE_SHAPE;
	public static final VoxelShape UPPER_SHAPE;

	public MushrooomLampBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(THIRD, BlockThird.LOWER));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(THIRD);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (state.get(THIRD) == BlockThird.MIDDLE) return MIDDLE_SHAPE;
		return state.get(THIRD) == BlockThird.LOWER? LOWER_SHAPE : UPPER_SHAPE;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		BlockThird third = state.get(THIRD);
		if (direction.getAxis() == Direction.Axis.Y) {
			if (third == BlockThird.LOWER && direction == Direction.UP) {
				return newState.isOf(this) && newState.get(THIRD) != third ? state : Blocks.AIR.getDefaultState();
			} else if (third == BlockThird.MIDDLE) {
				return newState.isOf(this) && newState.get(THIRD) != third ? state : Blocks.AIR.getDefaultState();
			} else if (third == BlockThird.UPPER && direction == Direction.DOWN) {
				return newState.isOf(this) && newState.get(THIRD) != third ? state : Blocks.AIR.getDefaultState();
			}
		}
		return third == BlockThird.LOWER
				&& direction == Direction.DOWN
				&& !state.canPlaceAt(world, pos) ?
				Blocks.AIR.getDefaultState()
				: super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient && player.isCreative()) {
			BlockThird third = state.get(THIRD);
			if (third == BlockThird.MIDDLE) {
				BlockPos upPos = pos.up();
				BlockState upState = world.getBlockState(upPos);
				if (upState.getBlock() == state.getBlock() && upState.get(THIRD) == BlockThird.UPPER) {
					world.setBlockState(upPos, Blocks.AIR.getDefaultState(), 35);
					world.syncWorldEvent(player, 2001, upPos, Block.getRawIdFromState(upState));
				}
				BlockPos downPos = pos.down();
				BlockState downState = world.getBlockState(downPos);
				if (downState.getBlock() == state.getBlock() && downState.get(THIRD) == BlockThird.LOWER) {
					world.setBlockState(downPos, Blocks.AIR.getDefaultState(), 35);
					world.syncWorldEvent(player, 2001, downPos, Block.getRawIdFromState(downState));
				}
			} else if (third == BlockThird.UPPER) {
				BlockPos downPos = pos.down();
				BlockState downState = world.getBlockState(downPos);
				if (downState.getBlock() == state.getBlock() && downState.get(THIRD) == BlockThird.MIDDLE) {
					world.setBlockState(downPos, Blocks.AIR.getDefaultState(), 35);
					world.syncWorldEvent(player, 2001, downPos, Block.getRawIdFromState(downState));
				}
				BlockPos downerPos = pos.down().down();
				BlockState downerState = world.getBlockState(downerPos);
				if (downerState.getBlock() == state.getBlock() && downerState.get(THIRD) == BlockThird.LOWER) {
					world.setBlockState(downerPos, Blocks.AIR.getDefaultState(), 35);
					world.syncWorldEvent(player, 2001, downerPos, Block.getRawIdFromState(downerState));
				}
			}
		}

		super.onBreak(world, pos, state, player);
	}

	@Override
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos blockPos = ctx.getBlockPos();
		if (blockPos.getY() < 255 && ctx.getWorld().getBlockState(blockPos.up()).canReplace(ctx)) {
			return this.getDefaultState().with(THIRD, BlockThird.LOWER);
		} else {
			return null;
		}
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		world.setBlockState(pos.up(), state.with(THIRD, BlockThird.MIDDLE), 3);
		world.setBlockState(pos.up().up(), state.with(THIRD, BlockThird.UPPER), 3);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (state.get(THIRD) == BlockThird.UPPER) {
			for (int i = 0; i < 2; i++) {
				double d = (double) pos.getX() + 9/16D + (7/16D - random.nextDouble());
				double e = (double) pos.getY() + 1/2D + (1/4D - random.nextDouble());
				double f = (double) pos.getZ() + 9/16D + (7/16D - random.nextDouble());
				double g = (double) random.nextFloat() * -0.04D;
				world.addParticle(ParticleTypes.END_ROD, d, e, f, 0.0D, g, 0.0D);
			}
		}
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.randomTick(state, world, pos, random);
		if (random.nextInt(10) == 0) {
			HaustorComponent component = MycoturgyComponents.HAUSTOR_COMPONENT.get(world.getChunk(pos));
			if (component.getPrimordia() > 2) {
				//TODO: different effect four soul campfire?
				component.changePrimordia(-2);
				component.changeHypha(1);
			}
		}
	}
	
	static {
		LOWER_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 19, 10);
		MIDDLE_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 19, 10);
		UPPER_SHAPE = VoxelShapes.union(
				Block.createCuboidShape(4, 6, 4, 12, 13, 12),
				Block.createCuboidShape(5, 3, 5, 11, 6, 11),
				Block.createCuboidShape(3, 13, 3, 13, 16, 13),
				Block.createCuboidShape(1, 13, 1, 15, 13, 15)
		);
	}
}
