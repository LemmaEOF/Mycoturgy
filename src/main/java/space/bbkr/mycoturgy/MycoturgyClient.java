package space.bbkr.mycoturgy;

import space.bbkr.mycoturgy.client.hud.HaustorBandHud;
import space.bbkr.mycoturgy.client.render.CookingPotBlockEntityRenderer;
import space.bbkr.mycoturgy.client.render.MasonJarBlockEntityRenderer;
import space.bbkr.mycoturgy.init.MycoturgyBlocks;
import space.bbkr.mycoturgy.patchouli.JarInfusingPage;
import vazkii.patchouli.client.book.ClientBookRegistry;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

@Environment(EnvType.CLIENT)
public class MycoturgyClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), MycoturgyBlocks.SPOREBRUSH_CROP, MycoturgyBlocks.HAUSTOR_SEQUESTER, MycoturgyBlocks.SCATTERED_ASHES);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), MycoturgyBlocks.MASON_JAR, MycoturgyBlocks.COOKING_POT);
		//TODO: change color if ashes in jar?
		ColorProviderRegistry.BLOCK.register((state, world, pos, index) -> FluidRenderHandlerRegistry.INSTANCE.get(Fluids.WATER).getFluidColor(world, pos, Fluids.WATER.getDefaultState()), MycoturgyBlocks.MASON_JAR, MycoturgyBlocks.COOKING_POT);
		HudRenderCallback.EVENT.register(HaustorBandHud::render);
		ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(Mycoturgy.MODID, "jar_infusing"), JarInfusingPage.class);
		BlockEntityRendererRegistry.INSTANCE.register(MycoturgyBlocks.MASON_JAR_BLOCK_ENTITY, MasonJarBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(MycoturgyBlocks.COOKING_POT_BLOCK_ENTITY, CookingPotBlockEntityRenderer::new);
	}
}
