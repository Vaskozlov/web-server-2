package org.server1;

import java.nio.charset.StandardCharsets;

public record HttpResponse(
        HttpVersion version,
        int statusCode,
        String statusMessage,
        String contentType,
        String content
) {
    @Override
    public String toString() {
        return """
                %s %d %s
                Content-Type: %s
                Content-Length: %d
                
                
                %s
                
                
                """.formatted(
                version.toString(),
                statusCode,
                statusMessage,
                contentType,
                content.getBytes(StandardCharsets.UTF_8).length + 1,
                content
        );
    }
}