package space.bbkr.mycoturgy.recipe;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.recipe.*;
import space.bbkr.mycoturgy.init.MycoturgyRecipes;
import space.bbkr.mycoturgy.inventory.CookingPotInventory;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
	public ItemStack craft(CookingPotInventory inv) {
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
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}

	public List<ItemStack> getBonusOutputs(ServerWorld world, BlockEntity be) {
		LootContext context = new LootContext.Builder(world)
				.parameter(LootContextParameters.BLOCK_ENTITY, be)
				.parameter(LootContextParameters.ORIGIN, Vec3d.of(be.getPos()))
				.parameter(LootContextParameters.BLOCK_STATE, be.getCachedState())
				.build(CRAFTING);
		return world.getServer().getLootManager().getTable(bonusOutputs).generateLoot(context);
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

	public static class Serializer implements RecipeSerializer<PotCookingRecipe> {

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
	}
}
