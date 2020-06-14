package space.bbkr.mycoturge.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.bbkr.mycoturge.Mycoturge;
import space.bbkr.mycoturge.component.HaustorComponent;

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
			if (cookingTimes[i] % 80 == 0 && itemsBeingCooked.get(i).getItem() == Mycoturge.SPORE_BUNDLE) {
				HaustorComponent component = Mycoturge.HAUSTOR_COMPONENT.get(this.world.getChunk(this.getPos()));
				//TODO: different effect four soul campfire?
				component.setHypha(component.getHypha() + 1);
			}
		}
	}
}
