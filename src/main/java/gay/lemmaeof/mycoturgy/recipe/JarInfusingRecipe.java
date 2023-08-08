package gay.lemmaeof.mycoturgy.recipe;

import com.google.gson.JsonObject;
import gay.lemmaeof.mycoturgy.component.HaustorComponent;
import gay.lemmaeof.mycoturgy.init.MycoturgyComponents;
import gay.lemmaeof.mycoturgy.init.MycoturgyRecipes;
import gay.lemmaeof.mycoturgy.inventory.SingleStackSyncedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

//TODO: any other costs? put ash in the jar maybe?
public class JarInfusingRecipe implements Recipe<SingleStackSyncedInventory> {
	private final Identifier id;
	private final Ingredient input;
	private final ItemStack output;
	private final int time;
	private final int hyphaCost;
	private final int lamellaCost;

	public JarInfusingRecipe(Identifier id, Ingredient input, ItemStack output, int time, int hyphaCost, int lamellaCost) {
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
	public boolean matches(SingleStackSyncedInventory inv, World world) {
		HaustorComponent component = MycoturgyComponents.HAUSTOR_COMPONENT.get(world.getChunk(inv.getPos()));
		return input.test(inv.getStack()) && component.getHypha() >= hyphaCost && component.getLamella() >= lamellaCost;
	}

	@Override
	public ItemStack craft(SingleStackSyncedInventory inv, DynamicRegistryManager manager) {
		return getResult(manager).copy();
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return DefaultedList.ofSize(1, input);
	}

	@Override
	public ItemStack getResult(DynamicRegistryManager manager) {
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
	public RecipeSerializer<JarInfusingRecipe> getSerializer() {
		return MycoturgyRecipes.JAR_INFUSING_SERIALIZER;
	}

	@Override
	public RecipeType<JarInfusingRecipe> getType() {
		return MycoturgyRecipes.JAR_INFUSING_RECIPE;
	}

	public static class Serializer implements QuiltRecipeSerializer<JarInfusingRecipe> {

		@Override
		public JarInfusingRecipe read(Identifier id, JsonObject json) {
			Ingredient ing = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
			ItemStack res = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
			int time = JsonHelper.getInt(json, "infusingtime", 400);
			int hypha = JsonHelper.getInt(json, "hyphacost", 0);
			int lamella = JsonHelper.getInt(json, "lamellacost", 0);
			return new JarInfusingRecipe(id, ing, res, time, hypha, lamella);
		}

		@Override
		public JarInfusingRecipe read(Identifier id, PacketByteBuf buf) {
			Ingredient ing = Ingredient.fromPacket(buf);
			ItemStack res = buf.readItemStack();
			int time = buf.readVarInt();
			int hypha = buf.readVarInt();
			int lamella = buf.readVarInt();
			return new JarInfusingRecipe(id, ing, res, time, hypha, lamella);
		}

		@Override
		public void write(PacketByteBuf buf, JarInfusingRecipe recipe) {
			recipe.input.write(buf);
			buf.writeItemStack(recipe.output);
			buf.writeVarInt(recipe.time);
			buf.writeVarInt(recipe.hyphaCost);
			buf.writeVarInt(recipe.lamellaCost);
		}

		@Override
		public JsonObject toJson(JarInfusingRecipe recipe) {
			JsonObject res = new JsonObject();

			res.addProperty("type", Registries.RECIPE_SERIALIZER.getId(MycoturgyRecipes.JAR_INFUSING_SERIALIZER).toString());

			res.add("ingredient", recipe.input.toJson());

			JsonObject output = new JsonObject();
			output.addProperty("item", Registries.ITEM.getId(recipe.output.getItem()).toString());
			if (recipe.output.getCount() != 1) {
				output.addProperty("count", recipe.output.getCount());
			}
			res.add("result", output);

			res.addProperty("infusingtime", recipe.time);
			res.addProperty("hyphacost", recipe.hyphaCost);
			res.addProperty("lamellacost", recipe.lamellaCost);

			return res;
		}
	}
}
