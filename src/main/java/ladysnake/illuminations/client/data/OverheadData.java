package ladysnake.illuminations.client.data;

import ladysnake.illuminations.client.Illuminations;
import ladysnake.illuminations.client.render.entity.model.hat.OverheadModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class OverheadData {
	private final Function<EntityRendererFactory.Context, OverheadModel> model;
	private final Identifier texture;

	public OverheadData(Function<EntityRendererFactory.Context, OverheadModel> model, String textureName) {
		this.model = model;
		this.texture = new Identifier(Illuminations.MODID, "textures/entity/" + textureName + ".png");
	}

	public OverheadModel createModel(EntityRendererFactory.Context ctx) {
		return model.apply(ctx);
	}

	public Identifier getTexture() {
		return texture;
	}
}
