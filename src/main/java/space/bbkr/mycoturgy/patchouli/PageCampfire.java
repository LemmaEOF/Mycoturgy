package space.bbkr.mycoturgy.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PageCampfire extends PageDoubleRecipeRegistry<CampfireCookingRecipe> {
	public PageCampfire() {
		super(RecipeType.CAMPFIRE_COOKING);
	}

	@Override
	protected void drawRecipe(MatrixStack ms, CampfireCookingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		this.mc.getTextureManager().bindTexture(this.book.craftingTexture);
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(ms, recipeX, recipeY, 11.0F, 71.0F, 96, 24, 128, 128);
		this.parent.drawCenteredStringNoShadow(ms, this.getTitle(second), 58, recipeY - 10, this.book.headerColor);
		this.parent.renderIngredient(ms, recipeX + 4, recipeY + 4, mouseX, mouseY, recipe.getPreviewInputs().get(0));
		this.parent.renderItemStack(ms, recipeX + 76, recipeY + 4, mouseX, mouseY, recipe.getOutput());
	}

	@Override
	protected ItemStack getRecipeOutput(CampfireCookingRecipe recipe) {
		return recipe == null ? ItemStack.EMPTY : recipe.getOutput();
	}

	@Override
	protected int getRecipeHeight() {
		return 45;
	}
}
