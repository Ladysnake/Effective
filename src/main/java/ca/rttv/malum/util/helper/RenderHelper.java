package ca.rttv.malum.util.helper;

import ca.rttv.malum.util.ShaderHolder;
import ca.rttv.malum.util.TrailPoint;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class RenderHelper {
	public static final int FULL_BRIGHT = 15728880;

	public static ShaderProgram getShader(RenderLayer type) {
		if (type instanceof RenderLayer.MultiPhase multiPhase) {
			Optional<Supplier<ShaderProgram>> shader = multiPhase.phases.shader.supplier;
			if (shader.isPresent()) {
				return shader.get().get();
			}
		}
		return null;
	}

	public static void blit(MatrixStack stack, ShaderHolder shader, int x, int y, double width, double height, float u, float v, float xCanvasSize, float yCanvasSize) {
		innerBlit(stack, shader, x, y, width, height, u / xCanvasSize, v / yCanvasSize, (float) width / xCanvasSize, (float) height / yCanvasSize);
	}

	public static void blit(MatrixStack stack, ShaderHolder shader, int x, int y, double width, double height, float u, float v, float uWidth, float vHeight, float xCanvasSize, float yCanvasSize) {
		innerBlit(stack, shader, x, y, width, height, u / xCanvasSize, v / yCanvasSize, uWidth / xCanvasSize, vHeight / yCanvasSize);
	}

	public static void blit(MatrixStack stack, ShaderHolder shader, int x, int y, double width, double height, float u, float v, float canvasSize) {
		innerBlit(stack, shader, x, y, width, height, u / canvasSize, v / canvasSize, (float) (x + width) / canvasSize, (float) (y + height) / canvasSize);
	}

	public static void blit(MatrixStack stack, ShaderHolder shader, int x, int y, double width, double height, float u, float v, float uWidth, float vHeight, float canvasSize) {
		innerBlit(stack, shader, x, y, width, height, u / canvasSize, v / canvasSize, uWidth / canvasSize, vHeight / canvasSize);
	}

	public static void blit(MatrixStack stack, ShaderHolder shader, int x, int y, double width, double height, float r, float g, float b, float a, float u, float v, float xCanvasSize, float yCanvasSize) {
		innerBlit(stack, shader, x, y, width, height, r, g, b, a, u / xCanvasSize, v / yCanvasSize, (float) width / xCanvasSize, (float) height / yCanvasSize);
	}

	public static void blit(MatrixStack stack, ShaderHolder shader, int x, int y, double width, double height, float r, float g, float b, float a, float u, float v, float uWidth, float vHeight, float xCanvasSize, float yCanvasSize) {
		innerBlit(stack, shader, x, y, width, height, r, g, b, a, u / xCanvasSize, v / yCanvasSize, uWidth / xCanvasSize, vHeight / yCanvasSize);
	}

	public static void blit(MatrixStack stack, ShaderHolder shader, int x, int y, double width, double height, float r, float g, float b, float a, float u, float v, float canvasSize) {
		innerBlit(stack, shader, x, y, width, height, r, g, b, a, u / canvasSize, v / canvasSize, (float) width / canvasSize, (float) height / canvasSize);
	}

	public static void blit(MatrixStack stack, ShaderHolder shader, int x, int y, double width, double height, float r, float g, float b, float a, float u, float v, float uWidth, float vHeight, float canvasSize) {
		innerBlit(stack, shader, x, y, width, height, r, g, b, a, u / canvasSize, v / canvasSize, uWidth / canvasSize, vHeight / canvasSize);
	}

	public static void innerBlit(MatrixStack stack, ShaderHolder shader, int x, int y, double width, double height, float r, float g, float b, float a, float u, float v, float uWidth, float vHeight) {
		Matrix4f last = stack.peek().getModel();
		RenderSystem.setShader(shader.getInstance());
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBufferBuilder();
		bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		bufferbuilder.vertex(last, (float) x, (float) y + (float) height, 0).color(r, g, b, a).uv(u, v + vHeight).next();
		bufferbuilder.vertex(last, (float) x + (float) width, (float) y + (float) height, 0).color(r, g, b, a).uv(u + uWidth, v + vHeight).next();
		bufferbuilder.vertex(last, (float) x + (float) width, (float) y, 0).color(r, g, b, a).uv(u + uWidth, v).next();
		bufferbuilder.vertex(last, (float) x, (float) y, 0).color(r, g, b, a).uv(u, v).next();
		BufferRenderer.drawWithShader(bufferbuilder.end());
	}

	public static void innerBlit(MatrixStack stack, ShaderHolder shader, int x, int y, double width, double height, float u, float v, float uWidth, float vHeight) {
		Matrix4f last = stack.peek().getModel();
		RenderSystem.setShader(shader.getInstance());
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBufferBuilder();
		bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferbuilder.vertex(last, (float) x, (float) y + (float) height, 0).uv(u, v + vHeight).next();
		bufferbuilder.vertex(last, (float) x + (float) width, (float) y + (float) height, 0).uv(u + uWidth, v + vHeight).next();
		bufferbuilder.vertex(last, (float) x + (float) width, (float) y, 0).uv(u + uWidth, v).next();
		bufferbuilder.vertex(last, (float) x, (float) y, 0).uv(u, v).next();
		BufferRenderer.drawWithShader(bufferbuilder.end());
	}

	public static void blit(MatrixStack stack, int x, int y, double width, double height, float u, float v, float xCanvasSize, float yCanvasSize) {
		innerBlit(stack, x, y, width, height, u / xCanvasSize, v / yCanvasSize, (float) width / xCanvasSize, (float) height / yCanvasSize);
	}

	public static void blit(MatrixStack stack, int x, int y, double width, double height, float u, float v, float uWidth, float vHeight, float xCanvasSize, float yCanvasSize) {
		innerBlit(stack, x, y, width, height, u / xCanvasSize, v / yCanvasSize, uWidth / xCanvasSize, vHeight / yCanvasSize);
	}

	public static void blit(MatrixStack stack, int x, int y, double width, double height, float u, float v, float canvasSize) {
		innerBlit(stack, x, y, width, height, u / canvasSize, v / canvasSize, (float) width / canvasSize, (float) height / canvasSize);
	}

	public static void blit(MatrixStack stack, int x, int y, double width, double height, float u, float v, float uWidth, float vHeight, float canvasSize) {
		innerBlit(stack, x, y, width, height, u / canvasSize, v / canvasSize, uWidth / canvasSize, vHeight / canvasSize);
	}

	public static void blit(MatrixStack stack, int x, int y, double width, double height, float r, float g, float b, float a, float u, float v, float xCanvasSize, float yCanvasSize) {
		innerBlit(stack, x, y, width, height, r, g, b, a, u / xCanvasSize, v / yCanvasSize, (float) width / xCanvasSize, (float) height / yCanvasSize);
	}

	public static void blit(MatrixStack stack, int x, int y, double width, double height, float r, float g, float b, float a, float u, float v, float uWidth, float vHeight, float xCanvasSize, float yCanvasSize) {
		innerBlit(stack, x, y, width, height, r, g, b, a, u / xCanvasSize, v / yCanvasSize, uWidth / xCanvasSize, vHeight / yCanvasSize);
	}

	public static void blit(MatrixStack stack, int x, int y, double width, double height, float r, float g, float b, float a, float u, float v, float canvasSize) {
		innerBlit(stack, x, y, width, height, r, g, b, a, u / canvasSize, v / canvasSize, (float) width / canvasSize, (float) height / canvasSize);
	}

	public static void blit(MatrixStack stack, int x, int y, double width, double height, float r, float g, float b, float a, float u, float v, float uWidth, float vHeight, float canvasSize) {
		innerBlit(stack, x, y, width, height, r, g, b, a, u / canvasSize, v / canvasSize, uWidth / canvasSize, vHeight / canvasSize);
	}

	public static void innerBlit(MatrixStack stack, int x, int y, double width, double height, float r, float g, float b, float a, float u, float v, float uWidth, float vHeight) {
		Matrix4f last = stack.peek().getModel();
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBufferBuilder();
		bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		bufferbuilder.vertex(last, (float) x, (float) y + (float) height, 0).color(r, g, b, a).uv(u, v + vHeight).next();
		bufferbuilder.vertex(last, (float) x + (float) width, (float) y + (float) height, 0).color(r, g, b, a).uv(u + uWidth, v + vHeight).next();
		bufferbuilder.vertex(last, (float) x + (float) width, (float) y, 0).color(r, g, b, a).uv(u + uWidth, v).next();
		bufferbuilder.vertex(last, (float) x, (float) y, 0).color(r, g, b, a).uv(u, v).next();
		BufferRenderer.draw(bufferbuilder.end());
	}

	public static void innerBlit(MatrixStack stack, int x, int y, double width, double height, float u, float v, float uWidth, float vHeight) {
		Matrix4f last = stack.peek().getModel();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBufferBuilder();
		bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferbuilder.vertex(last, (float) x, (float) y + (float) height, 0).uv(u, v + vHeight).next();
		bufferbuilder.vertex(last, (float) x + (float) width, (float) y + (float) height, 0).uv(u + uWidth, v + vHeight).next();
		bufferbuilder.vertex(last, (float) x + (float) width, (float) y, 0).uv(u + uWidth, v).next();
		bufferbuilder.vertex(last, (float) x, (float) y, 0).uv(u, v).next();
		BufferRenderer.draw(bufferbuilder.end());
	}

	public static VertexBuilder create() {
		return new VertexBuilder();
	}

	public static class VertexBuilder {
		float r = 1, g = 1, b = 1, a = 1;
		float xOffset = 0, yOffset = 0, zOffset = 0;
		int light = FULL_BRIGHT;
		float u0 = 0, v0 = 0, u1 = 1, v1 = 1;


		public VertexBuilder setColor(Color color) {
			return setColor(color.getRed(), color.getGreen(), color.getBlue());
		}

		public VertexBuilder setColor(Color color, float a) {
			return setColor(color).setAlpha(a);
		}

		public VertexBuilder setColor(float r, float g, float b, float a) {
			return setColor(r, g, b).setAlpha(a);
		}

		public VertexBuilder setColor(float r, float g, float b) {
			this.r = r / 255f;
			this.g = g / 255f;
			this.b = b / 255f;
			return this;
		}

		public VertexBuilder setAlpha(float a) {
			this.a = a;
			return this;
		}

		public VertexBuilder setOffset(float xOffset, float yOffset, float zOffset) {
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.zOffset = zOffset;
			return this;
		}

		public VertexBuilder setLight(int light) {
			this.light = light;
			return this;
		}

		public VertexBuilder setUV(float u0, float v0, float u1, float v1) {
			this.u0 = u0;
			this.v0 = v0;
			this.u1 = u1;
			this.v1 = v1;
			return this;
		}

		public VertexBuilder renderTriangle(VertexConsumer vertexConsumer, MatrixStack stack, float size) {
			return renderTriangle(vertexConsumer, stack, size, size);
		}

		public VertexBuilder renderTriangle(VertexConsumer vertexConsumer, MatrixStack stack, float width, float height) {
			Matrix4f last = stack.peek().getModel();
			vertexPosColorUVLight(vertexConsumer, last, -width, -height, 0, r, g, b, a, 0, 1, light);
			vertexPosColorUVLight(vertexConsumer, last, width, -height, 0, r, g, b, a, 1, 1, light);
			vertexPosColorUVLight(vertexConsumer, last, 0, height, 0, r, g, b, a, 0.5f, 0, light);
			return this;
		}


		public VertexBuilder renderTrail(VertexConsumer vertexConsumer, MatrixStack stack, List<Vector4f> trailSegments, Function<Float, Float> widthFunc) {
			return renderTrail(vertexConsumer, stack.peek().getModel(), trailSegments, widthFunc);
		}

		public VertexBuilder renderTrail(VertexConsumer vertexConsumer, Matrix4f pose, List<Vector4f> trailSegments, Function<Float, Float> widthFunc) {
			if (trailSegments.size() < 3) {
				return this;
			}
			for (Vector4f pos : trailSegments) {
				pos.add(xOffset, yOffset, zOffset, 0);
				pos.transform(pose);
			}

			int count = trailSegments.size() - 1;
			float increment = 1.0F / (count - 1);
			ArrayList<TrailPoint> points = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				float width = widthFunc.apply(increment * i);
				Vector4f start = trailSegments.get(i);
				Vector4f end = trailSegments.get(i + 1);
				Vector4f mid = midpoint(start, end);
				Vec2f offset = corners(start, end, width);
				Vector4f positions = new Vector4f(mid.getX() + offset.x, mid.getX() - offset.x, mid.getY() + offset.y, mid.getY() - offset.y);
				points.add(new TrailPoint(positions.getX(), positions.getY(), positions.getZ(), positions.getW(), mid.getZ()));
			}
			return renderPoints(vertexConsumer, points, u0, v0, u1, v1);
		}

		public VertexBuilder renderPoints(VertexConsumer vertexConsumer, List<TrailPoint> trailPoints, float u0, float v0, float u1, float v1) {
			int count = trailPoints.size() - 1;
			float increment = 1.0F / count;
			trailPoints.get(0).renderStart(vertexConsumer, light, r, g, b, a, u0, v0, u1, MathHelper.lerp(increment, v0, v1));
			for (int i = 1; i < count; i++) {
				float current = MathHelper.lerp(i * increment, v0, v1);
				trailPoints.get(i).renderMid(vertexConsumer, light, r, g, b, a, u0, current, u1, current);
			}
			trailPoints.get(count).renderEnd(vertexConsumer, light, r, g, b, a, u0, MathHelper.lerp((count) * increment, v0, v1), u1, v1);
			return this;
		}

		public VertexBuilder renderBeam(VertexConsumer vertexConsumer, MatrixStack stack, Vec3d start, Vec3d end, float width) {
			MinecraftClient minecraft = MinecraftClient.getInstance();
			start.add(xOffset, yOffset, zOffset);
			end.add(xOffset, yOffset, zOffset);
			stack.translate(-start.x, -start.y, -start.z);
			Vec3d cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPos();
			Vec3d delta = end.subtract(start);
			Vec3d normal = start.subtract(cameraPosition).crossProduct(delta).normalize().multiply(width / 2f, width / 2f, width / 2f);
			Matrix4f last = stack.peek().getModel();
			Vec3d[] positions = new Vec3d[]{start.subtract(normal), start.add(normal), end.add(normal), end.subtract(normal)};
			vertexPosColorUVLight(vertexConsumer, last, (float) positions[0].x, (float) positions[0].y, (float) positions[0].z, r, g, b, a, u0, v1, light);
			vertexPosColorUVLight(vertexConsumer, last, (float) positions[1].x, (float) positions[1].y, (float) positions[1].z, r, g, b, a, u1, v1, light);
			vertexPosColorUVLight(vertexConsumer, last, (float) positions[2].x, (float) positions[2].y, (float) positions[2].z, r, g, b, a, u1, v0, light);
			vertexPosColorUVLight(vertexConsumer, last, (float) positions[3].x, (float) positions[3].y, (float) positions[3].z, r, g, b, a, u0, v0, light);
			stack.translate(start.x, start.y, start.z);
			return this;
		}

		public VertexBuilder renderQuad(VertexConsumer vertexConsumer, MatrixStack stack, float size) {
			return renderQuad(vertexConsumer, stack, size, size);
		}

		public VertexBuilder renderQuad(VertexConsumer vertexConsumer, MatrixStack stack, float width, float height) {
			Vec3f[] positions = new Vec3f[]{new Vec3f(-width, -height, 0), new Vec3f(width, -height, 0), new Vec3f(width, height, 0), new Vec3f(-width, height, 0)};
			return renderQuad(vertexConsumer, stack, positions, width, height);
		}

		public VertexBuilder renderQuad(VertexConsumer vertexConsumer, MatrixStack stack, Vec3f[] positions, float size) {
			return renderQuad(vertexConsumer, stack, positions, size, size);
		}

		public VertexBuilder renderQuad(VertexConsumer vertexConsumer, MatrixStack stack, Vec3f[] positions, float width, float height) {
			Matrix4f last = stack.peek().getModel();
			stack.translate(xOffset, yOffset, zOffset);
			for (Vec3f position : positions) {
				position.multiplyComponentwise(width, height, width);
			}
			vertexPosColorUVLight(vertexConsumer, last, positions[0].getX(), positions[0].getY(), positions[0].getZ(), r, g, b, a, u0, v1, light);
			vertexPosColorUVLight(vertexConsumer, last, positions[1].getX(), positions[1].getY(), positions[1].getZ(), r, g, b, a, u1, v1, light);
			vertexPosColorUVLight(vertexConsumer, last, positions[2].getX(), positions[2].getY(), positions[2].getZ(), r, g, b, a, u1, v0, light);
			vertexPosColorUVLight(vertexConsumer, last, positions[3].getX(), positions[3].getY(), positions[3].getZ(), r, g, b, a, u0, v0, light);
			stack.translate(-xOffset, -yOffset, -zOffset);
			return this;
		}

		public VertexBuilder renderSphere(VertexConsumer vertexConsumer, MatrixStack stack, float radius, int longs, int lats) {
			Matrix4f last = stack.peek().getModel();
			float startU = 0;
			float startV = 0;
			float endU = MathHelper.PI * 2;
			float endV = MathHelper.PI;
			float stepU = (endU - startU) / longs;
			float stepV = (endV - startV) / lats;
			for (int i = 0; i < longs; ++i) {
				// U-points
				for (int j = 0; j < lats; ++j) {
					// V-points
					float u = i * stepU + startU;
					float v = j * stepV + startV;
					float un = (i + 1 == longs) ? endU : (i + 1) * stepU + startU;
					float vn = (j + 1 == lats) ? endV : (j + 1) * stepV + startV;
					Vec3f p0 = parametricSphere(u, v, radius);
					Vec3f p1 = parametricSphere(u, vn, radius);
					Vec3f p2 = parametricSphere(un, v, radius);
					Vec3f p3 = parametricSphere(un, vn, radius);

					float textureU = u / endU * radius;
					float textureV = v / endV * radius;
					float textureUN = un / endU * radius;
					float textureVN = vn / endV * radius;
					vertexPosColorUVLight(vertexConsumer, last, p0.getX(), p0.getY(), p0.getZ(), r, g, b, a, textureU, textureV, light);
					vertexPosColorUVLight(vertexConsumer, last, p2.getX(), p2.getY(), p2.getZ(), r, g, b, a, textureUN, textureV, light);
					vertexPosColorUVLight(vertexConsumer, last, p1.getX(), p1.getY(), p1.getZ(), r, g, b, a, textureU, textureVN, light);

					vertexPosColorUVLight(vertexConsumer, last, p3.getX(), p3.getY(), p3.getZ(), r, g, b, a, textureUN, textureVN, light);
					vertexPosColorUVLight(vertexConsumer, last, p1.getX(), p1.getY(), p1.getZ(), r, g, b, a, textureU, textureVN, light);
					vertexPosColorUVLight(vertexConsumer, last, p2.getX(), p2.getY(), p2.getZ(), r, g, b, a, textureUN, textureV, light);
				}
			}
			return this;
		}
	}

	public static void vertexPos(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z) {
		vertexConsumer.vertex(last, x, y, z).next();
	}

	public static void vertexPosUV(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float u, float v) {
		vertexConsumer.vertex(last, x, y, z).uv(u, v).next();
	}

	public static void vertexPosUVLight(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float u, float v, int light) {
		vertexConsumer.vertex(last, x, y, z).uv(u, v).light(light).next();
	}

	public static void vertexPosColor(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a) {
		vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).next();
	}

	public static void vertexPosColorUV(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a, float u, float v) {
		vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).uv(u, v).next();
	}

	public static void vertexPosColorUVLight(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a, float u, float v, int light) {
		vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).uv(u, v).light(light).next();
	}

	public static Vec3f parametricSphere(float u, float v, float r) {
		return new Vec3f(MathHelper.cos(u) * MathHelper.sin(v) * r, MathHelper.cos(v) * r, MathHelper.sin(u) * MathHelper.sin(v) * r);
	}

	public static Vec2f corners(Vector4f start, Vector4f end, float width) {
		float x = -start.getX();
		float y = -start.getY();
		float z = Math.abs(start.getZ());
		if (z <= 0) {
			x += end.getX();
			y += end.getY();
		} else if (z > 0) {
			float ratio = end.getZ() / start.getZ();
			x = end.getX() + x * ratio;
			y = end.getY() + y * ratio;
		}
		if (start.getZ() > 0) {
			x = -x;
			y = -y;
		}
		float distance = DataHelper.distance(x, y);
		if (distance > 0F) {
			float normalize = width * 0.5F / (float) Math.sqrt(distance);
			x *= normalize;
			y *= normalize;
		}
		return new Vec2f(-y, x);
	}

	public static Vector4f midpoint(Vector4f a, Vector4f b) {
		return new Vector4f((a.getX() + b.getX()) * 0.5F, (a.getY() + b.getY()) * 0.5F, (a.getZ() + b.getZ()) * 0.5F, (a.getW() + b.getW()) * 0.5F);
	}
}
