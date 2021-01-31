package space.bbkr.mycoturgy.block.entity;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import space.bbkr.mycoturgy.block.CookingPotBlock;
import space.bbkr.mycoturgy.block.MasonJarBlock;
import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.init.MycoturgyBlocks;
import space.bbkr.mycoturgy.init.MycoturgyComponents;
import space.bbkr.mycoturgy.init.MycoturgyRecipes;
import space.bbkr.mycoturgy.inventory.CookingPotInventory;
import space.bbkr.mycoturgy.inventory.SingleStackSyncedInventory;
import space.bbkr.mycoturgy.recipe.PotCookingRecipe;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Vec3d;

import net.fabricmc.fabric.api.util.NbtType;

public class CookingPotBlockEntity extends SyncingBlockEntity implements Tickable {
	public int ticks = 0;
	private final CookingPotInventory inv = new CookingPotInventory(this);
	private final SingleStackSyncedInventory outInv = new SingleStackSyncedInventory(this);
	private int processTime = 0;
	private PotCookingRecipe currentRecipe;

	public CookingPotBlockEntity() {
		super(MycoturgyBlocks.COOKING_POT_BLOCK_ENTITY);
	}

	public CookingPotInventory getInventory() {
		return inv;
	}

	public SingleStackSyncedInventory getOutputInventory() {
		return outInv;
	}

	@Override
	public void tick() {
		ticks++;
		if (world == null || world.isClient || !getCachedState().get(CookingPotBlock.FILLED)) return;
		if (currentRecipe == null) {
			Optional<PotCookingRecipe> potentialRecipe = this.world.getRecipeManager().getFirstMatch(MycoturgyRecipes.POT_COOKING_RECIPE, inv, this.world);
			potentialRecipe.ifPresent(recipe -> currentRecipe = recipe);
		} else {
			if (currentRecipe.matches(inv, world)) {
				Vec3d potPos = new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				Random random = new Random();
				processTime++;
				if (processTime % 10 == 0) ((ServerWorld)world).spawnParticles(ParticleTypes.BUBBLE, potPos.x + random.nextDouble() * (1/8D) * (double)(random.nextBoolean() ? 1 : -1), potPos.y + random.nextDouble() * (3/8D), potPos.z + random.nextDouble() * (1/8D) * (double)(random.nextBoolean() ? 1 : -1), random.nextInt(5), 0.0, 0.1, 0.0, 0.1);
				if (processTime >= currentRecipe.getTime()) {
					HaustorComponent component = MycoturgyComponents.HAUSTOR_COMPONENT.get(world.getChunk(this.pos));
					component.changeHypha(currentRecipe.getHyphaCost() * -1);
					component.changePrimordia(currentRecipe.getHyphaCost() * 2);
					component.changeLamella(currentRecipe.getLamellaCost() * -1);
					component.changeHypha(currentRecipe.getLamellaCost() * 2);
					outInv.setStack(currentRecipe.craft(inv));
					List<ItemStack> bonuses = currentRecipe.getBonusOutputs((ServerWorld) world, this);
					inv.clear();
					for (ItemStack stack : bonuses) {
						ItemScatterer.spawn(world, getPos().getX(), getPos().getY(), getPos().getZ(), stack);
					}
					processTime = 0;
					currentRecipe = null;
					world.playSound(null, pos, SoundEvents.ENTITY_WITCH_DRINK, SoundCategory.BLOCKS, 1f, 1f);
					((ServerWorld)world).spawnParticles(ParticleTypes.SPLASH, potPos.x + random.nextDouble() * (3/16D) * (double)(random.nextBoolean() ? 1 : -1), potPos.y + random.nextDouble() * (1/2D), potPos.z + random.nextDouble() * (3/16D) * (double)(random.nextBoolean() ? 1 : -1), 30, 0.0, 0.7, 0.0, 0.7);
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

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.processTime = tag.getInt("ProcessTime");
		this.inv.clear();
		this.outInv.clear();
		this.inv.readTags(tag.getList("Items", NbtType.COMPOUND));
		this.outInv.readTags(tag.getList("Output", NbtType.COMPOUND));
		if (tag.contains("Recipe", NbtType.STRING)) {
			Optional<? extends Recipe<?>> recipeOpt = this.world.getRecipeManager().get(new Identifier(tag.getString("Recipe")));
			if (recipeOpt.isPresent() && recipeOpt.get() instanceof PotCookingRecipe) {
				this.currentRecipe = (PotCookingRecipe)recipeOpt.get();
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
		tag.putInt("ProcessTime", processTime);
		tag.put("Items", inv.getTags());
		tag.put("Output", outInv.getTags());
		if (currentRecipe != null) {
			tag.putString("Recipe", currentRecipe.getId().toString());
		}
		return tag;
	}

}
