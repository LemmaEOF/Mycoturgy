package space.bbkr.mycoturgy.inventory;

import space.bbkr.mycoturgy.block.entity.CookingPotBlockEntity;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.util.math.BlockPos;

public class CookingPotInventory extends SimpleInventory {
	private CookingPotBlockEntity parent;

	public CookingPotInventory(CookingPotBlockEntity parent) {
		super(7);
		this.parent = parent;
	}

	public BlockPos getPos() {
		return parent.getPos();
	}

	@Override
	public void markDirty() {
		super.markDirty();
		parent.markDirty();
		if (parent.getWorld() != null && !parent.getWorld().isClient) parent.sync();
	}
}
