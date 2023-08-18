package gay.lemmaeof.mycoturgy.patchouli;

import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.init.MycoturgyRecipes;
import gay.lemmaeof.mycoturgy.recipe.PotCookingRecipe;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public class PotCookingPage extends PageDoubleRecipeRegistry<PotCookingRecipe> {
	private static final Identifier POT_COOKING_TEXTURE = new Identifier(Mycoturgy.MODID, "textures/gui/pot_cooking.png");
	private static final IntIntPair[] POSITIONS = new IntIntPair[] {
			IntIntPair.of(22, 4),
			IntIntPair.of(58, 4),
			IntIntPair.of(76, 32),
			IntIntPair.of(58, 60),
			IntIntPair.of(22, 60),
			IntIntPair.of(4, 32)
	};

	public PotCookingPage() {
		super(MycoturgyRecipes.POT_COOKING_RECIPE);
	}

	@Override
	protected void drawRecipe(GuiGraphics graphics, PotCookingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		graphics.drawTexture(POT_COOKING_TEXTURE, recipeX, recipeY, 0F, 0F, 96, 80, 128, 128);
		this.parent.drawCenteredStringNoShadow(graphics, this.getTitle(second).asOrderedText(), 58, recipeY - 10, this.book.headerColor);
		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			this.parent.renderIngredient(graphics, recipeX + POSITIONS[i].firstInt(), recipeY + POSITIONS[i].secondInt(), mouseX, mouseY, recipe.getIngredients().get(i));
		}
		this.parent.renderItemStack(graphics, recipeX + 40, recipeY + 32, mouseX, mouseY, recipe.getResult(DynamicRegistryManager.EMPTY));
		if (recipe.getHyphaCost() > 0) {
			this.parent.drawCenteredStringNoShadow(graphics, "" + recipe.getHyphaCost(), recipeX + 31, recipeY + 40, this.book.textColor);
			if (this.parent.isMouseInRelativeRange(mouseX, mouseY, recipeX + 23, recipeY + 28, 16, 20)) {
				this.parent.setTooltip(Text.translatable("text.mycoturgy.hypha_cost"));
			}
		}
		if (recipe.getLamellaCost() > 0) {
			this.parent.drawCenteredStringNoShadow(graphics, "" + recipe.getLamellaCost(), recipeX + 65, recipeY + 40, this.book.textColor);
			if (this.parent.isMouseInRelativeRange(mouseX, mouseY, recipeX + 54, recipeY + 28, 16, 20)) {
				this.parent.setTooltip(Text.translatable("text.mycoturgy.lamella_cost"));
			}
		}
	}

	@Override
	protected ItemStack getRecipeOutput(World level, PotCookingRecipe recipe) {
		return recipe.getResult(level.getRegistryManager());
	}

	@Override
	protected int getRecipeHeight() {
		return 100;
	}
}
