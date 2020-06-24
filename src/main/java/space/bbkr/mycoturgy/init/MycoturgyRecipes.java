package space.bbkr.mycoturgy.init;


import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.recipe.JarBrewingRecipe;
import space.bbkr.mycoturgy.recipe.PatchouliBookRecipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MycoturgyRecipes {
	public static RecipeType<JarBrewingRecipe> MASON_JAR_RECIPE;
	public static RecipeSerializer<JarBrewingRecipe> MASON_JAR_SERIALIZER;
	public static RecipeSerializer<PatchouliBookRecipe> PATCHOULI_BOOK_SERIALZIER;

	public static void init() {
		MASON_JAR_RECIPE = register("jar_brewing");
		MASON_JAR_SERIALIZER = register("jar_brewing", new JarBrewingRecipe.Serializer());
		PATCHOULI_BOOK_SERIALZIER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Mycoturgy.MODID, "patchouli_book"), new PatchouliBookRecipe.Serializer());
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
