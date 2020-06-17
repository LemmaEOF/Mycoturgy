package space.bbkr.mycoturgy.block.entity;

import java.util.Optional;

import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.init.MycoturgyRecipes;
import space.bbkr.mycoturgy.inventory.MasonJarInventory;
import space.bbkr.mycoturgy.recipe.JarBrewingRecipe;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;

public class MasonJarBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {
	private MasonJarInventory inv = new MasonJarInventory(this.pos);
	private int processTime = 0;
	private JarBrewingRecipe currentRecipe;

	public MasonJarBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public void tick() {
		if (world == null) return;
		if (currentRecipe == null) {
			Optional<JarBrewingRecipe> potentialRecipe = this.world.getRecipeManager().getFirstMatch(MycoturgyRecipes.MASON_JAR_RECIPE, inv, this.world);
			potentialRecipe.ifPresent(jarBrewingRecipe -> currentRecipe = jarBrewingRecipe);
		} else {
			if (currentRecipe.matches(inv, world)) {
				processTime++;
				if (processTime >= currentRecipe.getTime()) {
					HaustorComponent component = Mycoturgy.HAUSTOR_COMPONENT.get(world.getChunk(this.pos));
					component.changeHypha(currentRecipe.getHyphaCost() * -1);
					component.changePrimordia(currentRecipe.getHyphaCost() * 2);
					component.changeLamella(currentRecipe.getLamellaCost() * -1);
					component.changeHypha(currentRecipe.getLamellaCost() * 2);
					inv.setStack(currentRecipe.craft(inv));
					processTime = 0;
					currentRecipe = null;
				}
				markDirty();
			} else {
				processTime = 0;
				currentRecipe = null;
				markDirty();
			}
		}
	}

	@Override
	public void setLocation(World world, BlockPos pos) {
		super.setLocation(world, pos);
		inv.setPos(pos);
	}

	public MasonJarInventory getInventory() {
		return inv;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.processTime = tag.getInt("ProcessTime");
		this.inv.setStack(0, ItemStack.fromTag(tag.getCompound("Stack")));
		if (tag.contains("Recipe", NbtType.STRING)) {
			Optional<? extends Recipe<?>> recipeOpt = this.world.getRecipeManager().get(new Identifier(tag.getString("Recipe")));
			if (recipeOpt.isPresent() && recipeOpt.get() instanceof JarBrewingRecipe) {
				this.currentRecipe = (JarBrewingRecipe)recipeOpt.get();
			}
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putInt("ProcessTime", processTime);
		tag.put("Stack", inv.getStack(0).toTag(new CompoundTag()));
		if (currentRecipe != null) {
			tag.putString("Recipe", currentRecipe.getId().toString());
		}
		return tag;
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTag(this.getCachedState(), tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTag(tag);
	}
}
