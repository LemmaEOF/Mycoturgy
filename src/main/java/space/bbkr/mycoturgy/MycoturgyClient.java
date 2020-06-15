package space.bbkr.mycoturgy;

import space.bbkr.mycoturgy.client.hud.TempHaustorHud;
import space.bbkr.mycoturgy.patchouli.PageCampfire;
import vazkii.patchouli.client.book.ClientBookRegistry;

import net.minecraft.client.render.RenderLayer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

@Environment(EnvType.CLIENT)
public class MycoturgyClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), Mycoturgy.SPOREBRUSH_CROP, Mycoturgy.HAUSTOR_SEQUESTER);
		HudRenderCallback.EVENT.register(TempHaustorHud::render);
		ClientBookRegistry.INSTANCE.pageTypes.put("campfire", PageCampfire.class);
	}
}
