package space.bbkr.mycoturgy.client.hud;

import java.util.Optional;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.render.*;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.init.MycoturgyComponents;
import space.bbkr.mycoturgy.init.MycoturgyItems;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

//TODO: *really* gotta make a lib for this, so that this doesn't overlap with CRPG or Trion
public class HaustorBandHud {
	private static MinecraftClient client = MinecraftClient.getInstance();

	private static final Identifier BAR_TEX = new Identifier(Mycoturgy.MODID, "textures/gui/bars.png");
	private static final Identifier PRIMORDIA_TEX = new Identifier(Mycoturgy.MODID, "textures/icons/primordia.png");
	private static final Identifier HYPHA_TEX = new Identifier(Mycoturgy.MODID, "textures/icons/hypha.png");
	private static final Identifier LAMELLA_TEX = new Identifier(Mycoturgy.MODID, "textures/icons/lamella.png");

	public static void render(MatrixStack matrices, float tickDelta) {
		if (TrinketsApi.getTrinketComponent(client.player).orElseThrow().isEquipped(MycoturgyItems.HAUSTORAL_BAND)) {
			matrices.push();
			Optional<HaustorComponent> componentOpt = MycoturgyComponents.HAUSTOR_COMPONENT.maybeGet(client.player.world.getChunk(client.player.getBlockPos()));
			//TODO: real colors - these are just the trion ones right now
			if (componentOpt.isPresent()) {
				HaustorComponent component = componentOpt.get();
				drawBar(matrices, PRIMORDIA_TEX, 0x7ACFA1, (float) component.getPrimordia(), 1024f, 4, 28);
				drawBar(matrices, HYPHA_TEX, 0x4D5BB1, (float) component.getHypha(), 512f, 4, 40);
				drawBar(matrices, LAMELLA_TEX, 0x8B6ECA, (float) component.getLamella(), 256f, 4, 52);
			}
			matrices.pop();
		}
	}

	private static void drawBar(MatrixStack matrices, Identifier icon, int color, float amount, float max, int left, int top) {
		//draw icon
		RenderSystem.enableTexture();
		RenderSystem.setShaderTexture(0, icon);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, GlStateManager.class_4535.ONE, GlStateManager.class_4534.ZERO);
//		RenderSystem.enableAlphaTest();
		//TODO: how does color work now? does this need any change?
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
			MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "" + (int) amount, left + 66, top, 0xFFFFFF);
		}
		RenderSystem.disableBlend();
		RenderSystem.disableTexture();
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
		BufferBuilder buffer = tess.getBuffer();
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		buffer.vertex(x1, y2, z).texture(u1, v2).next();
		buffer.vertex(x2, y2, z).texture(u2, v2).next();
		buffer.vertex(x2, y1, z).texture(u2, v1).next();
		buffer.vertex(x1, y1, z).texture(u1, v1).next();
		buffer.end();
//		tess.draw();
		BufferRenderer.draw(buffer);
	}

	private static float texUV(int orig) {
		return ((float)orig) / 256f;
	}
}
