package org.vaskozlov.web2.lib;

public record ResponseResult(
        double x,
        double y,
        double r,
        boolean isInArea,
        long executionTime
) {
}
