package space.bbkr.mycoturge;

import java.util.Collection;

import dev.onyxstudios.cca.api.component.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.component.chunk.StaticChunkComponentInitializer;

import net.minecraft.util.Identifier;

//TODO: use?
public class ComponentInit implements StaticChunkComponentInitializer {
	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {

	}

	@Override
	public Collection<Identifier> getSupportedComponentTypes() {
		return null;
	}
}
