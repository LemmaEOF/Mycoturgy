package space.bbkr.mycoturgy.client.journal;

import space.bbkr.mycoturgy.Mycoturgy;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TitlePage extends Page {
	public static final Identifier BACKGROUND = new Identifier(Mycoturgy.MODID, "textures/gui/journal_title_page.png");
	String text, title;

	public TitlePage(String textKey) {
		super(BACKGROUND);
		this.text = textKey;
		this.title = textKey + ".title";
	}

	@Override
	public void render(JournalGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
		String title = I18n.translate(this.title);
		int titleWidth = MinecraftClient.getInstance().textRenderer.getWidth(title);
		drawText(gui, mStack, title, x + 64 - titleWidth / 2, y + 15 - MinecraftClient.getInstance().textRenderer.fontHeight);
		drawWrappingText(gui, mStack, I18n.translate(text), x + 4, y + 24, 120);
	}
}
