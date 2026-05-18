package edu.ccrm.web.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class StaticFileHandler implements HttpHandler {
    private static final String PUBLIC_DIR = "public";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (!"GET".equals(method)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String path = exchange.getRequestURI().getPath();
        if (path.equals("/") || path.isEmpty()) {
            path = "/index.html";
        }

        File file = new File(PUBLIC_DIR + path);
        if (!file.exists() || file.isDirectory()) {
            // Check if fallback to index.html works (for SPA routing, though we won't strictly need it if we use hash routing)
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        String contentType = guessContentType(file.getName());
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, file.length());

        try (OutputStream os = exchange.getResponseBody()) {
            Files.copy(file.toPath(), os);
        }
    }

    private String guessContentType(String filename) {
        if (filename.endsWith(".html")) return "text/html";
        if (filename.endsWith(".css")) return "text/css";
        if (filename.endsWith(".js")) return "application/javascript";
        if (filename.endsWith(".png")) return "image/png";
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
        if (filename.endsWith(".svg")) return "image/svg+xml";
        if (filename.endsWith(".json")) return "application/json";
        return "application/octet-stream";
    }
}
