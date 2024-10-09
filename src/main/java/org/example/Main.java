package org.example;

import com.fastcgi.FCGIInterface;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.example.RequestValidator.gson;
import static org.example.RequestValidator.validateRequest;

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
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("x", data.x());
        jsonObject.addProperty("y", data.y());
        jsonObject.addProperty("r", data.r());
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
        } catch (Throwable error) {
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