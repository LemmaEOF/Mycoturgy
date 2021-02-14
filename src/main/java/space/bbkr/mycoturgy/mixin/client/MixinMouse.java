package space.bbkr.mycoturgy.mixin.client;


import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import space.bbkr.mycoturgy.init.MycoturgyItems;
import space.bbkr.mycoturgy.init.MycoturgyNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public class MixinMouse {
	@Shadow @Final private MinecraftClient client;

	@Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void handleSlotScroll(long window, double horizontal, double vertical, CallbackInfo info, double velocity, float clippedVelocity) {
		ItemStack mainStack = this.client.player.getMainHandStack();
		ItemStack offStack = this.client.player.getOffHandStack();
		if (Screen.hasAltDown()) {
			if (mainStack.getItem() == MycoturgyItems.SPORE_POUCH) {
				sendPacket(true, clippedVelocity);
				info.cancel();
			} else if (offStack.getItem() == MycoturgyItems.SPORE_POUCH) {
				sendPacket(false, clippedVelocity);
				info.cancel();
			}
		}
	}

	private void sendPacket(boolean isMain, float direction) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(isMain);
		buf.writeVarInt(direction > 0? 1 : (direction < 0? -1 : 0));
		ClientPlayNetworking.send(MycoturgyNetworking.SCROLL, buf);
	}
}
