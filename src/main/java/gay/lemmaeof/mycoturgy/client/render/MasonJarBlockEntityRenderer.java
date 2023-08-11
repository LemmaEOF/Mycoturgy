package gay.lemmaeof.mycoturgy.client.render;

import gay.lemmaeof.mycoturgy.block.MasonJarBlock;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import gay.lemmaeof.mycoturgy.block.entity.MasonJarBlockEntity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Vec3d;

public class MasonJarBlockEntityRenderer implements BlockEntityRenderer<MasonJarBlockEntity> {
	private final BlockEntityRendererFactory.Context context;
	public MasonJarBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
		this.context = context;
	}

	@Override
	public void render(MasonJarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		ItemStack stack = entity.getInventory().getStack();
		if (!stack.isEmpty()) {
			matrices.push();
			matrices.translate(0.5D, 0.2D, 0.5D);
			Vec3d offset = entity.getCachedState().getModelOffset(entity.getWorld(), entity.getPos());
			double y = offset.y;
			float rotation = entity.getAshes() * 22.5F;
			if (entity.getCachedState().get(MasonJarBlock.FILLED)) {
				y += Math.sin((tickDelta + entity.ticks) / 10) * 0.05;
				rotation += (tickDelta + entity.ticks) / 1.5F;
			}
			matrices.translate(offset.x, y, offset.z);
			matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(rotation));
			matrices.scale(0.35f, 0.35f, 0.35f);
			context.getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, null, 0);
			matrices.pop();
		}
	}
}
