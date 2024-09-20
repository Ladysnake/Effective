package org.ladysnake.effective.cosmetics.render.entity.model.hat;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.ladysnake.effective.cosmetics.EffectiveCosmetics;

public class HaloModel extends OverheadModel {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Identifier.of(EffectiveCosmetics.MODID, "halo"), "main");

	public HaloModel(EntityRendererFactory.Context ctx) {
		super(ctx, MODEL_LAYER);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData modelPartData1 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 7).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(-4.0f)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData1.addChild("halo", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -11.0F, 5.0F, 16.0F, 16.0F, 0.0F), ModelTransform.pivot(0.0F, -4.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 48);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		head.render(matrices, vertices, light, overlay);
	}

	public void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}
