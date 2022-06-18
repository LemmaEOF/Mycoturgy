package gay.lemmaeof.mycoturgy.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import gay.lemmaeof.mycoturgy.Mycoturgy;
import gay.lemmaeof.mycoturgy.init.MycoturgyEffects;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//yes, I know HudRenderCallback exists. I wanna do MORE
@Mixin(InGameHud.class)
public abstract class MixinInGameHud extends DrawableHelper {
	private static final Identifier MYCOTURGY_ICONS_TEXTURE = new Identifier(Mycoturgy.MODID, "textures/gui/icons.png");

	private static final int[] fuckery = new int[]{4, 2, 3, 4, 5, 3, 6}; //used for making there not be waves in the fire

	@Shadow protected abstract PlayerEntity getCameraPlayer();

	@Shadow public abstract int getTicks();

	private int currentFrame;
	private int drawnFrame;
	private int lastTick;
	private int currentHeart;

	@Inject(method = "renderStatusBars", at = @At("HEAD"))
	private void updateTicks(MatrixStack matrices, CallbackInfo info) {
		if (lastTick < getTicks() && getTicks() % 2 == 0) {
			lastTick = getTicks();
			currentFrame++;
			currentFrame %= 7;
		}
		drawnFrame = currentFrame;
		currentHeart = 0;
	}

	@Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
	private void drawGriefFire(MatrixStack matrices, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo info) {
		PlayerEntity player = this.getCameraPlayer(); //only called when player exists so we're safe
		if (player.hasStatusEffect(MycoturgyEffects.GRIEF) && type == InGameHud.HeartType.CONTAINER) {
			RenderSystem.setShaderTexture(0, MYCOTURGY_ICONS_TEXTURE);
			this.drawTexture(matrices, x, y - 3, 9 * drawnFrame, 0, 9, 13);
//			this.drawTexture(matrices, x, y - 3, 9 * ((lastTick / 2 % x + currentFrame) % 7), 0, 9, 13);
			RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
			drawnFrame += fuckery[currentHeart % 7];
			currentHeart++;
			drawnFrame %= 7;
			info.cancel();
		}
	}

	@Inject(method = "renderVignetteOverlay", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V"))
	private void injectSuperRelaxedVignette(Entity e, CallbackInfo info) {
		if (e instanceof LivingEntity living && living.hasStatusEffect(MycoturgyEffects.RELAXATION)) {
			StatusEffectInstance relaxation = living.getStatusEffect(MycoturgyEffects.RELAXATION);
			if (relaxation.getAmplifier() == 1) {
				RenderSystem.setShaderColor(0.25F, 0.75F, 0, 1);
			}
		}
	}

	@SuppressWarnings("unused")
	@Mixin(InGameHud.HeartType.class)
	private static class MixinHeartType {
		@Inject(method = "fromPlayerState", at = @At("HEAD"), cancellable = true)
		private static void injectGriefHearts(PlayerEntity player, CallbackInfoReturnable<InGameHud.HeartType> info) {
			if (player.hasStatusEffect(MycoturgyEffects.GRIEF)) {
				info.setReturnValue(InGameHud.HeartType.WITHERED);
			}
		}
	}
}
