package org.ladysnake.effective.cosmetics.render.entity.model.hat;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.ladysnake.effective.cosmetics.EffectiveCosmetics;

public class CrownModel extends OverheadModel {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Identifier.of(EffectiveCosmetics.MODID, "crown"), "main");

	public CrownModel(EntityRendererFactory.Context ctx) {
		super(ctx, MODEL_LAYER);

		ModelPart crown = this.head.getChild("crown");
		ModelPart south_r1 = crown.getChild("south_r1");
		ModelPart east_r1 = crown.getChild("east_r1");
		ModelPart north_r1 = crown.getChild("north_r1");
		ModelPart west_r1 = crown.getChild("west_r1");
		setRotationAngle(east_r1, -0.2618F, 1.5708F, 0.0F);
		setRotationAngle(north_r1, -0.2618F, 3.1416F, 0.0F);
		setRotationAngle(west_r1, -0.2618F, -1.5708F, 0.0F);
		setRotationAngle(south_r1, -0.2618F, 0.0F, 0.0F);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData modelPartData1 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 7).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(-4.0f)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData modelPartData2 = modelPartData1.addChild("crown", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -13.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(-0.5f)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));
		modelPartData2.addChild("west_r1", ModelPartBuilder.create().uv(7, 39).cuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, new Dilation(-0.5F)), ModelTransform.pivot(0.0F, -7.0F, 0.0F));
		modelPartData2.addChild("north_r1", ModelPartBuilder.create().uv(7, 15).cuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, new Dilation(-0.5F)), ModelTransform.pivot(0.0F, -7.0F, 0.0F));
		modelPartData2.addChild("east_r1", ModelPartBuilder.create().uv(7, 23).cuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, new Dilation(-0.5F)), ModelTransform.pivot(0.0F, -7.0F, 0.0F));
		modelPartData2.addChild("south_r1", ModelPartBuilder.create().uv(7, 31).cuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, new Dilation(-0.5F)), ModelTransform.pivot(0.0F, -7.0F, 0.0F));
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
