package space.bbkr.mycoturgy.inventory;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import space.bbkr.mycoturgy.init.MycoturgyBlocks;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

public class SporePouchInventory extends SimpleInventory {
	public SporePouchInventory() {
		super(9);
	}

	public void saveToItem(ItemStack stack) {
		NbtCompound tag = stack.getOrCreateNbt();
		tag.put("Items", this.toNbtList());
	}

	public void readFromItem(ItemStack stack) {
		this.clear();
		if (stack.getOrCreateNbt().contains("Items", NbtElement.LIST_TYPE)) {
			this.readNbtList(stack.getNbt().getList("Items", NbtElement.COMPOUND_TYPE));
		}
		this.setStack(0, new ItemStack(MycoturgyBlocks.PADDLE_RHIZOME_SPORES));
	}
}
