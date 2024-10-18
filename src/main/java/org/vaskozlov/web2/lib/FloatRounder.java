package org.vaskozlov.web2.lib;

public class FloatRounder {
    public static float round(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        float factor = (float) Math.pow(10, places);
        return Math.round(value * factor) / factor;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        double factor = Math.pow(10, places);
        return Math.round(value * factor) / factor;
    }
}
