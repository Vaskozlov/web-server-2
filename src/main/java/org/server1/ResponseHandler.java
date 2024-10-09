package org.server1;

import com.google.gson.JsonObject;

import java.lang.reflect.Field;

import static org.server1.Reflect.getDoubleFromObjectField;
import static org.server1.RequestValidator.gson;
import static org.server1.RequestValidator.validateRequest;

public class ResponseHandler {
    public static HttpResponse errorResponse(int statusCode, ValidationError error) {
        return new HttpResponse(
                HttpVersion.HTTP_2_0,
                statusCode,
                error.message(),
                "application/json",
                gson.toJson(error)
        );
    }

    public static HttpResponse formBadResponse(int statusCode, String message, String content) {
        return errorResponse(statusCode, new ValidationError(message, content));
    }

    public static HttpResponse successResponse(DataFromUser data, boolean isInArea, long executionTimeNS) {
        try {
            JsonObject jsonObject = new JsonObject();

            for (Field field : data.getClass().getDeclaredFields()) {
                jsonObject.addProperty(field.getName(), getDoubleFromObjectField(field, data));
            }

            jsonObject.addProperty("isInArea", isInArea);
            jsonObject.addProperty("executionTimeNS", executionTimeNS);

            return new HttpResponse(
                    HttpVersion.HTTP_2_0,
                    200,
                    "OK",
                    "application/json",
                    gson.toJson(jsonObject)
            );
        } catch (Exception error) {
            return formBadResponse(500, "Failed to serialize", error.getMessage());
        }
    }

    public static HttpResponse formResponse(String content) {
        long begin = System.nanoTime();

        Result<DataFromUser, ValidationError> validationResult = validateRequest(content);

        if (validationResult.isError()) {
            return errorResponse(418, validationResult.getError());
        }

        DataFromUser userData = validationResult.getValue();
        boolean isInArea = IsAreaChecker.isInArea(userData.x(), userData.y(), userData.r());

        long end = System.nanoTime();

        return successResponse(userData, isInArea, end - begin);
    }
}
