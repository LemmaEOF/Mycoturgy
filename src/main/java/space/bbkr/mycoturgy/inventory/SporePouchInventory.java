package space.bbkr.mycoturgy.inventory;

import space.bbkr.mycoturgy.init.MycoturgyBlocks;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import net.fabricmc.fabric.api.util.NbtType;

public class SporePouchInventory extends SimpleInventory {
	public SporePouchInventory() {
		super(9);
	}

	public void saveToItem(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.put("Items", this.getTags());
	}

	public void readFromItem(ItemStack stack) {
		this.clear();
		if (stack.getOrCreateTag().contains("Items", NbtType.LIST)) {
			this.readTags(stack.getTag().getList("Items", NbtType.COMPOUND));
		}
		this.setStack(0, new ItemStack(MycoturgyBlocks.PADDLE_RHIZOME_SPORES));
	}
}
