package org.example;

public class IsAreaChecker {
    private static boolean isInFirstQuarter(double x, double y, double r) {
        final double halfR = r / 2;

        if (x >= 0 && y >= 0) {
            return (x <= r && y <= halfR - x / 2);
        }

        return false;
    }

    private static boolean isInFourthQuarter(double x, double y, double r) {
        final double halfR = r / 2;

        if (x >= 0 && y < 0) {
            return (x <= r && -y <= halfR);
        }

        return false;
    }

    private static boolean isInThirdQuarter(double x, double y, double r) {
        final double halfR = r / 2;

        if (x < 0 && y <= 0) {
            return Math.sqrt(x * x + y * y) <= halfR;
        }

        return false;
    }

    public static boolean isInArea(double x, double y, double r) {
        return isInFirstQuarter(x, y, r)
                || isInThirdQuarter(x, y, r)
                || isInFourthQuarter(x, y, r);
    }
}
