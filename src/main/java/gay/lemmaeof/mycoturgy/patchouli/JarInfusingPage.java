package gay.lemmaeof.mycoturgy.patchouli;

import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.init.MycoturgyRecipes;
import gay.lemmaeof.mycoturgy.recipe.JarInfusingRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class JarInfusingPage extends PageDoubleRecipeRegistry<JarInfusingRecipe> {
	private static final Identifier JAR_INFUSING_TEXTURE = new Identifier(Mycoturgy.MODID, "textures/gui/jar_infusing.png");

	public JarInfusingPage() {
		super(MycoturgyRecipes.JAR_INFUSING_RECIPE);
	}

	@Override
	protected void drawRecipe(GuiGraphics graphics, JarInfusingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		graphics.drawTexture(JAR_INFUSING_TEXTURE, recipeX, recipeY, 11.0F, 71.0F, 96, 24, 128, 128);
		this.parent.drawCenteredStringNoShadow(graphics, this.getTitle(second).asOrderedText(), 58, recipeY - 10, this.book.headerColor);
		this.parent.renderIngredient(graphics, recipeX + 4, recipeY + 4, mouseX, mouseY, recipe.getIngredients().get(0));
		this.parent.renderItemStack(graphics, recipeX + 76, recipeY + 4, mouseX, mouseY, recipe.getResult(DynamicRegistryManager.EMPTY));
		if (recipe.getHyphaCost() > 0) {
			this.parent.drawCenteredStringNoShadow(graphics, "" + recipe.getHyphaCost(), recipeX + 31, recipeY + 12, this.book.textColor);
			if (this.parent.isMouseInRelativeRange(mouseX, mouseY, recipeX + 23, recipeY, 16, 20)) {
				this.parent.setTooltip(Text.translatable("text.mycoturgy.hypha_cost"));
			}
		}
		if (recipe.getLamellaCost() > 0) {
			this.parent.drawCenteredStringNoShadow(graphics, "" + recipe.getLamellaCost(), recipeX + 65, recipeY + 12, this.book.textColor);
			if (this.parent.isMouseInRelativeRange(mouseX, mouseY, recipeX + 54, recipeY, 16, 20)) {
				this.parent.setTooltip(Text.translatable("text.mycoturgy.lamella_cost"));
			}
		}
	}

	@Override
	protected ItemStack getRecipeOutput(World world, JarInfusingRecipe recipe) {
		return recipe == null ? ItemStack.EMPTY : recipe.getResult(world.getRegistryManager());
	}

	@Override
	protected int getRecipeHeight() {
		return 45;
	}
}
