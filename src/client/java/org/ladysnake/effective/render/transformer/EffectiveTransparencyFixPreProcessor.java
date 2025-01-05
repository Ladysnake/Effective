package org.ladysnake.effective.render.transformer;

import foundry.veil.api.client.render.shader.processor.ShaderPreProcessor;
import foundry.veil.api.glsl.node.GlslNode;
import foundry.veil.api.glsl.node.GlslNodeList;
import foundry.veil.api.glsl.node.GlslTree;
import foundry.veil.api.glsl.node.branch.GlslSelectionNode;
import foundry.veil.api.glsl.node.expression.GlslCompareNode;
import foundry.veil.api.glsl.node.primary.GlslFloatConstantNode;
import net.minecraft.util.Identifier;

/**
 * A pre-processor intended to verify the Minecraft particle transparency check isn't active.
 * <p>
 * Intended to transform:
 * <pre>{@code
 * if (color.a < 0.1) {
 * 	discard;
 * }
 * }
 *
 * Into the following:
 * <pre>{@code
 * if (color.a < 0.0000001) {
 * 	discard;
 * }
 * }
 *
 * Inside of any fragment shader named <code>particle</code>.
 *
 * @author RyanHCode
 */
public class EffectiveTransparencyFixPreProcessor implements ShaderPreProcessor {

	public static final float NEW_TRANSPARENCY_THRESHOLD = 0.0000001f;

	@Override
	public void modify(final Context ctx, final GlslTree tree) {
		final Identifier name = ctx.name();
		assert name != null;

		if (!ctx.isSourceFile()) return;
		if (!name.getNamespace().equals("minecraft") || !name.getPath().equals("shaders/core/particle.fsh")) return;

		final GlslNodeList mainFunctionBody = tree.mainFunction().orElseThrow().getBody();
		assert mainFunctionBody != null : "Main function body is null";

		for (final GlslNode node : mainFunctionBody) {
			if (node instanceof GlslSelectionNode selectionNode && selectionNode.getExpression() instanceof GlslCompareNode compareNode) {
				// We know there's an if statement (selectionNode) with a comparison inside it
				// If the second part of the comparison is 0.1, and the comparison type is "less than", we can assume it's the transparency check

				if (compareNode.getOperand() == GlslCompareNode.Operand.LESS &&
					compareNode.getSecond() instanceof GlslFloatConstantNode floatConstant &&
					Math.abs(floatConstant.floatValue() - 0.1) < 0.0001) { // Arbitrary epsilon
					compareNode.setSecond(new GlslFloatConstantNode(NEW_TRANSPARENCY_THRESHOLD));
				}
			}
		}
	}

}
