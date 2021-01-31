package space.bbkr.mycoturgy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import space.bbkr.mycoturgy.init.MycoturgyItems;
import space.bbkr.mycoturgy.init.MycoturgyRecipes;
import space.bbkr.mycoturgy.inventory.CookingPotInventory;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class NetheriteCleaningRecipe extends PotCookingRecipe {
	public NetheriteCleaningRecipe(Identifier id, DefaultedList<Ingredient> input, ItemStack output, Identifier bonusOutputs, int time, int hyphaCost, int lamellaCost) {
		super(id, input, output, bonusOutputs, time, hyphaCost, lamellaCost);
	}

	@Override
	public ItemStack craft(CookingPotInventory inv) {
		ItemStack ret = super.craft(inv);
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			System.out.println(stack);
			if (stack.getItem().isIn(MycoturgyItems.NETHERITE_COMPOSED)) { //TODO: better heuristic maybe? Make this whole recipe dynamic?
				ret.setTag(stack.getTag());
				System.out.println("Netherite cleaned!");
				break;
			}
		}
		return ret;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return MycoturgyRecipes.NETHERITE_CLEANING_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<NetheriteCleaningRecipe> {

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
			ItemStack result = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
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
	}
}
