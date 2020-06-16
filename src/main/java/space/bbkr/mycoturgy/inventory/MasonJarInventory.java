package space.bbkr.mycoturgy.inventory;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class MasonJarInventory extends SimpleInventory {
	private BlockPos pos;

	public MasonJarInventory(BlockPos pos) {
		super(1);
		this.pos = pos;
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
	}

	public BlockPos getPos() {
		return pos;
	}

	public ItemStack getStack() {
		return getStack(0);
	}

	public void setStack(ItemStack stack) {
		setStack(0, stack);
		markDirty();
	}
}
