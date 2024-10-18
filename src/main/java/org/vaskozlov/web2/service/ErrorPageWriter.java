package org.vaskozlov.web2.service;

import java.io.PrintWriter;

public class ErrorPageWriter {
    public static void writeErrorPage(String pageHeader, String message, PrintWriter writer) {
        writer.write("""
                <!DOCTYPE html>
                <html>
                    <body>
                    <h1>
                        Error in %s
                    </h1>
                    <p>
                        %s
                    </p>
                    </body>
                </html>
                """.formatted(pageHeader, message)
        );
    }
}
