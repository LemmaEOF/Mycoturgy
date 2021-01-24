package space.bbkr.mycoturgy.inventory;

import space.bbkr.mycoturgy.block.entity.MasonJarBlockEntity;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class MasonJarInventory extends SimpleInventory {
	private final MasonJarBlockEntity parent;

	public MasonJarInventory(MasonJarBlockEntity parent) {
		super(1);
		this.parent = parent;
	}

	public BlockPos getPos() {
		return parent.getPos();
	}

	public ItemStack getStack() {
		return getStack(0);
	}

	public void setStack(ItemStack stack) {
		setStack(0, stack);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		parent.markDirty();
		if (parent.getWorld() != null && !parent.getWorld().isClient) parent.sync();
	}
}
