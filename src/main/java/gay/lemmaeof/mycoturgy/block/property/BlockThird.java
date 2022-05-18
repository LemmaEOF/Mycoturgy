package gay.lemmaeof.mycoturgy.block.property;

import net.minecraft.util.StringIdentifiable;

public enum BlockThird implements StringIdentifiable {
	UPPER("upper"),
	MIDDLE("middle"),
	LOWER("lower");

	private final String id;

	private BlockThird(String id) {
		this.id = id;
	}

	@Override
	public String asString() {
		return id;
	}
}
