package gay.lemmaeof.mycoturgy.component;

import net.minecraft.world.chunk.Chunk;

//carved-out component for blanketcon usage/performance
public class BlanketConHaustorComponent extends HaustorComponent {

	public BlanketConHaustorComponent(Chunk chunk) {
		super(chunk);
	}

	@Override
	public int getPrimordia() {
		return 1024;
	}

	@Override
	public void setPrimordia(int primordia) {}

	@Override
	public void changePrimordia(int amount) {}

	@Override
	public int getHypha() {
		return 512;
	}

	@Override
	public void setHypha(int hypha) {}

	@Override
	public void spawnHypha(int amount) {}

	@Override
	public void changeHypha(int amount) {}

	@Override
	public int getLamella() {
		return 256;
	}

	@Override
	public void setLamella(int lamella) {}

	@Override
	public void spawnLamella(int amount) {}
}
