package gay.lemmaeof.mycoturgy.item;

import java.util.List;
import java.util.Random;

import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.init.MycoturgyEffects;
import gay.lemmaeof.mycoturgy.init.MycoturgyItems;
import org.jetbrains.annotations.Nullable;

import net.minecraft.advancement.Advancement;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class SporebrushPipeItem extends Item {
	public static final Identifier BIG_HIT = new Identifier(Mycoturgy.MODID, "big_hit");
	public static final Identifier SHARE_PIPE = new Identifier(Mycoturgy.MODID, "share_pipe");
	public static final int MAX_FILL = 200;

	public SporebrushPipeItem(Settings settings) {
		super(settings);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.CROSSBOW;
	}

	private int getPipeFill(ItemStack stack) {
		return stack.getOrCreateNbt().getInt("PipeFill");
	}

	private void setNewFill(ItemStack stack, int remainingTicks) {
		stack.getOrCreateNbt().putInt("PipeFill", remainingTicks);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient) {
			if (hand == Hand.MAIN_HAND
					&& getPipeFill(user.getStackInHand(hand)) > 0
					&& user.getStackInHand(Hand.OFF_HAND).isIn(MycoturgyItems.PIPE_LIGHTS)) {
				user.setCurrentHand(Hand.MAIN_HAND);
			}
		}
		return super.use(world, user, hand);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!world.isClient && stack.getOrCreateNbt().containsUuid("LastHit")) {
			int shareCooldown = stack.getNbt().getInt("ShareCooldown");
			if (shareCooldown == 0) {
				stack.getNbt().remove("LastHit");
				stack.getNbt().remove("ShareCooldown");
			} else {
				stack.getNbt().putInt("ShareCooldown", shareCooldown - 1);
			}
		}
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (remainingUseTicks == getPipeFill(stack)) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		if (remainingUseTicks <= getPipeFill(stack)) {
			Random random = world.getRandom();
			if (remainingUseTicks % 5 == 0) {
				world.addParticle(
						ParticleTypes.FLAME,
						user.getX() + random.nextDouble() / 5.0 * (double) (random.nextBoolean() ? 1 : -1),
						user.getEyeY() - 0.1 + random.nextDouble() / 5.0 * (double) (random.nextBoolean() ? 1 : -1),
						user.getZ() + random.nextDouble() / 5.0 * (double) (random.nextBoolean() ? 1 : -1),
						0,
						0,
						0);
			}
			if (remainingUseTicks % 20 == 0) {
				world.addParticle(
						ParticleTypes.CAMPFIRE_COSY_SMOKE,
						user.getX() + random.nextDouble() / 5.0 * (double) (random.nextBoolean() ? 1 : -1),
						user.getEyeY() - 0.1 + random.nextDouble() / 5.0 * (double) (random.nextBoolean() ? 1 : -1),
						user.getZ() + random.nextDouble() / 5.0 * (double) (random.nextBoolean() ? 1 : -1),
						0,
						0.005,
						0);
			}
		}
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (world.isClient) return;
		if (remainingUseTicks <= getPipeFill(stack)) {
			user.addStatusEffect(new StatusEffectInstance(MycoturgyEffects.RELAXATION, (getPipeFill(stack) - remainingUseTicks) * 10, 0));
			if (user instanceof ServerPlayerEntity player) {
				if (stack.getOrCreateNbt().containsUuid("LastHit")) {
					MinecraftServer server = world.getServer();
					ServerPlayerEntity other = world.getServer().getPlayerManager().getPlayer(stack.getNbt().getUuid("LastHit"));
					Advancement advancement = server.getAdvancementLoader().get(SHARE_PIPE);
					server.getPlayerManager().getAdvancementTracker(player).grantCriterion(advancement, "share_pipe");
					server.getPlayerManager().getAdvancementTracker(other).grantCriterion(advancement, "share_pipe");
				}
				stack.getNbt().putUuid("LastHit", user.getUuid());
				stack.getNbt().putInt("ShareCountdown", 600);
			}
			setNewFill(stack, remainingUseTicks);

		}
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (user instanceof ServerPlayerEntity player && getPipeFill(stack) == MAX_FILL) {
			MinecraftServer server = world.getServer();
			Advancement advancement = server.getAdvancementLoader().get(BIG_HIT);
			server.getPlayerManager().getAdvancementTracker(player).grantCriterion(advancement, "big_hit");
		}
		user.addStatusEffect(new StatusEffectInstance(MycoturgyEffects.RELAXATION, getPipeFill(stack) * 10, 0));
		stack.getOrCreateNbt().putInt("PipeFill", 0);
		return stack;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return getPipeFill(stack) + 40;
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return getPipeFill(stack) != 0 && getPipeFill(stack) != MAX_FILL;
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		return 0x4D5BB1;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return Math.round((float)getPipeFill(stack) * 13.0F / (float)MAX_FILL);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		if (context.isAdvanced()) {
			tooltip.add(new TranslatableText("tooltip.mycoturgy.pipe_fill", getPipeFill(stack), MAX_FILL));
		}
	}
}
