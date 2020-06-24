package space.bbkr.mycoturgy.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.init.MycoturgyBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld extends World {
	private static final int[] OFFSETS = new int[]{ 0, -1, 0, 1, 1, 0, -1, 0 }; //north, south, east, west

	@Shadow public abstract ServerChunkManager getChunkManager();

	@Shadow public abstract void updateNeighbors(BlockPos pos, Block block);

	protected MixinServerWorld(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l) {
		super(mutableWorldProperties, registryKey, registryKey2, dimensionType, profiler, bl, bl2, l);
	}

	//TODO: is it possible to have this *not* be a cellular automoton, or otherwise optimize this further?
	@Inject(method = "tickChunk", at = @At("TAIL"))
	private void tickHaustor(WorldChunk chunk, int randomTickSpeed, CallbackInfo info) {
		this.getWorld().getProfiler().push("haustor");
		if (this.random.nextInt(1000) < Mycoturgy.HAUSTOR_TICK_SPEED) {
			int sequesters = getSequesterCount(chunk);
			ChunkPos pos = chunk.getPos();
			HaustorComponent selfComp = Mycoturgy.HAUSTOR_COMPONENT.get(chunk);
			if (selfComp.getHypha() > (sequesters * 10) + 1) {
				if (this.random.nextInt(6) == 0) {
					selfComp.changeHypha(-1);
					selfComp.changePrimordia(2);
				}
				for (int i = 0; i < 8; i += 2) {
					if (this.isChunkLoaded(pos.x + OFFSETS[i], pos.z + OFFSETS[i + 1])) {
						HaustorComponent otherComp = Mycoturgy.HAUSTOR_COMPONENT.get(
								this.getChunk(pos.x + OFFSETS[i], pos.z + OFFSETS[i + 1])
						);
						if (selfComp.getHypha() > otherComp.getHypha() + 1) { //to avoid fluctuations
							selfComp.changeHypha(-1);
							otherComp.changeHypha(1);
						}
					}
				}
			}
			if (selfComp.getLamella() > (sequesters * 5) + 1 && this.random.nextInt(18) == 0) {
				selfComp.changeLamella(-1);
				selfComp.changeHypha(2);
			}
		}
		this.getWorld().getProfiler().pop();
	}

	private static int getSequesterCount(WorldChunk chunk) {
		int count = 0;
		for (BlockEntity be : chunk.getBlockEntities().values()) {
			if (be.getType() == MycoturgyBlocks.HAUSTOR_SEQUESTER_BLOCK_ENTITY) {
				count++;
			}
		}
		return count;
	}
}
