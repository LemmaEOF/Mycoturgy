package space.bbkr.mycoturgy.recipe;

import com.google.gson.JsonObject;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.init.MycoturgyComponents;
import space.bbkr.mycoturgy.init.MycoturgyRecipes;
import space.bbkr.mycoturgy.inventory.MasonJarInventory;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

//TODO: any other costs? put ash in the jar maybe?
public class JarInfusingRecipe implements Recipe<MasonJarInventory> {
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
	public boolean matches(MasonJarInventory inv, World world) {
		HaustorComponent component = MycoturgyComponents.HAUSTOR_COMPONENT.get(world.getChunk(inv.getPos()));
		return input.test(inv.getStack()) && component.getHypha() >= hyphaCost && component.getLamella() >= lamellaCost;
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
	public DefaultedList<Ingredient> getPreviewInputs() {
		return DefaultedList.ofSize(1, input);
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
	public RecipeSerializer<JarInfusingRecipe> getSerializer() {
		return MycoturgyRecipes.MASON_JAR_SERIALIZER;
	}

	@Override
	public RecipeType<JarInfusingRecipe> getType() {
		return MycoturgyRecipes.MASON_JAR_RECIPE;
	}

	public static class Serializer implements RecipeSerializer<JarInfusingRecipe> {

		@Override
		public JarInfusingRecipe read(Identifier id, JsonObject json) {
			Ingredient ing = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
			ItemStack res = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
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
	}
}
