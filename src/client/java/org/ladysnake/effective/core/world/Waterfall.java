package org.ladysnake.effective.core.world;

import net.minecraft.util.math.BlockPos;

import java.awt.*;

public record Waterfall(BlockPos blockPos, float strength, boolean isSilent, Color mistColor) {
}
