package space.bbkr.mycoturgy.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.init.MycoturgyEffects;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

//yes, I know HudRenderCallback exists. I wanna do MORE
@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class MixinInGameHud extends DrawableHelper {
	private static final Identifier MYCOTURGY_ICONS_TEXTURE = new Identifier(Mycoturgy.MODID, "textures/gui/icons.png");

	private static final int[] fuckery = new int[]{4, 2, 3, 4, 5, 3, 6}; //used for making there not be waves in the fire

	@Shadow protected abstract PlayerEntity getCameraPlayer();

	@Shadow @Final private MinecraftClient client;

	@Shadow public abstract int getTicks();

	@Shadow private int scaledWidth;
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

	/**
	 * @author LemmaEOF
	 * @reason withered hearts with Grief
	 */
	@Redirect(method = "renderStatusBars",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/player/PlayerEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z",
					ordinal = 2)
	)
	private boolean hasGrief(PlayerEntity player, StatusEffect wither) {
		return player.hasStatusEffect(wither) || player.hasStatusEffect(MycoturgyEffects.GRIEF);
	}

	/**
	 * @author LemmaEOF
	 * @reason heart fire with Grief
	 */
	@Redirect(method = "renderStatusBars",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
					ordinal = 3)
	)
	private void drawGriefFire(InGameHud helper, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
		PlayerEntity player = this.getCameraPlayer(); //already checked non-null because we're a mixin
		if (player.hasStatusEffect(MycoturgyEffects.GRIEF)) {
			this.client.getTextureManager().bindTexture(MYCOTURGY_ICONS_TEXTURE);
			this.drawTexture(matrices, x, y - 3, 9 * drawnFrame, 0, 9, 13);
//			this.drawTexture(matrices, x, y - 3, 9 * ((lastTick / 2 % x + currentFrame) % 7), 0, 9, 13);
			this.client.getTextureManager().bindTexture(GUI_ICONS_TEXTURE);
			drawnFrame += fuckery[currentHeart % 7];
			currentHeart++;
			drawnFrame %= 7;
		} else {
			this.drawTexture(matrices, x, y, u, v, width, height);
		}
	}

}
