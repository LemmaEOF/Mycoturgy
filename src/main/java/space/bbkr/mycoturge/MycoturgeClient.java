package space.bbkr.mycoturge;

import space.bbkr.mycoturge.client.hud.TempHaustorHud;

import net.minecraft.client.render.RenderLayer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

@Environment(EnvType.CLIENT)
public class MycoturgeClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), Mycoturge.SPOREBRUSH_CROP);
		HudRenderCallback.EVENT.register(TempHaustorHud::render);
	}
}
