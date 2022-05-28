package gay.lemmaeof.mycoturgy.recipe;

import gay.lemmaeof.mycoturgy.init.MycoturgyItems;
import gay.lemmaeof.mycoturgy.init.MycoturgyRecipes;
import gay.lemmaeof.mycoturgy.item.SporebrushPipeItem;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class PipeFillingRecipe extends SpecialCraftingRecipe {
	public PipeFillingRecipe(Identifier identifier) {
		super(identifier);
	}

	@Override
	public boolean matches(CraftingInventory inventory, World world) {
		ItemStack pipe = ItemStack.EMPTY;
		int leavesCount = 0;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (stack.getItem() == MycoturgyItems.SPOREBRUSH_PIPE) {
				if (!pipe.isEmpty()) return false; //only one pipe!
				pipe = stack;
			} else if (stack.getItem() == MycoturgyItems.SPOREBRUSH_LEAVES) {
				leavesCount++;
			}
			else if (!stack.isEmpty()) {
				return false;
			}
		}
		return !pipe.isEmpty()
				&& leavesCount > 0
				&& pipe.getOrCreateNbt().getInt("PipeFill") + (leavesCount - 1) * 25 <= SporebrushPipeItem.MAX_FILL;
	}

	@Override
	public ItemStack craft(CraftingInventory inventory) {
		ItemStack pipe = ItemStack.EMPTY;
		int leavesCount = 0;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (stack.getItem() == MycoturgyItems.SPOREBRUSH_PIPE) {
				pipe = stack.copy();
			} else if (stack.getItem() == MycoturgyItems.SPOREBRUSH_LEAVES) {
				leavesCount++;
			}
		}
		pipe.getOrCreateNbt().putInt("PipeFill", Math.min(SporebrushPipeItem.MAX_FILL, pipe.getNbt().getInt("PipeFill") + leavesCount * 25));
		return pipe;
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return MycoturgyRecipes.PIPE_FILLING;
	}
}
