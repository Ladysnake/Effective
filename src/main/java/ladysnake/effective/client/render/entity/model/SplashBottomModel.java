package ladysnake.effective.client.render.entity.model;

import ladysnake.effective.client.Effective;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class SplashBottomModel<T extends Entity> extends EntityModel<T> {
    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(Effective.MODID, "splash_bottom"), "main");
    private final ModelPart splash;

    public SplashBottomModel(ModelPart root) {
        this.splash = root.getChild("splash");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild("splash", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F), ModelTransform.pivot(0.0F, 0.0f, 0.0F));

        return TexturedModelData.of(modelData, 48, 28);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        splash.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}
