package space.bbkr.mycoturge.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.bbkr.mycoturge.Mycoturge;
import space.bbkr.mycoturge.component.HaustorComponent;

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
		if (this.random.nextInt(1000) < Mycoturge.HAUSTOR_TICK_SPEED /*5 currently*/) {
			ChunkPos pos = chunk.getPos();
			HaustorComponent selfComp = Mycoturge.HAUSTOR_COMPONENT.get(chunk);
			if (selfComp.getHypha() > (getSequesterCount(chunk) * 10) + 1) {
				for (int i = 0; i < 8; i += 2) {
					if (this.isChunkLoaded(pos.x + OFFSETS[i], pos.z + OFFSETS[i + 1])) {
						HaustorComponent otherComp = Mycoturge.HAUSTOR_COMPONENT.get(
								this.getChunk(pos.x + OFFSETS[i], pos.z + OFFSETS[i + 1])
						);
						if (selfComp.getHypha() > otherComp.getHypha() + 1) { //to avoid fluctiations
							selfComp.setHypha(selfComp.getHypha() - 1);
							otherComp.setHypha(otherComp.getHypha() + 1);
						}
					}
				}
			}
		}
		this.getWorld().getProfiler().pop();
	}

	private static int getSequesterCount(WorldChunk chunk) {
		int count = 0;
		for (BlockEntity be : chunk.getBlockEntities().values()) {
			if (be.getType() == Mycoturge.HAUSTOR_SEQUESTER_BLOCK_ENTITY) {
				count++;
			}
		}
		return count;
	}
}
