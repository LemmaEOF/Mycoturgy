package space.bbkr.mycoturgy.init;

import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.item.SporePouchItem;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MycoturgyNetworking {
	public static final Identifier SCROLL = new Identifier(Mycoturgy.MODID, "scroll");

	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(SCROLL, MycoturgyNetworking::handleScroll);
	}

	public static void handleScroll(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		boolean inMain = buf.readBoolean();
		int direction = buf.readVarInt();
		ItemStack stack = inMain? player.getMainHandStack() : player.getOffHandStack();
		if (stack.getItem() instanceof SporePouchItem) { //TODO: necessary on any other items? convert to iface?
			((SporePouchItem) stack.getItem()).shift(stack, direction);
		}
	}
}
