package space.bbkr.mycoturgy.client.journal;

import java.util.ArrayList;
import java.util.List;

import space.bbkr.mycoturgy.init.MycoturgyItems;

import net.minecraft.item.ItemStack;

public class JournalChapters {
	static List<Category> categories = new ArrayList<>();

	static Category ENTRIES, GUIDES, NOTES;

	static Chapter SPLASH;

	public static void init() {
		SPLASH = new Chapter("mycoturgy.journal.chapter.splash",
				new TitlePage("mycoturgy.journal.page.splash"),
				new TextPage("mycoturgy.journal.page.splash.text"));

		categories.add(ENTRIES = new Category("entries",
				new ItemStack(MycoturgyItems.GLITTERING_SPORES),
				ColorUtil.packColor(255, 89, 143, 76), SPLASH)
		);
	}
}
