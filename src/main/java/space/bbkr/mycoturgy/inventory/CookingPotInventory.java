package space.bbkr.mycoturgy.inventory;

import space.bbkr.mycoturgy.block.entity.CookingPotBlockEntity;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class CookingPotInventory extends SimpleInventory {
	private final CookingPotBlockEntity parent;

	public CookingPotInventory(CookingPotBlockEntity parent) {
		super(6);
		this.parent = parent;
	}

	@Override
	public ItemStack addStack(ItemStack stack) {
		this.addToNewSlot(stack);
		return stack.isEmpty() ? ItemStack.EMPTY : stack;
	}

	private void addToNewSlot(ItemStack stack) {
		for(int i = 0; i < this.size(); ++i) {
			ItemStack itemStack = this.getStack(i);
			if (itemStack.isEmpty()) {
				this.setStack(i, stack.copy());
				stack.setCount(0);
				return;
			}
		}
	}

	public BlockPos getPos() {
		return parent.getPos();
	}

	@Override
	public void markDirty() {
		super.markDirty();
		parent.markDirty();
		if (parent.getWorld() != null && !parent.getWorld().isClient) parent.markDirty();
	}
}
