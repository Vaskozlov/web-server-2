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
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static String formResponse(String content) throws JsonProcessingException {
        long begin = System.currentTimeMillis();
        JsonNode tree = objectMapper.readTree(content);
        double x = tree.get("x").asDouble();
        double y = tree.get("y").asDouble();
        double r = tree.get("r").asDouble();

        ObjectNode rootNode = objectMapper.createObjectNode();
        long end = System.currentTimeMillis();

        rootNode.put("isInArea", AreaChecker.isInArea(x, y, r));
        rootNode.put("executionTimeMS", end - begin);

        return new HttpResponse(
                HttpVersion.HTTP_1_1,
                200,
                "OK",
                "text/html",
                objectMapper.writeValueAsString(rootNode)
        ).toString();
    }

    private static String readRequestBody() throws IOException {
        FCGIInterface.request.inStream.fill();
        var contentLength = FCGIInterface.request.inStream.available();
        var buffer = ByteBuffer.allocate(contentLength);
        var readBytes =
                FCGIInterface.request.inStream.read(buffer.array(), 0,
                        contentLength);
        var requestBodyRaw = new byte[readBytes];
        buffer.get(requestBodyRaw);
        buffer.clear();
        return new String(requestBodyRaw, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws IOException {
        var fcgiInterface = new FCGIInterface();

        while (fcgiInterface.FCGIaccept() >= 0) {
            System.out.println(formResponse(readRequestBody()));
        }
    }
}