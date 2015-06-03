package com.datayes.magnifying;

/**
 * Created by quandong.li on 2015/6/2.
 */
public class MagnifyingLayoutConfiguration {

    public static final int DEFAULT_GLASS_WIDTH = 600;

    public static final int DEFAULT_GLASS_HEIGHT = 200;

    public static final int DEFAULT_PADDING = 150;

    public static final float DEFAULT_SCALE_FACTOR = 1.3f;

    /**
     * magnify glass width
     */
    final int glassWidth;
    /**
     * magnify glass height
     */
    final int glassHeight;
    /**
     * magnify glass padding bottom with touch point
     */
    final int padding;
    /**
     * magnify glass content scale factor
     */
    final float scaleFactor;

    private MagnifyingLayoutConfiguration(Builder builder) {
        glassWidth = builder.glassWidth;
        glassHeight = builder.glassHeight;
        padding = builder.padding;
        scaleFactor = builder.scaleFactor;
    }

    public static MagnifyingLayoutConfiguration createDefault() {
        return new Builder().build();
    }

    public static class Builder {
        private int glassWidth = DEFAULT_GLASS_WIDTH;
        private int glassHeight = DEFAULT_GLASS_HEIGHT;
        private int padding = DEFAULT_PADDING;
        private float scaleFactor = DEFAULT_SCALE_FACTOR;

        public Builder() {

        }
        public Builder setGlassWidth(int width) {
            glassWidth = width;
            return this;
        }
        public Builder setGlassHeight(int height) {
            glassHeight = height;
            return this;
        }
        public Builder setGlassPadding(int padding) {
            this.padding = padding;
            return this;
        }
        public Builder setScale(float scale) {
            scaleFactor = scale;
            return this;
        }
        public MagnifyingLayoutConfiguration build() {
            return new MagnifyingLayoutConfiguration(this);
        }
    }
}
