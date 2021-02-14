package space.bbkr.mycoturgy.init;


import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.recipe.JarInfusingRecipe;
import space.bbkr.mycoturgy.recipe.NetheriteCleaningRecipe;
import space.bbkr.mycoturgy.recipe.PotCookingRecipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MycoturgyRecipes {
	public static RecipeType<JarInfusingRecipe> MASON_JAR_RECIPE;
	public static RecipeType<PotCookingRecipe> POT_COOKING_RECIPE;
	public static RecipeSerializer<JarInfusingRecipe> MASON_JAR_SERIALIZER;
	public static RecipeSerializer<PotCookingRecipe> POT_COOKING_SERIALIZER;
	public static RecipeSerializer<NetheriteCleaningRecipe> NETHERITE_CLEANING_SERIALIZER;

	public static void init() {
		MASON_JAR_RECIPE = register("jar_infusing");
		POT_COOKING_RECIPE = register("pot_cooking");
		MASON_JAR_SERIALIZER = register("jar_infusing", new JarInfusingRecipe.Serializer());
		POT_COOKING_SERIALIZER = register("pot_cooking", new PotCookingRecipe.Serializer());
		NETHERITE_CLEANING_SERIALIZER = register("netherite_cleaning", new NetheriteCleaningRecipe.Serializer());
	}

	public static <T extends Recipe<?>> RecipeType<T> register(String name) {
		return Registry.register(Registry.RECIPE_TYPE, new Identifier(Mycoturgy.MODID, name), new RecipeType<T>() {
			@Override
			public String toString() {
				return Mycoturgy.MODID + ":" + name;
			}
		});
	}

	public static <T extends Recipe<?>> RecipeSerializer<T> register(String name, RecipeSerializer<T> serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Mycoturgy.MODID, name), serializer);
	}
}
