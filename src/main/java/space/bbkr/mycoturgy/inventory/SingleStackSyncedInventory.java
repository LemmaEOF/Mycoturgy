package space.bbkr.mycoturgy.inventory;

import space.bbkr.mycoturgy.block.entity.SyncingBlockEntity;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

//TODO: yes I know this is a mojang-y name, fuck you, give me a better one
public class SingleStackSyncedInventory extends SimpleInventory {
	private final SyncingBlockEntity parent;

	public SingleStackSyncedInventory(SyncingBlockEntity parent) {
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
