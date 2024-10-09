package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.util.regex.Pattern;

public class RequestValidator {
    public static final Gson gson = new GsonBuilder().create();

    private static final double DOUBLE_COMPARISON_ERROR = 1e-9;

    private static final Pattern PATTERN_FOR_X = Pattern.compile("[+-]?3[.]0*[1-9]+\\d*");

    private static final Pattern PATTERN_FOR_Y = Pattern.compile("[+-]?5[.]0*[1-9]+\\d*");

    private static final double[] availableRValues = {1.0, 1.5, 2, 2.5, 3.0};

    public static Result<DataFromUser, ValidationError> validateRequest(String content) {
        DataFromUser userData;
        String xRepresentation;
        String yRepresentation;

        try {
            userData = gson.fromJson(content, DataFromUser.class);
            JsonObject t = gson.fromJson(content, JsonObject.class);

            xRepresentation = t.get("x").getAsString();
            yRepresentation = t.get("y").getAsString();
        } catch (JsonSyntaxException e) {
            return Result.error(new ValidationError("json", e.getMessage()));
        }

        final double xAbsoluteValue = Math.abs(userData.x());
        final double yAbsoluteValue = Math.abs(userData.y());

        if (xAbsoluteValue > 3 || PATTERN_FOR_X.matcher(xRepresentation).matches()) {
            return Result.error(new ValidationError("x", "x must be in range [-3, 3]"));
        }

        if (yAbsoluteValue > 5 || PATTERN_FOR_Y.matcher(yRepresentation).matches()) {
            return Result.error(new ValidationError("y", "y must be in range [-5, 5]"));
        }

        for (double r : availableRValues) {
            if (Math.abs(userData.r() - r) <= DOUBLE_COMPARISON_ERROR) {
                return Result.ok(new DataFromUser(userData.x(), userData.y(), r));
            }
        }

        return Result.error(new ValidationError("r", "r must be in [1.0, 1.5, 2.0, 2.5, 3.0]"));
    }
}
