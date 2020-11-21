package space.bbkr.mycoturgy.component;

import java.util.Random;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.chunk.Chunk;

public class HaustorComponent implements AutoSyncedComponent {
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
	}

	public void changeHypha(int amount) {
		setHypha(hypha + amount);
	}

	public void spendHypha(int amount) {
		changeHypha(-amount);
		changePrimordia(2*amount);
	}

	public void spawnHypha(int amount) {
		changeHypha(amount);
		changePrimordia(-2*amount);
	}

	public int getLamella() {
		return lamella;
	}

	public void setLamella(int lamella) {
		if (this.lamella == lamella) return;
		this.lamella = Math.min(lamella, 256);
	}

	public void changeLamella(int amount) {
		setLamella(lamella + amount);
	}

	public void spendLamella(int amount) {
		changeLamella(-amount);
		changeHypha(2*amount);
	}

	public void spawnLamella(int amount) {
		changeLamella(amount);
		changeHypha(-2*amount);
	}

	public Chunk getChunk() {
		return chunk;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		this.primordia = tag.getInt("Primordia");
		this.hypha = tag.getInt("Hypha");
		this.lamella = tag.getInt("Lamella");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("Primordia", primordia);
		tag.putInt("Hypha", hypha);
		tag.putInt("Lamella", lamella);
	}
}
