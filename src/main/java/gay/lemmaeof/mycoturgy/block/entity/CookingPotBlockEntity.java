package gay.lemmaeof.mycoturgy.block.entity;

import gay.lemmaeof.mycoturgy.block.CookingPotBlock;
import gay.lemmaeof.mycoturgy.component.HaustorComponent;
import gay.lemmaeof.mycoturgy.init.MycoturgyBlocks;
import gay.lemmaeof.mycoturgy.init.MycoturgyComponents;
import gay.lemmaeof.mycoturgy.init.MycoturgyRecipes;
import gay.lemmaeof.mycoturgy.inventory.CookingPotInventory;
import gay.lemmaeof.mycoturgy.inventory.SingleStackSyncedInventory;
import gay.lemmaeof.mycoturgy.recipe.PotCookingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CookingPotBlockEntity extends SyncingBlockEntity {
	public int ticks = 0;
	private final CookingPotInventory inv = new CookingPotInventory(this);
	private final SingleStackSyncedInventory outInv = new SingleStackSyncedInventory(this);
	private int processTime = 0;
	private PotCookingRecipe currentRecipe;

	public CookingPotBlockEntity(BlockPos pos, BlockState state) {
		super(MycoturgyBlocks.COOKING_POT_BLOCK_ENTITY, pos, state);
	}

	public CookingPotInventory getInventory() {
		return inv;
	}

	public SingleStackSyncedInventory getOutputInventory() {
		return outInv;
	}

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
					component.spendHypha(currentRecipe.getHyphaCost());
					component.spendLamella(currentRecipe.getLamellaCost());
					outInv.setStack(currentRecipe.craft(inv, DynamicRegistryManager.EMPTY));
					List<ItemStack> bonuses = currentRecipe.getBonusOutputs((ServerWorld) world, this);
					inv.clear();
					for (ItemStack stack : bonuses) {
						ItemScatterer.spawn(world, getPos().getX(), getPos().getY(), getPos().getZ(), stack);
					}
					processTime = 0;
					currentRecipe = null;
					world.playSound(null, pos, SoundEvents.ENTITY_WITCH_DRINK, SoundCategory.BLOCKS, 1f, 1f);
					((ServerWorld)world).spawnParticles(ParticleTypes.WATER_SPLASH, potPos.x + random.nextDouble() * (3/16D) * (double)(random.nextBoolean() ? 1 : -1), potPos.y + random.nextDouble() * (1/2D), potPos.z + random.nextDouble() * (3/16D) * (double)(random.nextBoolean() ? 1 : -1), 30, 0.0, 0.7, 0.0, 0.7);
					world.setBlockState(pos, getCachedState().with(CookingPotBlock.FILLED, false));
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
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		this.processTime = tag.getInt("ProcessTime");
		this.inv.clear();
		this.outInv.clear();
		this.inv.readNbtList(tag.getList("Items", NbtElement.COMPOUND_TYPE));
		this.outInv.readNbtList(tag.getList("Output", NbtElement.COMPOUND_TYPE));
		if (tag.contains("Recipe", NbtElement.STRING_TYPE)) {
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
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.putInt("ProcessTime", processTime);
		tag.put("Items", inv.toNbtList());
		tag.put("Output", outInv.toNbtList());
		if (currentRecipe != null) {
			tag.putString("Recipe", currentRecipe.getId().toString());
		}
	}

}
