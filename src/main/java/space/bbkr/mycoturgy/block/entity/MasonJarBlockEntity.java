package space.bbkr.mycoturgy.block.entity;

import java.util.Optional;

import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.block.MasonJarBlock;
import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.init.MycoturgyBlocks;
import space.bbkr.mycoturgy.init.MycoturgyRecipes;
import space.bbkr.mycoturgy.inventory.MasonJarInventory;
import space.bbkr.mycoturgy.recipe.JarBrewingRecipe;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;

public class MasonJarBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {
	public int ticks = 0;
	private MasonJarInventory inv = new MasonJarInventory(this);
	private byte ashes = 0;
	private int processTime = 0;
	private JarBrewingRecipe currentRecipe;

	public MasonJarBlockEntity() {
		super(MycoturgyBlocks.MASON_JAR_BLOCK_ENTITY);
	}

	@Override
	public void tick() {
		ticks++;
		if (world == null || world.isClient || !getCachedState().get(MasonJarBlock.FILLED) || ashes <= 0) return;
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
					ashes--;
					if (ashes < 0) ashes = 0;
					world.setBlockState(pos, getCachedState().with(MasonJarBlock.FILLED, false));
				}
				markDirty();
			} else {
				processTime = 0;
				currentRecipe = null;
				markDirty();
			}
		}
	}

	public MasonJarInventory getInventory() {
		return inv;
	}

	public byte getAshes() {
		return ashes;
	}

	public void addAshes() {
		setAshes(ashes + 4);
	}

	public void setAshes(int ashes) {
		this.ashes = (byte)Math.min(16, ashes);
		sync();
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.ashes = tag.getByte("Ashes");
		this.processTime = tag.getInt("ProcessTime");
		this.inv.setStack(ItemStack.fromTag(tag.getCompound("Stack")));
		if (tag.contains("Recipe", NbtType.STRING)) {
			Optional<? extends Recipe<?>> recipeOpt = this.world.getRecipeManager().get(new Identifier(tag.getString("Recipe")));
			if (recipeOpt.isPresent() && recipeOpt.get() instanceof JarBrewingRecipe) {
				this.currentRecipe = (JarBrewingRecipe)recipeOpt.get();
			} else {
				this.currentRecipe = null;
			}
		} else {
			this.currentRecipe = null;
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putByte("Ashes", ashes);
		tag.putInt("ProcessTime", processTime);
		tag.put("Stack", inv.getStack().toTag(new CompoundTag()));
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
