package org.example;

public class AreaChecker {
    private static boolean isInFirstQuarter(double x, double y, double r) {
        final double halfR = r / 2;

        if (x >= 0 && y >= 0) {
            return (x <= r && y <= halfR - x / 2);
        }

        return false;
    }

    private static boolean inInFourthQuarter(double x, double y, double r) {
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
        if (isInFirstQuarter(x, y, r)) {
            return true;
        }

        if (isInThirdQuarter(x, y, r)) {
            return true;
        }

        return inInFourthQuarter(x, y, r);
    }
}
