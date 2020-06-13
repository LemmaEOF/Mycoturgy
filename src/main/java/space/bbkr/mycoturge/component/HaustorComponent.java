package space.bbkr.mycoturge.component;

import java.util.Random;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.util.sync.ChunkSyncedComponent;
import space.bbkr.mycoturge.Mycoturge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

public class HaustorComponent implements ChunkSyncedComponent<HaustorComponent> {
	public static final Random random = new Random();
	private final Chunk chunk;
	//TODO: more types of haustoria?
	private int defense;

	public HaustorComponent(Chunk chunk) {
		this.chunk = chunk;
		defense = random.nextInt(20); //TODO: possible to make seed-based at all?
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
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
		this.defense = tag.getInt("Defense");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("Defense", defense);
		return tag;
	}
}
