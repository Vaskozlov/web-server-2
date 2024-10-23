package org.vaskozlov.web2.lib;

import lombok.*;

@Data
@AllArgsConstructor
public class ResponseResult {
    private double x;
    private double y;
    private double r;
    private boolean inArea;
    private long executionTimeNs;
}
