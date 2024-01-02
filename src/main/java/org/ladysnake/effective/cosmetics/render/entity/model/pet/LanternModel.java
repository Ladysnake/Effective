/// Made with Model Converter by Globox_Z
/// Generate all required imports
/// Made with Blockbench 3.8.4
/// Exported for Minecraft version 1.15
/// Paste this class into your mod and generate all required imports
package org.ladysnake.effective.cosmetics.render.entity.model.pet;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.ladysnake.effective.cosmetics.EffectiveCosmetics;
import org.ladysnake.effective.cosmetics.render.GlowyRenderLayer;

public class LanternModel extends Model {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(EffectiveCosmetics.MODID, "lantern"), "main");

	private final ModelPart lantern;

	public LanternModel(ModelPart root) {
		super(GlowyRenderLayer::get);
		this.lantern = root.getChild("lantern");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		modelPartData.addChild("lantern", ModelPartBuilder.create()
				.uv(0, 0)
				.cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 7.0F, 6.0F)
				.uv(0, 13)
				.cuboid(-2.0F, -5.0F, -2.0F, 4.0F, 2.0F, 4.0F)
				.uv(16, 13)
				.cuboid(-2.5F, -8.0F, 0.0F, 5.0F, 4.0F, 0.0F),
			ModelTransform.pivot(0.0F, 16.0F, 0.0F)
		);
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		lantern.render(matrixStack, buffer, packedLight, packedOverlay);
	}
}
