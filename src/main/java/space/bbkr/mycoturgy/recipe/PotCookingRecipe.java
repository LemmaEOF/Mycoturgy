package space.bbkr.mycoturgy.recipe;

import java.util.List;

import space.bbkr.mycoturgy.inventory.CookingPotInventory;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class PotCookingRecipe implements Recipe<CookingPotInventory> {
	private final Identifier id;
	private final DefaultedList<Ingredient> input;
	private final ItemStack output;
	private final int time;
	private final int hyphaCost;
	private final int lamellaCost;

	public PotCookingRecipe(Identifier id, DefaultedList<Ingredient> input, ItemStack output, int time, int hyphaCost, int lamellaCost) {
		this.id = id;
		this.input = input;
		this.output = output;
		this.time = time;
		this.hyphaCost = hyphaCost;
		this.lamellaCost = lamellaCost;
	}

	public int getTime() {
		return time;
	}

	public int getHyphaCost() {
		return hyphaCost;
	}

	public int getLamellaCost() {
		return lamellaCost;
	}

	@Override
	public boolean matches(CookingPotInventory inv, World world) {
		return false;
	}

	@Override
	public ItemStack craft(CookingPotInventory inv) {
		return output.copy();
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return null;
	}

	@Override
	public RecipeType<?> getType() {
		return null;
	}
}
