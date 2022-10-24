package ca.rttv.malum.util.handler;

import ca.rttv.malum.util.ExtendedShader;
import ca.rttv.malum.util.RenderLayers;
import ca.rttv.malum.util.ShaderUniformHandler;
import ca.rttv.malum.util.helper.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.quiltmc.loader.api.QuiltLoader;

import java.util.HashMap;

public class RenderHandler {
	public static final HashMap<RenderLayer, BufferBuilder> BUFFERS = new HashMap<>();
	public static final HashMap<RenderLayer, ShaderUniformHandler> HANDLERS = new HashMap<>();
	//https://youtu.be/A3eHgip6DQM
	public static VertexConsumerProvider.Immediate DELAYED_RENDER = VertexConsumerProvider.immediate(BUFFERS, new BufferBuilder(QuiltLoader.isModLoaded("sodium") ? 262144 : 256));
	public static Matrix4f PARTICLE_MATRIX = null;
	public static Frustum FRUSTUM;

	public static void init() {

	}

	public static void renderLast(MatrixStack stack) {
		//prepareFrustum(stack, MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos(), MinecraftClient.getInstance().gameRenderer.getBasicProjectionMatrix(MinecraftClient.getInstance().options.fov));
		stack.push();
		RenderSystem.getModelViewStack().push();
		RenderSystem.getModelViewStack().loadIdentity();
		if (PARTICLE_MATRIX != null) {
			RenderSystem.getModelViewStack().multiplyMatrix(PARTICLE_MATRIX);
		}
		RenderSystem.applyModelViewMatrix();
		DELAYED_RENDER.draw(RenderLayers.ADDITIVE_PARTICLE);
		DELAYED_RENDER.draw(RenderLayers.ADDITIVE_BLOCK_PARTICLE);
		RenderSystem.getModelViewStack().pop();
		RenderSystem.applyModelViewMatrix();
		for (RenderLayer type : BUFFERS.keySet()) {
			ShaderProgram instance = RenderHelper.getShader(type);
			if (HANDLERS.containsKey(type)) {
				ShaderUniformHandler handler = HANDLERS.get(type);
				handler.updateShaderData(instance);
			}
			DELAYED_RENDER.draw(type);
			if (instance instanceof ExtendedShader extendedShaderInstance) {
				extendedShaderInstance.setUniformDefaults();
			}
		}
		DELAYED_RENDER.draw();
		stack.pop();
	}

	public static void prepareFrustum(MatrixStack poseStack, Vec3d position, Matrix4f stack) {
		Matrix4f matrix4f = poseStack.peek().getModel();
		double d0 = position.getX();
		double d1 = position.getY();
		double d2 = position.getZ();
		FRUSTUM = new Frustum(matrix4f, stack);
		FRUSTUM.setPosition(d0, d1, d2);
	}
}
