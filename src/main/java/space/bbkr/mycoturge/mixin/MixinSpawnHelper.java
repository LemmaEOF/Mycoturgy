package space.bbkr.mycoturge.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import space.bbkr.mycoturge.Mycoturge;
import space.bbkr.mycoturge.component.HaustorComponent;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.WorldView;

@Mixin(SpawnHelper.class)
public class MixinSpawnHelper {
	@Inject(method = "canSpawn(Lnet/minecraft/entity/SpawnRestriction$Location;Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/EntityType;)Z", at = @At("HEAD"), cancellable = true)
	private static void blockSpawns(SpawnRestriction.Location location, WorldView world, BlockPos pos, @Nullable EntityType<?> type, CallbackInfoReturnable<Boolean> info) {
		if (type != null && type.getSpawnGroup() == SpawnGroup.MONSTER) {
			HaustorComponent component = Mycoturge.HAUSTOR_COMPONENT.get(world.getChunk(pos));
			if (component.getLamella() >= 64) {
				info.setReturnValue(false);
			}
		}
	}
}
