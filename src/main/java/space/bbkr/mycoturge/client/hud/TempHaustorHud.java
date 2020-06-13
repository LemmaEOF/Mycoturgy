package space.bbkr.mycoturge.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;
import space.bbkr.mycoturge.Mycoturge;
import space.bbkr.mycoturge.component.HaustorComponent;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TempHaustorHud {
	private static MinecraftClient client = MinecraftClient.getInstance();

	private static final Identifier BAR_TEX = new Identifier(Mycoturge.MODID, "textures/gui/bars.png");
	private static final Identifier ICON_TEX = new Identifier(Mycoturge.MODID, "textures/icons/trion.png");

	private static final int normalColor = 0x5FEC94;

	public static void render(MatrixStack matrices, float tickDelta) {
		matrices.push();
		HaustorComponent component = Mycoturge.HAUSTOR_COMPONENT.get(client.player.world.getChunk(client.player.getBlockPos()));
		drawBar(matrices, component);
		matrices.pop();
	}

	private static void drawBar(MatrixStack matrices, HaustorComponent component) {
		//draw icon
		client.getTextureManager().bindTexture(ICON_TEX);
		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();
		int left = 4;
		int top = 16;
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		blit(left, top, 9, 9);

		left += 10;

		//draw bar
		float r = (normalColor >> 16 & 255) / 255f;
		float g = (normalColor >> 8 & 255) / 255f;
		float b = (normalColor & 255) / 255f;
		client.getTextureManager().bindTexture(BAR_TEX);
		//bar BG: left edge, middle, right edge
		blit(left, top, 1, 9, texUV(0), texUV(20), texUV(1), texUV(29));
		blit(left + 1, top, 62, 9, texUV(1), texUV(20), texUV(63), texUV(29));
		blit(left + 63, top, 1, 9, texUV(63), texUV(20), texUV(64), texUV(29));

		RenderSystem.color4f(r, g, b, 1f);
		int fgLength = (int)(((float)component.getDefense() / 20f) * 62f);
		//bar FG: left edge, middle, right edge
		blit(left, top, 1, 9, texUV(0), texUV(29), texUV(1), texUV(38));
		blit(left + 1, top, fgLength, 9, texUV(1), texUV(29), texUV(fgLength + 1), texUV(38));
		blit(left + fgLength + 1, top, 1, 9, texUV(63), texUV(29), texUV(64), texUV(38));

		RenderSystem.color4f(1f, 1f, 1f, 1f);

		MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "" + component.getDefense(), left + 66, top, 0xFFFFFF);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
	}

	private static void blit(int x, int y, int width, int height) {
		blit(x, y, width, height, 0f, 0f, 1f, 1f);
	}

	private static void blit(int x, int y, int width, int height, float u1, float v1, float u2, float v2) {
		innerBlit(x, y, x+width, y+height, 0d, u1, v1, u2, v2);
	}

	private static void innerBlit(double x1, double y1, double x2, double y2, double z, float u1, float v1, float u2, float v2) {
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buffer = tess.getBuffer();
		buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);
		buffer.vertex(x1, y2, z).texture(u1, v2).next();
		buffer.vertex(x2, y2, z).texture(u2, v2).next();
		buffer.vertex(x2, y1, z).texture(u2, v1).next();
		buffer.vertex(x1, y1, z).texture(u1, v1).next();
		tess.draw();
	}

	private static float texUV(int orig) {
		return ((float)orig) / 256f;
	}
}
