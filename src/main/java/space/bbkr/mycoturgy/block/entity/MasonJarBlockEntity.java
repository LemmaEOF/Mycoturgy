package space.bbkr.mycoturgy.block.entity;

import space.bbkr.mycoturgy.inventory.MasonJarInventory;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

public class MasonJarBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {
	private MasonJarInventory inv = new MasonJarInventory(this.pos);
	int processTime = 0;

	public MasonJarBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public void tick() {

	}

	@Override
	public void setLocation(World world, BlockPos pos) {
		super.setLocation(world, pos);
		inv.setPos(pos);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		this.processTime = tag.getInt("ProcessTime");
		this.inv.setStack(0, ItemStack.fromTag(tag.getCompound("Stack")));
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		tag.putInt("ProcessTime", processTime);
		tag.put("Stack", inv.getStack(0).toTag(new CompoundTag()));
		return tag;
	}
}
