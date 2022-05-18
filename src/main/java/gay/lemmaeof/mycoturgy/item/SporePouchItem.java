package gay.lemmaeof.mycoturgy.item;

import org.jetbrains.annotations.Nullable;
import gay.lemmaeof.mycoturgy.inventory.SporePouchInventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class SporePouchItem extends Item {
	public SporePouchItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.getWorld().isClient) return super.useOnBlock(context);
		int slot = context.getStack().getOrCreateNbt().getInt("CurrentSlot");
		SporePouchInventory inv = new SporePouchInventory();
		inv.readFromItem(context.getStack());
		ItemStack stack = inv.getStack(slot);
		if (!stack.isEmpty()) {
			BlockHitResult res = new BlockHitResult(context.getHitPos(), context.getSide(), context.getBlockPos(), context.hitsInsideBlock());
			ItemUsageContext ctx = new FuckYouItemUsageContext(context.getWorld(), context.getPlayer(), context.getHand(), stack, res);
			return stack.useOnBlock(ctx);
		}
		return super.useOnBlock(context);
	}

	//TODO: trackpad-friendly shift method and not just alt-scroll
	public void shift(ItemStack stack, int direction) {
		int current = stack.getOrCreateNbt().getInt("CurrentSlot");
		current += direction;
		if (current == -1) current = 8;
		current = current % 9;
		stack.getNbt().putInt("CurrentSlot", current);
	}

	private static class FuckYouItemUsageContext extends ItemUsageContext {

		public FuckYouItemUsageContext(World world, @Nullable PlayerEntity player, Hand hand, ItemStack stack, BlockHitResult hit) {
			super(world, player, hand, stack, hit);
		}
	}
}
