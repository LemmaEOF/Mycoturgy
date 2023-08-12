package gay.lemmaeof.mycoturgy.mixin.client;

import gay.lemmaeof.mycoturgy.init.MycoturgyItems;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.util.math.Axis;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer {

	@Shadow protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);

	@Shadow protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);


	@Shadow @Final private MinecraftClient client;


	@Shadow public abstract void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode modelTransformationMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

	//copying in a decent amount of the crossbow first-person render code so the pipe Looks Good in first person!
	//TODO: make it not dip when changing NBT for puff puff pass
	@Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
	private void injectPipeRenderer(AbstractClientPlayerEntity player,
									float tickDelta,
									float pitch,
									Hand hand,
									float swingProgress,
									ItemStack item,
									float equipProgress,
									MatrixStack matrices,
									VertexConsumerProvider vertexConsumers,
									int light,
									CallbackInfo info) {
		if (!player.isUsingSpyglass() && item.isOf(MycoturgyItems.SPOREBRUSH_PIPE)) {
			matrices.push();
			boolean isMainHand = hand == Hand.MAIN_HAND;
			Arm arm = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
			boolean inRightArm = arm == Arm.RIGHT;
			int transformFactor = inRightArm ? 1 : -1;

			if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
				this.applyEquipOffset(matrices, arm, 0);
				matrices.translate((float)transformFactor * -0.7 /*-0.4785682F*/, -0.094387F, 0.05731531F);
				matrices.multiply(Axis.X_POSITIVE.rotationDegrees(-11.935F));
				matrices.multiply(Axis.Y_POSITIVE.rotationDegrees((float)transformFactor * 65.3F));
				matrices.multiply(Axis.Z_POSITIVE.rotationDegrees((float)transformFactor * -9.785F));
				float useTimeLeft = (float)item.getMaxUseTime() - ((float)client.player.getItemUseTimeLeft() - tickDelta + 1.0F);
				float pullPercent = useTimeLeft / (float) CrossbowItem.getPullTime(item);
				if (pullPercent > 1.0F) {
					pullPercent = 1.0F;
				}

				if (pullPercent > 0.1F) {
					float useFactor = MathHelper.sin((useTimeLeft - 0.1F) * 1.3F);
					float adjusted = pullPercent - 0.1F;
					float yOffset = useFactor * adjusted;
					matrices.translate(0, yOffset * 0.004F,  0);
				}

				matrices.translate(0, 0, pullPercent * 0.04F);
				matrices.scale(1.0F, 1.0F, 1.0F + pullPercent * 0.2F);
				matrices.multiply(Axis.Y_NEGATIVE.rotationDegrees((float)transformFactor * 45.0F));
			} else {
				float xOffset = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
				float yOffset = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) (Math.PI * 2));
				float zOffset = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
				matrices.translate((float)transformFactor * xOffset, yOffset, zOffset);
				applyEquipOffset(matrices, arm, equipProgress);
				applySwingOffset(matrices, arm, equipProgress);
			}


			renderItem(
					player,
					item,
					inRightArm ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
					!inRightArm,
					matrices,
					vertexConsumers,
					light
			);

			matrices.pop();
			info.cancel();
		}
	}
}
