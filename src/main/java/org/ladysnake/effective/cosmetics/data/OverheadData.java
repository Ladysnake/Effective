package org.ladysnake.effective.cosmetics.data;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import org.ladysnake.effective.cosmetics.EffectiveCosmetics;
import org.ladysnake.effective.cosmetics.render.entity.model.hat.OverheadModel;

import java.util.function.Function;

public class OverheadData {
	private final Function<EntityRendererFactory.Context, OverheadModel> model;
	private final Identifier texture;

	public OverheadData(Function<EntityRendererFactory.Context, OverheadModel> model, String textureName) {
		this.model = model;
		this.texture = new Identifier(EffectiveCosmetics.MODID, "textures/entity/" + textureName + ".png");
	}

	public OverheadModel createModel(EntityRendererFactory.Context ctx) {
		return model.apply(ctx);
	}

	public Identifier getTexture() {
		return texture;
	}
}
