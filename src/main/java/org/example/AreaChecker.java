package org.example;

public class AreaChecker {
    public static boolean isInArea(double x, double y, double r) {
        final double halfR = r / 2;

        if (x >= 0 && y >= 0) {
            return (x <= r && y <= halfR - x / 2);
        }

        if (x >= 0 && y < 0) {
            return (x <= r && -y <= halfR);
        }

        if (x < 0 && y <= 0) {
            return Math.sqrt(x * x + y * y) <= halfR;
        }

        return false;
    }
}
