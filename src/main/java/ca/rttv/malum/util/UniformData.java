package ca.rttv.malum.util;

import com.mojang.blaze3d.shader.Uniform;

public class UniformData {
    public final String uniformName;
    public final int uniformType;

    public UniformData(String uniformName, int uniformType) {
        this.uniformName = uniformName;
        this.uniformType = uniformType;
    }

    public void setUniformValue(Uniform uniform) {

    }

    public static class FloatUniformData extends UniformData {
        public final float[] array;

        public FloatUniformData(String uniformName, int uniformType, float[] array) {
            super(uniformName, uniformType);
            this.array = array;
        }

        @Override
        public void setUniformValue(Uniform uniform) {
            if (uniformType <= 7) {
                uniform.setForDataType(array[0], array[1], array[2], array[3]);
            } else {
                uniform.setFloats(array);
            }
        }
    }

    public static class IntegerUniformData extends UniformData {
        public final int[] array;

        public IntegerUniformData(String uniformName, int uniformType, int[] array) {
            super(uniformName, uniformType);
            this.array = array;
        }

        @Override
        public void setUniformValue(Uniform uniform) {
            uniform.setForDataType(array[0], array[1], array[2], array[3]);
        }
    }
}