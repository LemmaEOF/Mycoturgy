package space.bbkr.mycoturgy.item;

import space.bbkr.mycoturgy.client.journal.JournalGui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MycoturgeJournalItem extends Item {
	public MycoturgeJournalItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (world.isClient) {
			user.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 1.0f, 1.0f);
			MinecraftClient.getInstance().openScreen(JournalGui.getInstance());
//			PatchouliAPI.instance.openBookGUI((ServerPlayerEntity) user, new Identifier(Mycoturgy.MODID, "mycoturge_journal"));
			return TypedActionResult.success(user.getStackInHand(hand));
		}
		return TypedActionResult.consume(user.getStackInHand(hand));
	}

}
