package gay.lemmaeof.mycoturgy.client.render;

import gay.lemmaeof.mycoturgy.block.MasonJarBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Vec3d;
import gay.lemmaeof.mycoturgy.block.entity.CookingPotBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class CookingPotBlockEntityRenderer implements BlockEntityRenderer<CookingPotBlockEntity> {
	private static final List<Vec3d> OFFSETS = new ArrayList<>();
	private final BlockEntityRendererFactory.Context context;

	public CookingPotBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
		this.context = context;
	}

	@Override
	public void render(CookingPotBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		for (int i = 0; i < entity.getInventory().size(); i++) {
			renderStack(OFFSETS.get(i), entity.getInventory().getStack(i), entity, tickDelta, matrices, vertexConsumers, light, overlay);
		}
		renderStack(Vec3d.ZERO, entity.getOutputInventory().getStack(), entity, tickDelta, matrices, vertexConsumers, light, overlay);
	}

	private void renderStack(Vec3d offset, ItemStack stack, CookingPotBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (!stack.isEmpty()) {
			matrices.push();
			matrices.translate(0.5D, -0.2D, 0.5D);
			double y = offset.y;
			if (entity.getCachedState().get(MasonJarBlock.FILLED)) {
				y += Math.sin((tickDelta + entity.ticks) / 32) * 0.05;
				matrices.multiply(Axis.Y_POSITIVE.rotationDegrees((tickDelta + entity.ticks) / 1.5f));
			}
			matrices.translate(offset.x, y, offset.z);
			matrices.scale(0.35f, 0.35f, 0.35f);
			context.getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, null, 0);
			matrices.pop();
		}
	}

	static {
		OFFSETS.add(new Vec3d(2/16D, 0D, 2/16D));
		OFFSETS.add(new Vec3d(-2/16D, 0D, -2/16D));
		OFFSETS.add(new Vec3d(-2/16D, 0D, 2/16D));
		OFFSETS.add(new Vec3d(2/16D, 0D, -2/16D));
		OFFSETS.add(new Vec3d(0D, 0D, 4/16D));
		OFFSETS.add(new Vec3d(0D, 0D, -4/16D));
	}
}
