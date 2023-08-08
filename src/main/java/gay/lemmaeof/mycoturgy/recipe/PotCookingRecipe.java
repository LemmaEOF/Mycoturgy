package gay.lemmaeof.mycoturgy.recipe;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.loot.context.*;
import net.minecraft.recipe.*;
import gay.lemmaeof.mycoturgy.init.MycoturgyRecipes;
import gay.lemmaeof.mycoturgy.inventory.CookingPotInventory;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

public class PotCookingRecipe implements Recipe<CookingPotInventory> {
	protected static final LootContextType CRAFTING = new LootContextType.Builder()
			.require(LootContextParameters.BLOCK_ENTITY)
			.require(LootContextParameters.ORIGIN)
			.require(LootContextParameters.BLOCK_STATE)
			.build();

	protected final Identifier id;
	protected final DefaultedList<Ingredient> input;
	protected final ItemStack output;
	protected final Identifier bonusOutputs;
	protected final int time;
	protected final int hyphaCost;
	protected final int lamellaCost;

	public PotCookingRecipe(Identifier id, DefaultedList<Ingredient> input, ItemStack output, Identifier bonusOutputs, int time, int hyphaCost, int lamellaCost) {
		this.id = id;
		this.input = input;
		this.output = output;
		this.bonusOutputs = bonusOutputs;
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
		RecipeMatcher recipeFinder = new RecipeMatcher();
		int heldStacks = 0;

		for(int i = 0; i < inv.size(); i++) {
			ItemStack itemStack = inv.getStack(i);
			if (!itemStack.isEmpty()) {
				heldStacks++;
				recipeFinder.addInput(itemStack, 1);
			}
		}

		return heldStacks == this.input.size() && recipeFinder.match(this, null);
	}

	@Override
	public ItemStack craft(CookingPotInventory inv, DynamicRegistryManager manager) {
		return output.copy();
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return input;
	}

	@Override
	public ItemStack getResult(DynamicRegistryManager manager) {
		return output;
	}

	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}

	public List<ItemStack> getBonusOutputs(ServerWorld world, BlockEntity be) {
		LootContextParameterSet context = new LootContextParameterSet.Builder(world)
				.add(LootContextParameters.BLOCK_ENTITY, be)
				.add(LootContextParameters.ORIGIN, Vec3d.of(be.getPos()))
				.add(LootContextParameters.BLOCK_STATE, be.getCachedState())
				.build(CRAFTING);
		return world.getServer().getLootManager().getLootTable(bonusOutputs).generateLoot(context);
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return MycoturgyRecipes.POT_COOKING_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return MycoturgyRecipes.POT_COOKING_RECIPE;
	}

	public static class Serializer implements QuiltRecipeSerializer<PotCookingRecipe> {

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
		public PotCookingRecipe read(Identifier id, JsonObject json) {
			DefaultedList<Ingredient> ingredients = getIngredients(JsonHelper.getArray(json, "ingredients"));
			ItemStack result = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
			Identifier bonus = new Identifier(JsonHelper.getString(json, "bonus", "empty"));
			int cookingTime = JsonHelper.getInt(json, "cookingtime", 200);
			int hyphaCost = JsonHelper.getInt(json, "hyphacost", 0);
			int lamellaCost = JsonHelper.getInt(json, "lamellacost", 0);
			return new PotCookingRecipe(id, ingredients, result, bonus, cookingTime, hyphaCost, lamellaCost);
		}

		@Override
		public PotCookingRecipe read(Identifier id, PacketByteBuf buf) {
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
			return new PotCookingRecipe(id, ingredients, result, bonus, time, hyphaCost, lamellaCost);
		}

		@Override
		public void write(PacketByteBuf buf, PotCookingRecipe recipe) {
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
		public JsonObject toJson(PotCookingRecipe recipe) {
			JsonObject res = new JsonObject();

			res.addProperty("type", Registries.RECIPE_SERIALIZER.getId(MycoturgyRecipes.POT_COOKING_SERIALIZER).toString());

			JsonArray ingredients = new JsonArray();
			for (Ingredient ingredient : recipe.input) {
				ingredients.add(ingredient.toJson());
			}
			res.add("ingredients", ingredients);

			JsonObject output = new JsonObject();
			output.addProperty("item", Registries.ITEM.getId(recipe.output.getItem()).toString());
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
