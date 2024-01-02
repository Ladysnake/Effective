/// Made with Model Converter by Globox_Z
/// Generate all required imports
/// Made with Blockbench 3.8.4
/// Exported for Minecraft version 1.15
/// Paste this class into your mod and generate all required imports
package org.ladysnake.effective.cosmetics.render.entity.model.pet;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.ladysnake.effective.cosmetics.EffectiveCosmetics;
import org.ladysnake.effective.cosmetics.render.GlowyRenderLayer;

public class WillOWispModel extends Model {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(EffectiveCosmetics.MODID, "will_o_wisp"), "main");

	private final ModelPart skull;

	public WillOWispModel(ModelPart root) {
		super(GlowyRenderLayer::get);
		this.skull = root.getChild("skull");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("skull", ModelPartBuilder.create()
				.uv(0, 0)
				.cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F)
				.uv(0, 16)
				.cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 7.0F, 6.0F, new Dilation(0.25F)),
			ModelTransform.pivot(0.0F, 16.0F, 0.0F)
		);
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		skull.render(matrixStack, buffer, packedLight, packedOverlay);
	}
}
