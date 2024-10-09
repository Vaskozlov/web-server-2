package org.server1;

import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Main {
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
                System.out.println(RequestHandler.formResponse(readRequestBody()));
            } catch (BadRequest badRequest) {
                System.out.println(RequestHandler.formBadResponse(400, "Bad request", badRequest.getMessage()));
            }
        }
    }
}