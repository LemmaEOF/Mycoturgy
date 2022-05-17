package space.bbkr.mycoturgy.client.journal;

import space.bbkr.mycoturgy.Mycoturgy;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TextPage extends Page {
	public static final Identifier BACKGROUND = new Identifier(Mycoturgy.MODID, "textures/gui/journal_blank_page.png");
	String text;

	public TextPage(String textKey) {
		super(BACKGROUND);
		this.text = textKey;
	}

	@Override
	public void render(JournalGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
		drawWrappingText(gui, mStack, I18n.translate(text), x + 4, y + 4, 120);
	}
}