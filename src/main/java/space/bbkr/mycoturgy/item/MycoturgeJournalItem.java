package space.bbkr.mycoturgy.item;

import space.bbkr.mycoturgy.Mycoturgy;
import vazkii.patchouli.api.PatchouliAPI;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MycoturgeJournalItem extends Item {
	public MycoturgeJournalItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient && user instanceof ServerPlayerEntity) {
			PatchouliAPI.instance.openBookGUI((ServerPlayerEntity) user, new Identifier(Mycoturgy.MODID, "mycoturge_journal"));
			return TypedActionResult.success(user.getStackInHand(hand));
		}
		return TypedActionResult.consume(user.getStackInHand(hand));
	}

}
