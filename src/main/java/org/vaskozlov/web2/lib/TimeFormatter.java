package org.vaskozlov.web2.lib;

public class TimeFormatter {
    public static String formatExecutionTime(long timeNs) {
        final double timeUs = timeNs / 1000.0;
        final double timeMs = timeUs / 1000.0;
        final double timeS = timeMs / 1000.0;

        if (timeS > 1) {
            return String.format("%.2f s", timeS);
        }

        if (timeMs > 1) {
            return String.format("%.2f ms", timeMs);
        }

        if (timeUs > 1) {
            return String.format("%.2f us", timeUs);
        }

        return String.format("%d ns", timeNs);
    }
}
