package org.ladysnake.effective.cosmetics.render.entity.model.hat;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import org.ladysnake.effective.cosmetics.render.GlowyRenderLayer;

public abstract class OverheadModel extends Model {
	public final ModelPart head;

	public OverheadModel(EntityRendererFactory.Context ctx, EntityModelLayer entityModelLayer) {
		super(GlowyRenderLayer::get);
		this.head = ctx.getPart(entityModelLayer).getChild("head");
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		this.head.render(matrices, vertices, light, overlay);
	}
}
