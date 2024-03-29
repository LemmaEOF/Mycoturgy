package gay.lemmaeof.mycoturgy.client.hud;

import java.util.Optional;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.render.*;
import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.component.HaustorComponent;
import gay.lemmaeof.mycoturgy.init.MycoturgyComponents;
import gay.lemmaeof.mycoturgy.init.MycoturgyItems;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

//TODO: *really* gotta make a lib for this, so that this doesn't overlap with CRPG or Trion
public class HaustorBandHud {
	private static final MinecraftClient client = MinecraftClient.getInstance();

	private static final Identifier BAR_TEX = new Identifier(Mycoturgy.MODID, "textures/gui/bars.png");
	private static final Identifier PRIMORDIA_TEX = new Identifier(Mycoturgy.MODID, "textures/icons/primordia.png");
	private static final Identifier HYPHA_TEX = new Identifier(Mycoturgy.MODID, "textures/icons/hypha.png");
	private static final Identifier LAMELLA_TEX = new Identifier(Mycoturgy.MODID, "textures/icons/lamella.png");

	public static void render(GuiGraphics graphics, float tickDelta) {
		if (TrinketsApi.getTrinketComponent(client.player).orElseThrow().isEquipped(stack -> stack.isIn(MycoturgyItems.CASTING_BANDS))) {
			Optional<HaustorComponent> componentOpt = MycoturgyComponents.HAUSTOR_COMPONENT.maybeGet(client.player.clientWorld.getChunk(client.player.getBlockPos()));
			if (componentOpt.isPresent()) {
				HaustorComponent component = componentOpt.get();
				drawBar(graphics, PRIMORDIA_TEX, 0x7ACFA1, (float) component.getPrimordia(), 1024f, 4, 28);
				drawBar(graphics, HYPHA_TEX, 0x4D5BB1, (float) component.getHypha(), 512f, 4, 40);
				drawBar(graphics, LAMELLA_TEX, 0x8B6ECA, (float) component.getLamella(), 256f, 4, 52);
			}
		}
	}

	private static void drawBar(GuiGraphics graphics, Identifier icon, int color, float amount, float max, int left, int top) {
		//draw icon
		RenderSystem.setShaderTexture(0, icon);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//		RenderSystem.enableAlphaTest();
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		blit(left, top, 9, 9);

		left += 10;

		//draw bar
		float r = (color >> 16 & 255) / 255f;
		float g = (color >> 8 & 255) / 255f;
		float b = (color & 255) / 255f;
		RenderSystem.setShaderTexture(0, BAR_TEX);
		//bar BG: left edge, middle, right edge
		blit(left, top, 1, 9, texUV(0), texUV(20), texUV(1), texUV(29));
		blit(left + 1, top, 62, 9, texUV(1), texUV(20), texUV(63), texUV(29));
		blit(left + 63, top, 1, 9, texUV(63), texUV(20), texUV(64), texUV(29));

		RenderSystem.setShaderColor(r, g, b, 1f);
		int fgLength = (int)((amount / max) * 62f);
		//bar FG: left edge, middle, right edge
		blit(left, top, 1, 9, texUV(0), texUV(29), texUV(1), texUV(38));
		blit(left + 1, top, fgLength, 9, texUV(1), texUV(29), texUV(fgLength + 1), texUV(38));
		blit(left + fgLength + 1, top, 1, 9, texUV(63), texUV(29), texUV(64), texUV(38));

		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

		if (client.player.isSneaking()) {
			graphics.drawShadowedText(MinecraftClient.getInstance().textRenderer, "" + (int) amount, left + 66, top, 0xFFFFFF);
		}
		RenderSystem.disableBlend();
//		RenderSystem.disableAlphaTest();
	}

	private static void blit(int x, int y, int width, int height) {
		blit(x, y, width, height, 0f, 0f, 1f, 1f);
	}

	private static void blit(int x, int y, int width, int height, float u1, float v1, float u2, float v2) {
		innerBlit(x, y, x+width, y+height, 0d, u1, v1, u2, v2);
	}

	private static void innerBlit(double x1, double y1, double x2, double y2, double z, float u1, float v1, float u2, float v2) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buffer = tess.getBufferBuilder();
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		buffer.vertex(x1, y2, z).uv(u1, v2).next();
		buffer.vertex(x2, y2, z).uv(u2, v2).next();
		buffer.vertex(x2, y1, z).uv(u2, v1).next();
		buffer.vertex(x1, y1, z).uv(u1, v1).next();
		BufferRenderer.drawWithShader(buffer.end());

	}


	private static float texUV(int orig) {
		return ((float)orig) / 256f;
	}
}
