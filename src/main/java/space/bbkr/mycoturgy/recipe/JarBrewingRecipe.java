package space.bbkr.mycoturgy.recipe;

import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.inventory.MasonJarInventory;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class JarBrewingRecipe implements Recipe<MasonJarInventory> {
	private Identifier id;
	private Ingredient input;
	private ItemStack output;
	private int time;
	private int hyphaCost;
	private int lamellaCost;

	public JarBrewingRecipe(Identifier id, Ingredient input, ItemStack output, int time, int hyphaCost, int lamellaCost) {
		this.id = id;
		this.input = input;
		this.output = output;
		this.time = time;
		this.hyphaCost = hyphaCost;
		this.lamellaCost = lamellaCost;
	}

	@Override
	public boolean matches(MasonJarInventory inv, World world) {
		HaustorComponent component = Mycoturgy.HAUSTOR_COMPONENT.get(world.getChunk(inv.getPos()));
		return input.test(inv.getStack(0)) && component.getHypha() >= hyphaCost && component.getLamella() >= lamellaCost;
	}

	@Override
	public ItemStack craft(MasonJarInventory inv) {
		return getOutput().copy();
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
	public boolean isIgnoredInRecipeBook() {
		return true;
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
