package space.bbkr.mycoturgy.item;

import org.jetbrains.annotations.Nullable;
import space.bbkr.mycoturgy.inventory.SporePouchInventory;

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
		int slot = context.getStack().getOrCreateTag().getInt("CurrentSlot");
		SporePouchInventory inv = new SporePouchInventory();
		inv.readFromItem(context.getStack());
		ItemStack stack = inv.getStack(slot);
		System.out.println("Using stack " + stack +  " from slot " + slot);
		if (!stack.isEmpty()) {
			BlockHitResult res = new BlockHitResult(context.getHitPos(), context.getSide(), context.getBlockPos(), context.hitsInsideBlock());
			ItemUsageContext ctx = new FuckYouItemUsageContext(context.getWorld(), context.getPlayer(), context.getHand(), stack, res);
			return stack.useOnBlock(ctx);
		}
		return super.useOnBlock(context);
	}

	public void shift(ItemStack stack, int direction) {
		int current = stack.getOrCreateTag().getInt("CurrentSlot");
		current += direction;
		if (current == -1) current = 8;
		current = current % 9;
		stack.getTag().putInt("CurrentSlot", current);
		System.out.println("Scrolled stack " + stack + " in direction " + direction + " to slot " + current + "!");
	}

	private static class FuckYouItemUsageContext extends ItemUsageContext {

		public FuckYouItemUsageContext(World world, @Nullable PlayerEntity player, Hand hand, ItemStack stack, BlockHitResult hit) {
			super(world, player, hand, stack, hit);
		}
	}
}
