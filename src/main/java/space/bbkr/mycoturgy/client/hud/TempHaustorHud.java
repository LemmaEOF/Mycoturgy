package space.bbkr.mycoturgy.client.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.component.HaustorComponent;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

//TODO: *really* gotta make a lib for this, so that this doesn't overlap with CRPG or Trion
@Environment(EnvType.CLIENT)
public class TempHaustorHud {
	private static MinecraftClient client = MinecraftClient.getInstance();

	private static final Identifier BAR_TEX = new Identifier(Mycoturgy.MODID, "textures/gui/bars.png");
	private static final Identifier ICON_TEX = new Identifier(Mycoturgy.MODID, "textures/icons/trion.png");

	public static void render(MatrixStack matrices, float tickDelta) {
//		if (client.player.getStackInHand(Hand.MAIN_HAND)) //TODO: only display with specific item in hand
		matrices.push();
		HaustorComponent component = Mycoturgy.HAUSTOR_COMPONENT.get(client.player.world.getChunk(client.player.getBlockPos()));
		//TODO: real colors - these are just the trion ones right now
		drawBar(matrices, 0x5FEC94, (float)component.getPrimordia(), 1024f, 4, 28);
		drawBar(matrices, 0x5FD3EC, (float)component.getHypha(), 512f, 4, 40);
		drawBar(matrices, 0xEC5F6B, (float)component.getLamella(), 256f, 4, 52);
		matrices.pop();
	}

	private static void drawBar(MatrixStack matrices, int color, float amount, float max, int left, int top) {
		//TODO: icons? Is this gonna be more permanent?
		//draw icon
		client.getTextureManager().bindTexture(ICON_TEX);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
//		RenderSystem.enableAlphaTest();
		//TODO: how does color work now?
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		blit(left, top, 9, 9);

		left += 10;

		//draw bar
		float r = (color >> 16 & 255) / 255f;
		float g = (color >> 8 & 255) / 255f;
		float b = (color & 255) / 255f;
		client.getTextureManager().bindTexture(BAR_TEX);
		//bar BG: left edge, middle, right edge
		blit(left, top, 1, 9, texUV(0), texUV(20), texUV(1), texUV(29));
		blit(left + 1, top, 62, 9, texUV(1), texUV(20), texUV(63), texUV(29));
		blit(left + 63, top, 1, 9, texUV(63), texUV(20), texUV(64), texUV(29));

		RenderSystem.color4f(r, g, b, 1f);
		int fgLength = (int)((amount / max) * 62f);
		//bar FG: left edge, middle, right edge
		blit(left, top, 1, 9, texUV(0), texUV(29), texUV(1), texUV(38));
		blit(left + 1, top, fgLength, 9, texUV(1), texUV(29), texUV(fgLength + 1), texUV(38));
		blit(left + fgLength + 1, top, 1, 9, texUV(63), texUV(29), texUV(64), texUV(38));

		RenderSystem.color4f(1f, 1f, 1f, 1f);

		MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "" + (int)amount, left + 66, top, 0xFFFFFF);
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
