package space.bbkr.mycoturgy.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MasonJarBlock extends Block implements BlockEntityProvider {
	public static final BooleanProperty FILLED = BooleanProperty.of("filled");
	public static final VoxelShape SHAPE = Block.createCuboidShape(5, 0, 5, 11, 10, 11);

	public MasonJarBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState().with(FILLED, false));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.isEmpty() || world.isClient) return ActionResult.PASS;
		if (stack.getItem() == Items.POTION && !state.get(FILLED)) {
			Potion pot = PotionUtil.getPotion(stack);
			if (pot == Potions.WATER) {
				world.setBlockState(pos, state.with(FILLED, true), 2);
				if (!player.isCreative()) {
					player.setStackInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
				}
				return ActionResult.SUCCESS;
			}
			//TODO: how will this work with the ashes?
		} else if (stack.getItem() == Items.GLASS_BOTTLE && state.get(FILLED)) {
			world.setBlockState(pos, state.with(FILLED, false), 2);
			ItemUsage.method_30012(stack, player, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER));
			return ActionResult.SUCCESS;
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FILLED);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return null;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Vec3d offset = state.getModelOffset(world, pos);
		return SHAPE.offset(offset.x, offset.y, offset.z);
	}

	@Override
	public OffsetType getOffsetType() {
		return OffsetType.XZ;
	}

}
