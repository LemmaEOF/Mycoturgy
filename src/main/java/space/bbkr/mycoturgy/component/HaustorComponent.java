package space.bbkr.mycoturgy.component;

import java.util.Random;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.util.sync.ChunkSyncedComponent;
import space.bbkr.mycoturgy.Mycoturgy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.chunk.Chunk;

public class HaustorComponent implements ChunkSyncedComponent<HaustorComponent> {
	public static final Random random = new Random();
	private final Chunk chunk;
	//TODO: more types of haustoria?
	private int primordia;
	private int hypha;
	private int lamella;

	public HaustorComponent(Chunk chunk) {
		this.chunk = chunk;
		this.primordia = random.nextInt(512) + 512; //TODO: possible to make seed-based/noise-based at all?
		this.hypha = 0;
		this.lamella = 0;
	}

	public int getPrimordia() {
		return primordia;
	}

	public void setPrimordia(int primordia) {
		if (this.primordia == primordia) return;
		this.primordia = Math.min(primordia, 1024);
		sync();
	}

	public void changePrimordia(int amount) {
		setPrimordia(primordia + amount);
	}

	public int getHypha() {
		return hypha;
	}

	public void setHypha(int hypha) {
		if (this.hypha == hypha) return;
		this.hypha = Math.min(hypha, 512);
		sync();
	}

	public void changeHypha(int amount) {
		setHypha(hypha + amount);
	}

	public int getLamella() {
		return lamella;
	}

	public void setLamella(int lamella) {
		if (this.lamella == lamella) return;
		this.lamella = Math.min(lamella, 256);
		sync();
	}

	public void changeLamella(int amount) {
		setLamella(lamella + amount);
	}

	@Override
	public Chunk getChunk() {
		return chunk;
	}

	@Override
	public ComponentType<HaustorComponent> getComponentType() {
		return Mycoturgy.HAUSTOR_COMPONENT;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		this.primordia = tag.getInt("Primordia");
		this.hypha = tag.getInt("Hypha");
		this.lamella = tag.getInt("Lamella");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("Primordia", primordia);
		tag.putInt("Hypha", hypha);
		tag.putInt("Lamella", lamella);
		return tag;
	}
}
