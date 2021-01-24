package space.bbkr.mycoturgy.block;

import org.jetbrains.annotations.Nullable;
import space.bbkr.mycoturgy.block.entity.CookingPotBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class CookingPotBlock extends Block implements BlockEntityProvider {
	public CookingPotBlock(Settings settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new CookingPotBlockEntity();
	}
}
