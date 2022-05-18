package gay.lemmaeof.mycoturgy.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import gay.lemmaeof.mycoturgy.component.HaustorComponent;
import gay.lemmaeof.mycoturgy.init.MycoturgyComponents;
import gay.lemmaeof.mycoturgy.init.MycoturgyItems;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(CampfireBlockEntity.class)
public abstract class MixinCampfireBlockEntity extends BlockEntity {
	@Shadow @Final private DefaultedList<ItemStack> itemsBeingCooked;

	@Shadow @Final private int[] cookingTimes;

	public MixinCampfireBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Inject(method = "litServerTick", at = @At("TAIL"))
	private static void tickMycoturgy(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo info) {
		for (int i = 0; i < campfire.getItemsBeingCooked().size(); i++) {
			if (((MixinCampfireBlockEntity) (Object) campfire).cookingTimes[i] % 100 == 0 && ((MixinCampfireBlockEntity) (Object) campfire).itemsBeingCooked.get(i).getItem() == MycoturgyItems.SPORE_BUNDLE) {
				HaustorComponent component = MycoturgyComponents.HAUSTOR_COMPONENT.get(campfire.getWorld().getChunk(campfire.getPos()));
				if (component.getPrimordia() > 2) {
					//TODO: different effect four soul campfire?
					component.changePrimordia(-2);
					component.changeHypha(1);
				}
			}
		}
	}
}
