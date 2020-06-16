package space.bbkr.mycoturgy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class PatchouliBookRecipe extends ShapelessRecipe {
	private Identifier book;
	private String group;

	public PatchouliBookRecipe(Identifier id, Identifier book, String group, ItemStack output, DefaultedList<Ingredient> input) {
		super(id, group, output, input);
		this.book = book;
		this.group = group;
	}

	@Override
	public ItemStack craft(CraftingInventory craftingInventory) {
		ItemStack ret = super.craft(craftingInventory);
		ret.getOrCreateTag().putString("patchouli:book", book.toString());
		return ret;
	}

	public static class Serializer implements RecipeSerializer<PatchouliBookRecipe> {
		@Override
		public PatchouliBookRecipe read(Identifier identifier, JsonObject jsonObject) {
			String string = JsonHelper.getString(jsonObject, "group", "");
			DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
			if (defaultedList.isEmpty()) {
				throw new JsonParseException("No ingredients for shapeless recipe");
			} else if (defaultedList.size() > 9) {
				throw new JsonParseException("Too many ingredients for shapeless recipe");
			} else {
				ItemStack itemStack = ShapedRecipe.getItemStack(JsonHelper.getObject(jsonObject, "result"));
				Identifier bookId = new Identifier(JsonHelper.getString(jsonObject, "book"));
				return new PatchouliBookRecipe(identifier, bookId, string, itemStack, defaultedList);
			}
		}

		private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
			DefaultedList<Ingredient> defaultedList = DefaultedList.of();

			for(int i = 0; i < json.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(json.get(i));
				if (!ingredient.isEmpty()) {
					defaultedList.add(ingredient);
				}
			}

			return defaultedList;
		}

		@Override
		public PatchouliBookRecipe read(Identifier id, PacketByteBuf buf) {
			String string = buf.readString(32767);
			int i = buf.readVarInt();
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);

			for(int j = 0; j < defaultedList.size(); ++j) {
				defaultedList.set(j, Ingredient.fromPacket(buf));
			}

			ItemStack itemStack = buf.readItemStack();
			Identifier book = buf.readIdentifier();
			return new PatchouliBookRecipe(id, book, string, itemStack, defaultedList);
		}

		@Override
		public void write(PacketByteBuf buf, PatchouliBookRecipe recipe) {
			buf.writeString(recipe.group);
			buf.writeVarInt(recipe.getPreviewInputs().size());

			for (Ingredient ingredient : recipe.getPreviewInputs()) {
				ingredient.write(buf);
			}

			buf.writeItemStack(recipe.getOutput());
			buf.writeIdentifier(recipe.book);
		}
	}
}
