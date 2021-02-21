package space.bbkr.mycoturgy.client.journal;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public abstract class Page {
	Identifier bg;

	public Page(Identifier background) {
		this.bg = background;
	}

	public void reset() {
		//
	}

	@Environment(EnvType.CLIENT)
	public static void drawItem(JournalGui gui, MatrixStack mStack, ItemStack stack, int x, int y, int mouseX, int mouseY) {
		ItemRenderer ir = MinecraftClient.getInstance().getItemRenderer();
		ir.renderGuiItemIcon(stack, x, y);
		ir.renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, stack, x, y, null);
		if (mouseX >= x && mouseY >= y && mouseX <= x + 16 && mouseY <= y + 16) {
			gui.renderTooltip(mStack, stack, mouseX, mouseY);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void drawText(JournalGui gui, MatrixStack mStack, String text, int x, int y) {
		TextRenderer font = MinecraftClient.getInstance().textRenderer;
		font.draw(mStack, text, x, y - 1, ColorUtil.packColor(128, 255, 255, 255));
		font.draw(mStack, text, x - 1, y, ColorUtil.packColor(128, 219, 212, 184));
		font.draw(mStack, text, x + 1, y, ColorUtil.packColor(128, 219, 212, 184));
		font.draw(mStack, text, x, y + 1, ColorUtil.packColor(128, 191, 179, 138));
		font.draw(mStack, text, x, y, ColorUtil.packColor(255, 79, 59, 47));
	}

	@Environment(EnvType.CLIENT)
	public static void drawWrappingText(JournalGui gui, MatrixStack mStack, String text, int x, int y, int w) {
		TextRenderer font = MinecraftClient.getInstance().textRenderer;
		List<String> lines = new ArrayList<>();
		String[] words = text.split(" ");
		String line = "";
		for (String s : words) {
			if (font.getWidth(line) + font.getWidth(s) > w) {
				lines.add(line);
				line = s + " ";
			}
			else line += s + " ";
		}
		if (!line.isEmpty()) lines.add(line);
		for (int i = 0; i < lines.size(); i ++) {
			drawText(gui, mStack, lines.get(i), x, y + i * (font.fontHeight + 1));
		}
	}

	@Environment(EnvType.CLIENT)
	public void fullRender(JournalGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
		MinecraftClient.getInstance().getTextureManager().bindTexture(bg);
		renderBackground(gui, mStack, x, y, mouseX, mouseY);
		render(gui, mStack, x, y, mouseX, mouseY);
		renderIngredients(gui, mStack, x, y, mouseX, mouseY);
	}

	@Environment(EnvType.CLIENT)
	public void renderBackground(JournalGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
		MinecraftClient.getInstance().getTextureManager().bindTexture(bg);
		gui.drawTexture(mStack, x, y, 0, 0, 128, 160);
	}

	@Environment(EnvType.CLIENT)
	public boolean click(JournalGui gui, int x, int y, int mouseX, int mouseY) {
		return false;
	}

	@Environment(EnvType.CLIENT)
	public void render(JournalGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {}

	@Environment(EnvType.CLIENT)
	public void renderIngredients(JournalGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {}
}