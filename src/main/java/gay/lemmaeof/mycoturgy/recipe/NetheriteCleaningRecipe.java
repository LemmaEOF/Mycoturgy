package gay.lemmaeof.mycoturgy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gay.lemmaeof.mycoturgy.init.MycoturgyItems;
import gay.lemmaeof.mycoturgy.init.MycoturgyRecipes;
import gay.lemmaeof.mycoturgy.inventory.CookingPotInventory;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

public class NetheriteCleaningRecipe extends PotCookingRecipe {
	public NetheriteCleaningRecipe(Identifier id, DefaultedList<Ingredient> input, ItemStack output, Identifier bonusOutputs, int time, int hyphaCost, int lamellaCost) {
		super(id, input, output, bonusOutputs, time, hyphaCost, lamellaCost);
	}

	@Override
	public ItemStack craft(CookingPotInventory inv) {
		ItemStack ret = super.craft(inv);
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (stack.isIn(MycoturgyItems.NETHERITE_COMPOSED)) { //TODO: better heuristic maybe? Make this whole recipe dynamic?
				ret.setNbt(stack.getNbt());
				break;
			}
		}
		return ret;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return MycoturgyRecipes.NETHERITE_CLEANING_SERIALIZER;
	}

	public static class Serializer implements QuiltRecipeSerializer<NetheriteCleaningRecipe> {

		protected static DefaultedList<Ingredient> getIngredients(JsonArray json) {
			DefaultedList<Ingredient> defaultedList = DefaultedList.of();

			for(int i = 0; i < json.size(); i++) {
				Ingredient ingredient = Ingredient.fromJson(json.get(i));
				if (!ingredient.isEmpty()) {
					defaultedList.add(ingredient);
				}
			}

			return defaultedList;
		}

		@Override
		public NetheriteCleaningRecipe read(Identifier id, JsonObject json) {
			DefaultedList<Ingredient> ingredients = getIngredients(JsonHelper.getArray(json, "ingredients"));
			ItemStack result = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
			Identifier bonus = new Identifier(JsonHelper.getString(json, "bonus", "empty"));
			int cookingTime = JsonHelper.getInt(json, "cookingtime", 200);
			int hyphaCost = JsonHelper.getInt(json, "hyphacost", 0);
			int lamellaCost = JsonHelper.getInt(json, "lamellacost", 0);
			return new NetheriteCleaningRecipe(id, ingredients, result, bonus, cookingTime, hyphaCost, lamellaCost);
		}

		@Override
		public NetheriteCleaningRecipe read(Identifier id, PacketByteBuf buf) {
			int inputSize = buf.readVarInt();
			DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(inputSize, Ingredient.EMPTY);
			for (int i = 0; i < inputSize; i++) {
				ingredients.set(i, Ingredient.fromPacket(buf));
			}
			ItemStack result = buf.readItemStack();
			Identifier bonus = buf.readIdentifier();
			int time = buf.readVarInt();
			int hyphaCost = buf.readVarInt();
			int lamellaCost = buf.readVarInt();
			return new NetheriteCleaningRecipe(id, ingredients, result, bonus, time, hyphaCost, lamellaCost);
		}

		@Override
		public void write(PacketByteBuf buf, NetheriteCleaningRecipe recipe) {
			buf.writeVarInt(recipe.input.size());

			for (Ingredient ingredient : recipe.input) {
				ingredient.write(buf);
			}

			buf.writeItemStack(recipe.output);
			buf.writeIdentifier(recipe.bonusOutputs);
			buf.writeVarInt(recipe.time);
			buf.writeVarInt(recipe.hyphaCost);
			buf.writeVarInt(recipe.lamellaCost);
		}

		@Override
		public JsonObject toJson(NetheriteCleaningRecipe recipe) {
			JsonObject res = new JsonObject();

			res.addProperty("type", Registry.RECIPE_SERIALIZER.getId(MycoturgyRecipes.NETHERITE_CLEANING_SERIALIZER).toString());

			JsonArray ingredients = new JsonArray();
			for (Ingredient ingredient : recipe.input) {
				ingredients.add(ingredient.toJson());
			}
			res.add("ingredients", ingredients);

			JsonObject output = new JsonObject();
			output.addProperty("item", Registry.ITEM.getId(recipe.output.getItem()).toString());
			if (recipe.output.getCount() != 1) {
				output.addProperty("count", recipe.output.getCount());
			}
			res.add("result", output);

			res.addProperty("bonus", recipe.bonusOutputs.toString());
			res.addProperty("cookingtime", recipe.time);
			res.addProperty("hyphacost", recipe.hyphaCost);
			res.addProperty("lamellacost", recipe.lamellaCost);

			return res;
		}
	}
}
