package fr.iglee42.resourcefulshulkers.utils;

import net.minecraft.util.FastColor;

import java.util.concurrent.TimeUnit;

public final class RSColors {

    public static MutableColor getRGBColor() {
        if (RGB != null) return RGB;
        MutableColor color = new MutableColor(255, 255, 0, 0);
            Scheduling.schedule(() -> {
                if (color.getRed() > 0 && color.getBlue() == 0) {
                    color.setRed(color.getRed() - 1);
                    color.setGreen(color.getGreen() + 1);
                }
                if (color.getGreen() > 0 && color.getRed() == 0) {
                    color.setGreen(color.getGreen() - 1);
                    color.setBlue(color.getBlue() + 1);
                }
                if (color.getBlue() > 0 && color.getGreen() == 0) {
                    color.setBlue(color.getBlue() - 1);
                    color.setRed(color.getRed() + 1);
                }
            }, 0, 20, TimeUnit.MILLISECONDS);
        RGB = color;
        return RGB;
    }

    public static class MutableColor {

        private int alpha, red, green, blue;

        public MutableColor(int alpha, int red, int green, int blue) {
            this.alpha = alpha;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

        public int getRed() {
            return red;
        }

        public void setRed(int red) {
            this.red = red;
        }

        public int getGreen() {
            return green;
        }

        public void setGreen(int green) {
            this.green = green;
        }

        public int getBlue() {
            return blue;
        }

        public void setBlue(int blue) {
            this.blue = blue;
        }

        public float getAlphaFloat() {
            return alpha / 255f;
        }

        public float getRedFloat() {
            return red / 255f;
        }

        public float getGreenFloat() {
            return green / 255f;
        }

        public float getBlueFloat() {
            return blue / 255f;
        }

        public int toARGB() {
            return FastColor.ARGB32.color(alpha, red, green, blue);
        }
    }

    private static MutableColor RGB;


}
