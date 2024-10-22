package org.vaskozlov.web2.lib;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult {
    private double x;
    private double y;
    private double r;
    private boolean inArea;
    private long executionTimeNs;
}
