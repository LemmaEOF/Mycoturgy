package gay.lemmaeof.mycoturgy.init;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.component.BlanketConHaustorComponent;
import gay.lemmaeof.mycoturgy.component.HaustorComponent;

import net.minecraft.util.Identifier;

public class MycoturgyComponents implements ChunkComponentInitializer {
	public static final ComponentKey<HaustorComponent> HAUSTOR_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(
			new Identifier(Mycoturgy.MODID, "haustor"),
			HaustorComponent.class
	);

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(HAUSTOR_COMPONENT, BlanketConHaustorComponent::new);
	}
}
