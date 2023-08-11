package gay.lemmaeof.mycoturgy.block;

import java.util.stream.Stream;

import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.tag.BlockTags;
import org.jetbrains.annotations.Nullable;
import gay.lemmaeof.mycoturgy.block.entity.CookingPotBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import gay.lemmaeof.mycoturgy.init.MycoturgyBlocks;

public class CookingPotBlock extends Block implements BlockEntityProvider {
	public static final BooleanProperty FILLED = BooleanProperty.of("filled");
	private static final VoxelShape OUTLINE = Stream.of(
			Block.createCuboidShape(3, -7, 3, 13, -6, 13),
			Block.createCuboidShape(2, -6, 3, 3, 0, 13),
			Block.createCuboidShape(13, -6, 3, 14, 0, 13),
			Block.createCuboidShape(3, -6, 2, 13, 0, 3),
			Block.createCuboidShape(3, -6, 13, 13, 0, 14),
			Block.createCuboidShape(3, -6, 3, 13, 5, 13)
	).reduce((a, b) -> VoxelShapes.combineAndSimplify(a, b, BooleanBiFunction.OR)
	).orElseThrow(IllegalStateException::new);

	public CookingPotBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState().with(FILLED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FILLED);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (world.isClient) return ActionResult.SUCCESS;
		if (stack.getItem() == Items.POTION && !state.get(FILLED)) {
			Potion pot = PotionUtil.getPotion(stack);
			if (pot == Potions.WATER) {
				world.setBlockState(pos, state.with(FILLED, true), 2);
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1f, 1f);
				if (!player.isCreative()) {
					player.setStackInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
				}
				return ActionResult.SUCCESS;
			}
		} else if (stack.getItem() == Items.GLASS_BOTTLE && state.get(FILLED)) {
			world.setBlockState(pos, state.with(FILLED, false), 2);
			world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1f, 1f);
			ItemUsage.exchangeStack(stack, player, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER));
			return ActionResult.SUCCESS;
		} else if (!stack.isEmpty()) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof CookingPotBlockEntity) {
				CookingPotBlockEntity jar = (CookingPotBlockEntity)be;
				ItemStack previous = stack.copy();
				ItemStack toInsert = stack.split(1);
				jar.getInventory().addStack(toInsert);
				if (player.isCreative()) {
					player.setStackInHand(hand, previous);
				}
				return ActionResult.SUCCESS;
			}
		} else {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof CookingPotBlockEntity) {
				CookingPotBlockEntity jar = (CookingPotBlockEntity) be;
				if (!jar.getOutputInventory().getStack().isEmpty()) {
					player.getInventory().insertStack(jar.getOutputInventory().getStack());
					jar.getOutputInventory().clear();
					return ActionResult.SUCCESS;
				}
				return ActionResult.PASS;
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isIn(BlockTags.CAMPFIRES);
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return OUTLINE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CookingPotBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type != MycoturgyBlocks.COOKING_POT_BLOCK_ENTITY ? null : (w, p, s, be) -> ((CookingPotBlockEntity) be).tick();
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof CookingPotBlockEntity && !world.isClient) {
				CookingPotBlockEntity pot = (CookingPotBlockEntity)be;
				ItemScatterer.spawn(world, pos, pot.getInventory());
				ItemScatterer.spawn(world, pos, pot.getOutputInventory());
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
}
