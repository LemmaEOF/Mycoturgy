package gay.lemmaeof.mycoturgy.mixin.client;


import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import gay.lemmaeof.mycoturgy.init.MycoturgyItems;
import gay.lemmaeof.mycoturgy.init.MycoturgyNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

@Mixin(Mouse.class)
public class MixinMouse {
	@Shadow @Final private MinecraftClient client;

	@Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void handleSlotScroll(long window, double scrollDeltaX, double scrollDeltaY, CallbackInfo info, double velocity, int scrollDelta) {
		ItemStack mainStack = this.client.player.getMainHandStack();
		ItemStack offStack = this.client.player.getOffHandStack();
		if (Screen.hasAltDown()) {
			if (mainStack.getItem() == MycoturgyItems.SPORE_POUCH) {
				sendPacket(true, scrollDelta);
				info.cancel();
			} else if (offStack.getItem() == MycoturgyItems.SPORE_POUCH) {
				sendPacket(false, scrollDelta);
				info.cancel();
			}
		}
	}

	private void sendPacket(boolean isMain, int scrollDelta) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(isMain);
		buf.writeVarInt(scrollDelta);
		ClientPlayNetworking.send(MycoturgyNetworking.SCROLL, buf);
	}
}
