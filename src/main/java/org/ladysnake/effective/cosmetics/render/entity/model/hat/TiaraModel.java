package org.ladysnake.effective.cosmetics.render.entity.model.hat;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.ladysnake.effective.cosmetics.EffectiveCosmetics;

public class TiaraModel extends OverheadModel {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(EffectiveCosmetics.MODID, "tiara"), "main");

	public TiaraModel(EntityRendererFactory.Context ctx) {
		super(ctx, MODEL_LAYER);

		ModelPart tiara = this.head.getChild("tiara");
		ModelPart south_r1 = tiara.getChild("south_r1");
		ModelPart east_r1 = tiara.getChild("east_r1");
		ModelPart north_r1 = tiara.getChild("north_r1");
		ModelPart west_r1 = tiara.getChild("west_r1");
		setRotationAngle(east_r1, -0.0436F, 1.5708F, 0.0F);
		setRotationAngle(north_r1, -0.0873F, 3.1416F, 0.0F);
		setRotationAngle(west_r1, -0.0436F, -1.5708F, 0.0F);
		setRotationAngle(south_r1, -0.0873F, 0.0F, 0.0F);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData modelPartData1 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 7).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(-4.0f)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData modelPartData2 = modelPartData1.addChild("tiara", ModelPartBuilder.create().uv(0, 4).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 4.0F, 8.0F, new Dilation(-0.5F)).uv(0, 0), ModelTransform.pivot(0.0F, -4.0F, 0.0F));
		modelPartData2.addChild("west_r1", ModelPartBuilder.create().uv(6, 39).cuboid(-5.1F, -6.1F, 3.03F, 10.0F, 8.0F, 1.0F, new Dilation(-0.5F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));
		modelPartData2.addChild("north_r1", ModelPartBuilder.create().uv(8, 17).cuboid(0.0F, -4.3F, 3.2F, 6.0F, 6.0F, 1.0F, new Dilation(-0.5F)), ModelTransform.pivot(3.0F, -6.0F, 0.0F));
		modelPartData2.addChild("east_r1", ModelPartBuilder.create().uv(6, 23).cuboid(-4.9F, -6.1F, 3.03F, 10.0F, 8.0F, 1.0F, new Dilation(-0.5F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));
		modelPartData2.addChild("south_r1", ModelPartBuilder.create().uv(7, 32).cuboid(-4.0F, -6.3F, 3.2F, 8.0F, 8.0F, 1.0F, new Dilation(-0.75f)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 48);
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}
