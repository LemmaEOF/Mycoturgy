package gay.lemmaeof.mycoturgy.block.entity;

import gay.lemmaeof.mycoturgy.init.MycoturgyBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class InfestedSpawnerBlockEntity extends BlockEntity {
	private final MobSpawnerLogic logic = new MobSpawnerLogic() {
		@Override
		public void sendStatus(World world, BlockPos pos, int i) {
			world.addSyncedBlockEvent(pos, MycoturgyBlocks.INFESTED_MOB_SPAWNER, i, 0);
		}

		public void setSpawnEntry(@Nullable World world, BlockPos pos, MobSpawnerEntry spawnEntry) {
			super.setSpawnEntry(world, pos, spawnEntry);
			if (world != null) {
				BlockState blockState = world.getBlockState(pos);
				world.updateListeners(pos, blockState, blockState, 4);
			}

		}
	};

	public InfestedSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(null, blockPos, blockState); //FIXME REGISTER A BLOCK ENTITY TYPE!!!!
	}

	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.logic.readNbt(this.world, this.pos, nbt);
	}

	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		this.logic.writeNbt(nbt);
	}

	public static void clientTick(World world, BlockPos pos, BlockState state, InfestedSpawnerBlockEntity blockEntity) {
		blockEntity.logic.clientTick(world, pos);
	}

	public static void serverTick(World world, BlockPos pos, BlockState state, InfestedSpawnerBlockEntity blockEntity) {
		blockEntity.logic.serverTick((ServerWorld)world, pos);
	}

	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}

	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = this.toNbt();
		nbtCompound.remove("SpawnPotentials");
		return nbtCompound;
	}

	public boolean onSyncedBlockEvent(int type, int data) {
		return this.logic.handleStatus(this.world, type) || super.onSyncedBlockEvent(type, data);
	}

	public MobSpawnerLogic getLogic() {
		return this.logic;
	}
}
