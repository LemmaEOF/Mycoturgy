package gay.lemmaeof.mycoturgy.client.journal;

import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.mixin.client.ClientAdvancementManagerAccessor;

import net.minecraft.advancement.Advancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class IndexPage extends Page {
	public static final Identifier BACKGROUND = new Identifier(Mycoturgy.MODID, "textures/gui/journal_index_page.png");
	IndexEntry[] entries;

	public static class IndexEntry {
		Chapter chapter;
		ItemStack icon;

		public IndexEntry(Chapter chapter, ItemStack icon) {
			this.chapter = chapter;
			this.icon = icon;
		}

		public boolean isUnlocked() {
			return true;
		}
	}

	public static class AdvancementLockedEntry extends IndexEntry {
		Identifier[] facts;
		public AdvancementLockedEntry(Chapter chapter, ItemStack icon, Identifier... advancements) {
			super(chapter, icon);
			this.facts = advancements;
		}

		@Override
		public boolean isUnlocked() {
			for (Identifier fact : facts) {
				ClientAdvancementManager manager = MinecraftClient.getInstance().getNetworkHandler().getAdvancementHandler();
				Advancement adv = manager.getManager().get(fact);
				if (adv == null) throw new IllegalArgumentException("Looked for a nonexistent advancement! aaaaaaaa");
				if (!((ClientAdvancementManagerAccessor) manager).getAdvancementProgresses().get(adv).isDone()) return false;
			}
			return true;
		}
	}

	public IndexPage(IndexEntry... pages) {
		super(BACKGROUND);
		this.entries = pages;
	}

	@Override
	public boolean click(JournalGui gui, int x, int y, int mouseX, int mouseY) {
		for (int i = 0; i < entries.length; i ++) if (entries[i].isUnlocked()) {
			if (mouseX >= x + 2 && mouseX <= x + 124 && mouseY >= y + 8 + i * 20 && mouseY <= y + 26 + i * 20) {
				gui.changeChapter(entries[i].chapter);
				MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.NEUTRAL, 1.0f, 1.0f);
				return true;
			}
		}
		return false;
	}

	@Override
	public void render(JournalGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
		MinecraftClient.getInstance().getTextureManager().bindTexture(BACKGROUND);
		for (int i = 0; i < entries.length; i ++) {
			gui.drawTexture(mStack, x + 1, y + 7 + i * 20, 128, entries[i].isUnlocked() ? 0 : 96, 122, 18);
		}

		for (int i = 0; i < entries.length; i ++) if (entries[i].isUnlocked()) {
			MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(entries[i].icon, x + 2, y + 8 + i * 20);
			drawText(gui, mStack, I18n.translate(entries[i].chapter.titleKey), x + 24, y + 20 + i * 20 - MinecraftClient.getInstance().textRenderer.fontHeight);
		}
	}
}