package org.example;

import com.fastcgi.FCGIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Main {
    private record ValidationError(String value, String message) {
    }

    private record UserData(double x, double y, double r) {
    }

    static final Gson gson = new GsonBuilder().create();

    private static final double[] availableRValues = {1.0, 1.5, 2, 2.5, 3.0};

    private static Result<UserData, ValidationError> validateRequest(String content) {
        UserData userData;
        String xRepresentation;
        String yRepresentation;

        try {
            userData = gson.fromJson(content, UserData.class);
            JsonObject t = gson.fromJson(content, JsonObject.class);

            xRepresentation = t.get("x").getAsString();
            yRepresentation = t.get("y").getAsString();
        } catch (JsonSyntaxException e) {
            return Result.error(new ValidationError("json", e.getMessage()));
        }

        final double xAbsoluteValue = Math.abs(userData.x);
        final double yAbsoluteValue = Math.abs(userData.y);

        if (xAbsoluteValue > 3 || xRepresentation.matches("[+-]?3[.]0*[1-9]+\\d*")) {
            return Result.error(new ValidationError("x", "x must be in range [-3, 3]"));
        }

        if (yAbsoluteValue > 5 || yRepresentation.matches("[+-]?5[.]0*[1-9]+\\d*")) {
            return Result.error(new ValidationError("y", "y must be in range [-5, 5]"));
        }

        for (double r : availableRValues) {
            if (Math.abs(userData.r - r) < 1e-9) {
                return Result.ok(new UserData(userData.x, userData.y, r));
            }
        }

        return Result.error(new ValidationError("r", "r must be in [1.0, 1.5, 2.0, 2.5, 3.0]"));
    }

    private static HttpResponse errorResponse(ValidationError error) {
        return new HttpResponse(
                HttpVersion.HTTP_2_0,
                400,
                "Bad Request",
                "application/json",
                gson.toJson(error)
        );
    }

    private static HttpResponse formBadResponse(String content) {
        return errorResponse(new ValidationError("bad response", content));
    }

    private static HttpResponse successResponse(UserData data, boolean isInArea, long executionTimeNS) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("x", data.x);
        jsonObject.addProperty("y", data.y);
        jsonObject.addProperty("r", data.r);
        jsonObject.addProperty("isInArea", isInArea);
        jsonObject.addProperty("executionTimeNS", executionTimeNS);

        try {
            return new HttpResponse(
                    HttpVersion.HTTP_2_0,
                    200,
                    "OK",
                    "application/json",
                    gson.toJson(jsonObject)
            );
        } catch (JsonSyntaxException jsonProcessingException) {
            throw new IllegalStateException("Failed to serialize error response", jsonProcessingException);
        }
    }

    private static HttpResponse formResponse(String content) {
        long begin = System.nanoTime();

        Result<UserData, ValidationError> validationResult = validateRequest(content);

        if (validationResult.isError()) {
            return errorResponse(validationResult.getError());
        }

        UserData userData = validationResult.getValue();
        boolean isInArea = AreaChecker.isInArea(userData.x, userData.y, userData.r);

        long end = System.nanoTime();

        return successResponse(userData, isInArea, end - begin);
    }

    private static String readRequestBody() throws IOException, BadRequest {
        FCGIInterface.request.inStream.fill();
        var contentLength = FCGIInterface.request.inStream.available();

        var buffer = ByteBuffer.allocate(contentLength);
        var readBytes =
                FCGIInterface.request.inStream.read(buffer.array(), 0,
                        contentLength);

        var requestBodyRaw = new byte[readBytes];
        buffer.get(requestBodyRaw);
        buffer.clear();

        if (!FCGIInterface.request.params.get("REQUEST_METHOD").equals("POST")) {
            throw new BadRequest("POST method expected");
        }

        return new String(requestBodyRaw, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws IOException {
        var fcgiInterface = new FCGIInterface();

        while (fcgiInterface.FCGIaccept() >= 0) {
            try {
                System.out.println(formResponse(readRequestBody()));
            } catch (BadRequest badRequest) {
                System.out.println(formBadResponse(badRequest.getMessage()));
            }
        }
    }
}