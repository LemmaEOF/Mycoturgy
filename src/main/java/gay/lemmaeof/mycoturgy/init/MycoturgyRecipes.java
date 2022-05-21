package gay.lemmaeof.mycoturgy.init;


import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.recipe.JarInfusingRecipe;
import gay.lemmaeof.mycoturgy.recipe.NetheriteCleaningRecipe;
import gay.lemmaeof.mycoturgy.recipe.PipeFillingRecipe;
import gay.lemmaeof.mycoturgy.recipe.PotCookingRecipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MycoturgyRecipes {
	public static RecipeType<JarInfusingRecipe> JAR_INFUSING_RECIPE;
	public static RecipeType<PotCookingRecipe> POT_COOKING_RECIPE;
	public static RecipeSerializer<JarInfusingRecipe> JAR_INFUSING_SERIALIZER;
	public static RecipeSerializer<PotCookingRecipe> POT_COOKING_SERIALIZER;
	public static RecipeSerializer<NetheriteCleaningRecipe> NETHERITE_CLEANING_SERIALIZER;
	public static RecipeSerializer<PipeFillingRecipe> PIPE_FILLING;

	public static void init() {
		JAR_INFUSING_RECIPE = register("jar_infusing");
		POT_COOKING_RECIPE = register("pot_cooking");
		JAR_INFUSING_SERIALIZER = register("jar_infusing", new JarInfusingRecipe.Serializer());
		POT_COOKING_SERIALIZER = register("pot_cooking", new PotCookingRecipe.Serializer());
		NETHERITE_CLEANING_SERIALIZER = register("netherite_cleaning", new NetheriteCleaningRecipe.Serializer());
		PIPE_FILLING = register("pipe_filling", new SpecialRecipeSerializer<>(PipeFillingRecipe::new));
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
