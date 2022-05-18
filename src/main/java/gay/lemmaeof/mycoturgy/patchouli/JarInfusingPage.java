package gay.lemmaeof.mycoturgy.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.init.MycoturgyRecipes;
import gay.lemmaeof.mycoturgy.recipe.JarInfusingRecipe;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class JarInfusingPage extends PageDoubleRecipeRegistry<JarInfusingRecipe> {
	private static final Identifier CUSTOM_CRAFTING_TEXTURE = new Identifier(Mycoturgy.MODID, "textures/gui/custom_crafting.png");

	public JarInfusingPage() {
		super(MycoturgyRecipes.MASON_JAR_RECIPE);
	}


	@Override
	protected void drawRecipe(MatrixStack ms, JarInfusingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		this.mc.getTextureManager().bindTexture(CUSTOM_CRAFTING_TEXTURE);
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(ms, recipeX, recipeY, 11.0F, 71.0F, 96, 24, 128, 128);
		this.parent.drawCenteredStringNoShadow(ms, this.getTitle(second).asOrderedText(), 58, recipeY - 10, this.book.headerColor);
		this.parent.renderIngredient(ms, recipeX + 4, recipeY + 4, mouseX, mouseY, recipe.getIngredients().get(0));
		this.parent.renderItemStack(ms, recipeX + 76, recipeY + 4, mouseX, mouseY, recipe.getOutput());
		if (recipe.getHyphaCost() > 0) {
			this.parent.drawCenteredStringNoShadow(ms, "" + recipe.getHyphaCost(), recipeX + 31, recipeY + 12, this.book.textColor);
			if (this.parent.isMouseInRelativeRange(mouseX, mouseY, recipeX + 23, recipeY, 16, 20)) {
				this.parent.setTooltip(new TranslatableText("text.mycoturgy.hypha_cost"));
			}
		}
		if (recipe.getLamellaCost() > 0) {
			this.parent.drawCenteredStringNoShadow(ms, "" + recipe.getLamellaCost(), recipeX + 65, recipeY + 12, this.book.textColor);
			if (this.parent.isMouseInRelativeRange(mouseX, mouseY, recipeX + 54, recipeY, 16, 20)) {
				this.parent.setTooltip(new TranslatableText("text.mycoturgy.lamella_cost"));
			}
		}
	}

	@Override
	protected ItemStack getRecipeOutput(JarInfusingRecipe recipe) {
		return recipe == null ? ItemStack.EMPTY : recipe.getOutput();
	}

	@Override
	protected int getRecipeHeight() {
		return 45;
	}
}
