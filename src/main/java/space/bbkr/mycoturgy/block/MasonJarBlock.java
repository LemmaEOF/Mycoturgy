package space.bbkr.mycoturgy.block;

import java.util.Random;

import javax.annotation.Nullable;

import space.bbkr.mycoturgy.block.entity.MasonJarBlockEntity;
import space.bbkr.mycoturgy.init.MycoturgyItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class MasonJarBlock extends Block implements BlockEntityProvider {
	public static final BooleanProperty FILLED = BooleanProperty.of("filled");
	public static final VoxelShape SHAPE = Block.createCuboidShape(5, 0, 5, 11, 10, 11);

	public MasonJarBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState().with(FILLED, false));
	}

	//TODO: sounds
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (world.isClient) return ActionResult.SUCCESS;
		if (stack.getItem() == Items.POTION && !state.get(FILLED)) {
			Potion pot = PotionUtil.getPotion(stack);
			if (pot == Potions.WATER) {
				world.setBlockState(pos, state.with(FILLED, true), 2);
				if (!player.isCreative()) {
					player.setStackInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
				}
				return ActionResult.SUCCESS;
			}
		} else if (stack.getItem() == Items.GLASS_BOTTLE && state.get(FILLED)) {
			world.setBlockState(pos, state.with(FILLED, false), 2);
			ItemUsage.method_30012(stack, player, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER));
			return ActionResult.SUCCESS;
		} else if (stack.getItem() == MycoturgyItems.SPOREBRUSH_ASH) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof MasonJarBlockEntity) {
				MasonJarBlockEntity jar = (MasonJarBlockEntity)be;
				if (jar.getAshes() <= 12) {
					jar.addAshes();
					if (!player.isCreative()) {
						player.getStackInHand(hand).decrement(1);
					}
					return ActionResult.SUCCESS;
				}
				return ActionResult.PASS;
			}
		} else if (!stack.isEmpty()) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof MasonJarBlockEntity) {
				MasonJarBlockEntity jar = (MasonJarBlockEntity)be;
				if (jar.getInventory().getStack().isEmpty()) {
					ItemStack previous = stack.copy();
					ItemStack toInsert = stack.split(1);
					jar.getInventory().setStack(toInsert);
					if (player.isCreative()) {
						player.setStackInHand(hand, previous);
					}
					return ActionResult.SUCCESS;
				}
				return ActionResult.PASS;
			}
		} else {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof MasonJarBlockEntity) {
				MasonJarBlockEntity jar = (MasonJarBlockEntity)be;
				if (player.isSneaking()) {
					player.sendMessage(new TranslatableText("msg.mycoturgy.ashes", jar.getAshes()), true);
				} else if (!jar.getInventory().getStack().isEmpty()) {
					player.inventory.insertStack(jar.getInventory().getStack());
					jar.getInventory().clear();
					return ActionResult.SUCCESS;
				}
				return ActionResult.PASS;
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void onStacksDropped(BlockState state, World world, BlockPos pos, ItemStack stack) {
		super.onStacksDropped(state, world, pos, stack);
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof MasonJarBlockEntity && !world.isClient) {
			MasonJarBlockEntity jar = (MasonJarBlockEntity)be;
			ItemScatterer.spawn(world, pos, jar.getInventory());
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FILLED);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new MasonJarBlockEntity();
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

	//TODO: custom particles?
	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		Vec3d jarPos = new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5).add(state.getModelOffset(world, pos));
		BlockEntity be = world.getBlockEntity(pos);
		if (state.get(FILLED) && be instanceof MasonJarBlockEntity) {
			MasonJarBlockEntity jar = (MasonJarBlockEntity)be;
			if (jar.getAshes() > 0 && random.nextInt(5) < (jar.getAshes() / 4) + 1) {
				world.addParticle(ParticleTypes.WITCH, jarPos.x + random.nextDouble() * (3/16D) * (double)(random.nextBoolean() ? 1 : -1), jarPos.y + random.nextDouble() * (1/2D), jarPos.z + random.nextDouble() * (3/16D) * (double)(random.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
			}
		}
	}
}
