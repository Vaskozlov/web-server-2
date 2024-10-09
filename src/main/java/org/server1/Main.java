package org.server1;

import com.fastcgi.FCGIInterface;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.server1.Reflect.getDoubleFromObjectField;
import static org.server1.RequestValidator.gson;
import static org.server1.RequestValidator.validateRequest;

public class Main {
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
        return errorResponse(new ValidationError("bad request", content));
    }

    private static HttpResponse successResponse(DataFromUser data, boolean isInArea, long executionTimeNS) {
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
            throw new IllegalStateException("Failed to serialize object to json", error);
        }
    }

    private static HttpResponse formResponse(String content) {
        long begin = System.nanoTime();

        Result<DataFromUser, ValidationError> validationResult = validateRequest(content);

        if (validationResult.isError()) {
            return errorResponse(validationResult.getError());
        }

        DataFromUser userData = validationResult.getValue();
        boolean isInArea = IsAreaChecker.isInArea(userData.x(), userData.y(), userData.r());

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