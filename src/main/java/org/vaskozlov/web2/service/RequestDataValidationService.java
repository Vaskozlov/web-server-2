package org.vaskozlov.web2.service;

import lombok.Getter;
import org.vaskozlov.web2.lib.RequestValidationError;
import org.vaskozlov.web2.lib.Result;
import org.vaskozlov.web2.lib.ConvertedCheckParameters;

import java.util.Arrays;
import java.util.regex.Pattern;

public class RequestDataValidationService {
    private static final double DOUBLE_COMPARISON_ERROR = 1e-9;

    private static final String AVAILABLE_R_VALUES_STRING = "1.0, 1.5, 2.0, 2.5, 3.0";

    @Getter
    private static final double[] AVAILABLE_R_VALUES = Arrays.stream(AVAILABLE_R_VALUES_STRING.split(", "))
            .mapToDouble(Double::parseDouble)
            .toArray();

    private static final Pattern PATTERN_FOR_X = Pattern.compile("[+-]?3[.]0*[1-9]+\\d*");
    private static final Pattern PATTERN_FOR_Y = Pattern.compile("[+-]?5[.]0*[1-9]+\\d*");

    private static Result<Double, RequestValidationError> validateNumber(String value, String valueName) {
        try {
            return Result.ok(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            return Result.error(new RequestValidationError(
                    valueName,
                    "%s must be a number".formatted(valueName)
            ));
        }
    }

    private static Result<Double, RequestValidationError> createMissingFieldError(String fieldName) {
        return Result.error(new RequestValidationError(
                fieldName,
                "Request must contain %s".formatted(fieldName)
        ));
    }

    private static Result<Double, RequestValidationError> validateRequestField(String field, String fieldName) {
        if (field == null) {
            return createMissingFieldError(fieldName);
        }

        return validateNumber(field, fieldName);
    }

    public static Result<ConvertedCheckParameters, RequestValidationError> validateRequestData(String x, String y, String r) {
        final Result<Double, RequestValidationError> xResult = validateRequestField(x, "x");
        final Result<Double, RequestValidationError> yResult = validateRequestField(y, "y");
        final Result<Double, RequestValidationError> rResult = validateRequestField(r, "r");

        if (xResult.isError()) {
            return Result.error(xResult.getError());
        }

        if (yResult.isError()) {
            return Result.error(yResult.getError());
        }

        if (rResult.isError()) {
            return Result.error(rResult.getError());
        }

        final double xValue = xResult.getValue();
        final double yValue = yResult.getValue();
        final double rValue = rResult.getValue();

        for (double rVal : AVAILABLE_R_VALUES) {
            if (Math.abs(rValue - rVal) <= DOUBLE_COMPARISON_ERROR) {
                return Result.ok(new ConvertedCheckParameters(xValue, yValue, rValue));
            }
        }

        return Result.error(new RequestValidationError(
                "r",
                "r must be in [%s]".formatted(AVAILABLE_R_VALUES_STRING)
        ));
    }
}
