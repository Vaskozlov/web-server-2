package org.example;

import com.fastcgi.FCGIInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Main {
    private record ValidationError(String value, String message) {
    }

    private record UserData(double x, double y, double r) {
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final double[] availableRValues = {1.0, 1.5, 2, 2.5, 3.0};

    private static Result<UserData, ValidationError> validateRequest(String content) {
        UserData userData;
        String xRepresentation;
        String yRepresentation;

        try {
            JsonNode rootNode = objectMapper.readTree(content);
            xRepresentation = rootNode.get("x").asText();
            yRepresentation = rootNode.get("y").asText();
            userData = objectMapper.readValue(content, UserData.class);
        } catch (JsonProcessingException e) {
            return Result.error(new ValidationError("json", e.getMessage()));
        }

        final double xAbsoluteValue = Math.abs(userData.x);
        final double yAbsoluteValue = Math.abs(userData.y);

        xRepresentation = "0" + xRepresentation.replaceFirst("[^.,]*", "");
        yRepresentation = "0" + yRepresentation.replaceFirst("[^.,]*", "");

        if (xAbsoluteValue > 3 || (xAbsoluteValue == 3 && Math.abs(Double.parseDouble(xRepresentation)) > 0.0)) {
            return Result.error(new ValidationError("x", "x must be in range [-3, 3]"));
        }

        if (yAbsoluteValue > 5 || (yAbsoluteValue == 5 && Math.abs(Double.parseDouble(yRepresentation)) > 0.0)) {
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
        try {
            return new HttpResponse(
                    HttpVersion.HTTP_2_0,
                    400,
                    "Bad Request",
                    "application/json",
                    objectMapper.writeValueAsString(error)
            );
        } catch (JsonProcessingException jsonProcessingException) {
            throw new IllegalStateException("Failed to serialize error response", jsonProcessingException);
        }
    }

    private static HttpResponse formBadResponse(String content) {
        return errorResponse(new ValidationError("bad response", content));
    }

    private static HttpResponse successResponse(UserData data, boolean isInArea, long executionTimeNS) {
        ObjectNode rootNode = objectMapper.createObjectNode();

        rootNode.put("x", data.x);
        rootNode.put("y", data.y);
        rootNode.put("r", data.r);
        rootNode.put("isInArea", isInArea);
        rootNode.put("executionTimeNS", executionTimeNS);

        try {
            return new HttpResponse(
                    HttpVersion.HTTP_2_0,
                    200,
                    "OK",
                    "application/json",
                    objectMapper.writeValueAsString(rootNode)
            );
        } catch (JsonProcessingException jsonProcessingException) {
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