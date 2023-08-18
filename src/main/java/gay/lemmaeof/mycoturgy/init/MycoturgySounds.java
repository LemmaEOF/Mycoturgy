package gay.lemmaeof.mycoturgy.init;

import gay.lemmaeof.mycoturgy.Mycoturgy;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class MycoturgySounds {
	public static final SoundEvent SPRINGSTOOL_BOUNCE = register("springstool_bounce");
	public static final SoundEvent GLOWCAP_HUM = register("glowcap_hum");

	public static void init() {}

	private static SoundEvent register(String name) {
		Identifier id = new Identifier(Mycoturgy.MODID, name);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
	}
}
