package space.bbkr.mycoturgy.client.journal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import space.bbkr.mycoturgy.Mycoturgy;

public class JournalGui extends Screen {
	public static final JournalGui DUMMY = new JournalGui();
	public static final Identifier CODEX_BACKGROUND = new Identifier(Mycoturgy.MODID, "textures/gui/journal_bg.png");
	static int xSize = 312, ySize = 208;

	Chapter currentChapter;
	int currentPage = 0;

	static JournalGui INSTANCE = null;
	public static JournalGui getInstance() {
		for (Category cat : JournalChapters.categories) cat.reset();
		if (INSTANCE != null) return INSTANCE;
		return INSTANCE = new JournalGui();
	}

	protected JournalGui() {
		super(new TranslatableText("gui.mycoturgy.journal.title"));
		currentChapter = JournalChapters.SPLASH;
	}

	protected void resetPages() {
		Page left = currentChapter.get(currentPage), right = currentChapter.get(currentPage + 1);
		if (left != null) left.reset();
		if (right != null) right.reset();
	}

	protected void changeChapter(Chapter next) {
		currentChapter = next;
		currentPage = 0;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack);
		MinecraftClient mc = MinecraftClient.getInstance();
		mc.getTextureManager().bindTexture(CODEX_BACKGROUND);

		this.width = mc.getWindow().getScaledWidth();
		this.height = mc.getWindow().getScaledHeight();
		int guiLeft = (width - xSize) / 2, guiTop = (height - ySize) / 2;
		drawTexture(matrixStack, guiLeft, guiTop, 0, 256, xSize, ySize, 512, 512);

		for (int i = 0; i < JournalChapters.categories.size(); i ++) {
			int y = guiTop + 28 + (i % 8) * 20;
			JournalChapters.categories.get(i).draw(this, matrixStack, guiLeft + (i >= 8 ? 304 : 8), y, i >= 8, mouseX, mouseY);
		}

		mc.getTextureManager().bindTexture(CODEX_BACKGROUND);
		drawTexture(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize, 512, 512);
		Page left = currentChapter.get(currentPage), right = currentChapter.get(currentPage + 1);
		if (left != null) left.fullRender(this, matrixStack, guiLeft + 14, guiTop + 24, mouseX, mouseY);
		if (right != null) right.fullRender(this, matrixStack, guiLeft + 170, guiTop + 24, mouseX, mouseY);

		mc.getTextureManager().bindTexture(CODEX_BACKGROUND);
		if (currentPage > 0) { // left arrow
			int x = 10, y = 169;
			int v = 208;
			if (mouseX >= guiLeft + x && mouseY >= guiTop + y && mouseX <= guiLeft + x + 32 && mouseY <= guiTop + y + 16) v += 18;
			drawTexture(matrixStack, guiLeft + x, guiTop + y, 128, v, 32, 18, 512, 512);
		}
		if (currentPage + 2 < currentChapter.size()) { // right arrow
			int x = 270, y = 169;
			int v = 208;
			if (mouseX >= guiLeft + x && mouseY >= guiTop + y && mouseX <= guiLeft + x + 32 && mouseY <= guiTop + y + 16) v += 18;
			drawTexture(matrixStack, guiLeft + x, guiTop + y, 160, v, 32, 18, 512, 512);
		}

		for (int i = 0; i < JournalChapters.categories.size(); i ++) {
			int y = guiTop + 28 + (i % 8) * 20;
			JournalChapters.categories.get(i).drawTooltip(this, matrixStack, guiLeft + (i >= 8 ? 304 : 8), y, i >= 8, mouseX, mouseY);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			MinecraftClient mc = MinecraftClient.getInstance();
			this.width = mc.getWindow().getScaledWidth();
			this.height = mc.getWindow().getScaledHeight();
			int guiLeft = (width - xSize) / 2, guiTop = (height - ySize) / 2;

			if (currentPage > 0) { // left arrow
				int x = guiLeft + 10, y = guiTop + 169;
				if (mouseX >= x && mouseY >= y && mouseX <= x + 32 && mouseY <= y + 16) {
					currentPage -= 2;
					mc.player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.NEUTRAL, 1.0f, 1.0f);
					resetPages();
					return true;
				}
			}
			if (currentPage + 2 < currentChapter.size()) { // right arrow
				int x = guiLeft + 270, y = guiTop + 169;
				if (mouseX >= x && mouseY >= y && mouseX <= x + 32 && mouseY <= y + 16) {
					currentPage += 2;
					MinecraftClient.getInstance().player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.NEUTRAL, 1.0f, 1.0f);
					resetPages();
					return true;
				}
			}

			for (int i = 0; i < JournalChapters.categories.size(); i ++) {
				int y = guiTop + 28 + (i % 8) * 20;
				if (JournalChapters.categories.get(i).click(this, guiLeft + (i >= 8 ? 304 : 8), y, i >= 8, (int)mouseX, (int)mouseY)) return true;
			}

			Page left = currentChapter.get(currentPage), right = currentChapter.get(currentPage + 1);
			if (left != null) if (left.click(this,guiLeft + 14, guiTop + 24, (int)mouseX, (int)mouseY)) return true;
			if (right != null) if (right.click(this,guiLeft + 170, guiTop + 24, (int)mouseX, (int)mouseY)) return true;

		}
		return false;
	}

	@Override
	public void renderTooltip(MatrixStack mStack, ItemStack stack, int x, int y) {
		if (!stack.isEmpty()) super.renderTooltip(mStack, stack, x, y);
	}
}
