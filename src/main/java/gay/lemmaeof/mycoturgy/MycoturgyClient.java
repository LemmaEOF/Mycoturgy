package gay.lemmaeof.mycoturgy;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.item.UnclampedModelPredicateProvider;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import gay.lemmaeof.mycoturgy.client.hud.HaustorBandHud;
import gay.lemmaeof.mycoturgy.client.render.CookingPotBlockEntityRenderer;
import gay.lemmaeof.mycoturgy.client.render.MasonJarBlockEntityRenderer;
import gay.lemmaeof.mycoturgy.init.MycoturgyBlocks;
import gay.lemmaeof.mycoturgy.init.MycoturgyItems;
import gay.lemmaeof.mycoturgy.patchouli.JarInfusingPage;
import vazkii.patchouli.client.book.ClientBookRegistry;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class MycoturgyClient implements ClientModInitializer {

	private static final UnclampedModelPredicateProvider blockingPredicate = (stack, world, user, seed) -> user != null && user.isUsingItem() && user.getActiveItem() == stack ? 1.0F : 0.0F;

	@Override
	public void onInitializeClient(ModContainer container) {
		BlockRenderLayerMap.put(RenderLayer.getCutout(), MycoturgyBlocks.SPOREBRUSH_CROP, MycoturgyBlocks.HAUSTOR_SEQUESTER, MycoturgyBlocks.SCATTERED_ASHES);
		BlockRenderLayerMap.put(RenderLayer.getTranslucent(), MycoturgyBlocks.MASON_JAR, MycoturgyBlocks.COOKING_POT);
		//TODO: change color if ashes in jar?
		ColorProviderRegistry.BLOCK.register((state, world, pos, index) -> FluidRenderHandlerRegistry.INSTANCE.get(Fluids.WATER).getFluidColor(world, pos, Fluids.WATER.getDefaultState()), MycoturgyBlocks.MASON_JAR, MycoturgyBlocks.COOKING_POT);
		HudRenderCallback.EVENT.register(HaustorBandHud::render);
		ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(Mycoturgy.MODID, "jar_infusing"), JarInfusingPage.class);
		BlockEntityRendererRegistry.register(MycoturgyBlocks.MASON_JAR_BLOCK_ENTITY, MasonJarBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(MycoturgyBlocks.COOKING_POT_BLOCK_ENTITY, CookingPotBlockEntityRenderer::new);
		ModelPredicateProviderRegistry.register(MycoturgyItems.SIDE_SWORD, new Identifier("blocking"), blockingPredicate);
		ModelPredicateProviderRegistry.register(MycoturgyItems.MUSHROOM_SHIELD, new Identifier("blocking"), blockingPredicate);
		ModelPredicateProviderRegistry.register(MycoturgyItems.SPOREBRUSH_PIPE, new Identifier(Mycoturgy.MODID, "filled"), (stack, world, user, seed) -> stack.getOrCreateNbt().getInt("PipeFill") == 0? 0: 1);
	}
}
