package space.bbkr.mycoturgy.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

public abstract class SyncingBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
	public SyncingBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		this.fromTag(getCachedState(), tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return this.toTag(tag);
	}
}
