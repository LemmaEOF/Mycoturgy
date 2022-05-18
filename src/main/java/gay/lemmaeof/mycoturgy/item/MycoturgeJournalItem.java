package gay.lemmaeof.mycoturgy.item;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import gay.lemmaeof.mycoturgy.Mycoturgy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;

public class MycoturgeJournalItem extends Item {
	public MycoturgeJournalItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient) {
			user.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 1.0f, 1.0f);
//			MinecraftClient.getInstance().setScreen(JournalGui.getInstance());
			PatchouliAPI.get().openBookGUI((ServerPlayerEntity) user, new Identifier(Mycoturgy.MODID, "mycoturge_journal"));
			return TypedActionResult.success(user.getStackInHand(hand));
		}
		return TypedActionResult.consume(user.getStackInHand(hand));
	}

}
