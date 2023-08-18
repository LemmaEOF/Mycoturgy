package gay.lemmaeof.mycoturgy.block;

import gay.lemmaeof.mycoturgy.init.MycoturgyItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CustomCropBlock extends CropBlock {
	public static final IntProperty AGE = Properties.AGE_3;

	public CustomCropBlock(Settings settings) {
		super(settings);
	}

	@Override
	public IntProperty getAgeProperty() {
		return AGE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public int getMaxAge() {
		return 3;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		if (world.getBaseLightLevel(pos, 0) >= 9) {
			int age = this.getAge(state);
			if (age < this.getMaxAge()) {
				if (random.nextInt(6) == 0) {
					world.setBlockState(pos, this.withAge(age + 1), 2);
				}
			}
		}
	}

	@Override
	public void grow(World world,  BlockPos pos, BlockState state) {
		if (world.random.nextBoolean()) {
			int newAge = this.getAge(state) + 1;
			int maxAge = this.getMaxAge();
			if (newAge > maxAge) {
				newAge = maxAge;
			}

			world.setBlockState(pos, this.withAge(newAge), 2);
		}
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isIn(BlockTags.MUSHROOM_GROW_BLOCK);
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(MycoturgyItems.GLIMMERING_SPORES);
	}
}
