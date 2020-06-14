package space.bbkr.mycoturge.component;

import java.util.Random;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.util.sync.ChunkSyncedComponent;
import space.bbkr.mycoturge.Mycoturge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.chunk.Chunk;

public class HaustorComponent implements ChunkSyncedComponent<HaustorComponent> {
	public static final Random random = new Random();
	private final Chunk chunk;
	//TODO: more types of haustoria?
	private int hypha;

	public HaustorComponent(Chunk chunk) {
		this.chunk = chunk;
		hypha = random.nextInt(64); //TODO: possible to make seed-based at all?
	}

	public int getHypha() {
		return hypha;
	}

	public void setHypha(int hypha) {
		this.hypha = Math.min(hypha, 512);
		sync();
	}

	@Override
	public Chunk getChunk() {
		return chunk;
	}

	@Override
	public ComponentType<HaustorComponent> getComponentType() {
		return Mycoturge.HAUSTOR_COMPONENT;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		this.hypha = tag.getInt("Hypha");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("Hypha", hypha);
		return tag;
	}
}
