package space.bbkr.mycoturgy.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.bbkr.mycoturgy.Mycoturgy;
import space.bbkr.mycoturgy.component.HaustorComponent;
import space.bbkr.mycoturgy.init.MycoturgyItems;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(CampfireBlockEntity.class)
public abstract class MixinCampfireBlockEntity extends BlockEntity {
	@Shadow @Final private DefaultedList<ItemStack> itemsBeingCooked;

	@Shadow @Final private int[] cookingTimes;

	public MixinCampfireBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tickMycoturgy(CallbackInfo info) {
		for (int i = 0; i < itemsBeingCooked.size(); i++) {
			if (cookingTimes[i] % 100 == 0 && itemsBeingCooked.get(i).getItem() == MycoturgyItems.SPORE_BUNDLE) {
				HaustorComponent component = Mycoturgy.HAUSTOR_COMPONENT.get(this.world.getChunk(this.getPos()));
				if (component.getPrimordia() > 2) {
					//TODO: different effect four soul campfire?
					component.changePrimordia(-2);
					component.changeHypha(1);
				}
			}
		}
	}
}
