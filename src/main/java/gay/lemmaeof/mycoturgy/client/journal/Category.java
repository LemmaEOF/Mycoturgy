package gay.lemmaeof.mycoturgy.client.journal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;

public class Category {
	ItemStack icon;
	String key;
	Chapter chapter;
	int color;
	float hoveramount = 0;

	public Category(String name, ItemStack icon, int color, Chapter chapter) {
		this.icon = icon;
		this.key = name;
		this.chapter = chapter;
		this.color = color;
	}

	protected void reset() {
		hoveramount = 0;
	}

	static void colorBlit(MatrixStack mStack, int x, int y, int uOffset, int vOffset, int width, int height, int textureWidth, int textureHeight, int color) {
		Matrix4f matrix = mStack.peek().getModel();
		int maxX = x + width, maxY = y + height;
		float minU = (float)uOffset / textureWidth, minV = (float)vOffset / textureHeight;
		float maxU = minU + (float)width / textureWidth, maxV = minV + (float)height / textureHeight;
		int r = ColorUtil.getRed(color),
				g = ColorUtil.getGreen(color),
				b = ColorUtil.getBlue(color);
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
		bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
		bufferbuilder.vertex(matrix, (float)x, (float)maxY, 0).texture(minU, maxV).color(r, g, b, 255).next();
		bufferbuilder.vertex(matrix, (float)maxX, (float)maxY, 0).texture(maxU, maxV).color(r, g, b, 255).next();
		bufferbuilder.vertex(matrix, (float)maxX, (float)y, 0).texture(maxU, minV).color(r, g, b, 255).next();
		bufferbuilder.vertex(matrix, (float)x, (float)y, 0).texture(minU, minV).color(r, g, b, 255).next();
		bufferbuilder.end();
//		RenderSystem.enableAlphaTest();
		BufferRenderer.draw(bufferbuilder);
	}

	public boolean click(JournalGui gui, int x, int y, boolean right, int mouseX, int mouseY) {
		int w = 36;
		if (!right) x -= 36;
		w += hoveramount * 12;
		if (!right) x -= hoveramount * 12;

		boolean hover = mouseX >= x && mouseY >= y && mouseX <= x + w && mouseY <= y + 19;
		if (hover) {
			gui.currentPage = 0;
			gui.currentChapter = chapter;
			gui.resetPages();
			return true;
		}
		return false;
	}

	public void draw(JournalGui gui, MatrixStack mStack, int x, int y, boolean right, int mouseX, int mouseY) {
		int w = 36;
		if (!right) x -= 36;
		w += hoveramount * 12;
		if (!right) x -= hoveramount * 12;

		boolean hover = mouseX >= x && mouseY >= y && mouseX <= x + w && mouseY <= y + 19;
		if (hover && hoveramount < 1) hoveramount += MinecraftClient.getInstance().getTickDelta() / 4;
		else if (!hover && hoveramount > 0) hoveramount -= MinecraftClient.getInstance().getTickDelta() / 4;
		hoveramount = MathHelper.clamp(hoveramount, 0, 1);

		if (right) {
			x -= 12;
			x += hoveramount * 12;
		}

		MinecraftClient.getInstance().getTextureManager().bindTexture(JournalGui.CODEX_BACKGROUND);
		colorBlit(mStack, x, y, 208, right ? 208 : 227, 48, 19, 512, 512, color);
		MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(icon, x + (right ? 23 : 9), y + 1);
	}

	public void drawTooltip(JournalGui gui, MatrixStack mStack, int x, int y, boolean right, int mouseX, int mouseY) {
		int w = 36;
		if (!right) x -= 36;
		w += hoveramount * 12;
		if (!right) x -= hoveramount * 12;

		boolean hover = mouseX >= x && mouseY >= y && mouseX <= x + w && mouseY <= y + 19;
		if (hover) gui.renderTooltip(mStack, new TranslatableText("mycoturgy.journal.category." + key), mouseX, mouseY);
	}
}
