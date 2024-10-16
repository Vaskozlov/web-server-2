package org.vaskozlov.web2.service;

import org.vaskozlov.web2.lib.RequestValidationError;
import org.vaskozlov.web2.lib.Result;
import org.vaskozlov.web2.lib.TransformedRequestData;

import java.util.regex.Pattern;

public class RequestDataValidationService {
    private static final double DOUBLE_COMPARISON_ERROR = 1e-9;
    private static final double[] availableRValues = {1.0, 1.5, 2, 2.5, 3.0};

    private static final Pattern PATTERN_FOR_X = Pattern.compile("[+-]?3[.]0*[1-9]+\\d*");
    private static final Pattern PATTERN_FOR_Y = Pattern.compile("[+-]?5[.]0*[1-9]+\\d*");

    private static Result<Double, RequestValidationError> validateNumber(String value, String valueName) {
        try {
            return Result.ok(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            return Result.error(new RequestValidationError(valueName, valueName + " must be a number"));
        }
    }

    private static Result<Double, RequestValidationError> createMissingFieldError(String fieldName) {
        return Result.error(new RequestValidationError(fieldName, "Request must contain " + fieldName));
    }

    private static Result<Double, RequestValidationError> validateRequestField(String field, String fieldName) {
        if (field == null) {
            return createMissingFieldError(fieldName);
        }

        return validateNumber(field, fieldName);
    }

    public static Result<TransformedRequestData, RequestValidationError> validateRequestData(String x, String y, String r) {
        Result<Double, RequestValidationError> xResult = validateRequestField(x, "x");
        Result<Double, RequestValidationError> yResult = validateRequestField(y, "y");
        Result<Double, RequestValidationError> rResult = validateRequestField(r, "r");

        if (xResult.isError()) {
            return Result.error(xResult.getError());
        }

        if (yResult.isError()) {
            return Result.error(yResult.getError());
        }

        if (rResult.isError()) {
            return Result.error(rResult.getError());
        }

        double xValue = xResult.getValue();
        double yValue = yResult.getValue();
        double rValue = rResult.getValue();

        if (Math.abs(xValue) > 3 || PATTERN_FOR_X.matcher(x).matches()) {
            return Result.error(new RequestValidationError("x", "x must be in range [-3, 3]"));
        }

        if (Math.abs(yValue) > 5 || PATTERN_FOR_Y.matcher(y).matches()) {
            return Result.error(new RequestValidationError("y", "y must be in range [-5, 5]"));
        }

        for (double rVal : availableRValues) {
            if (Math.abs(rValue - rVal) <= DOUBLE_COMPARISON_ERROR) {
                return Result.ok(new TransformedRequestData(xValue, yValue, rValue));
            }
        }

        return Result.error(new RequestValidationError("r", "r must be in [1.0, 1.5, 2.0, 2.5, 3.0]"));
    }
}
