package Handlers;

import java.io.*;
import java.nio.file.*;

import java.net.HttpURLConnection;
import com.sun.net.httpserver.*;


public class AFileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

            String urlPath = exchange.getRequestURI().toString();

            if (exchange.getRequestURI().toString().equals("/") || exchange.getRequestURI() == null) {
                urlPath = "/index.html";
            }

            String filePath = "web" + urlPath;
            File foundFile = new File(filePath);
            File notFoundFile = new File("web/HTML/404.html");

            if(foundFile.exists()) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                Files.copy(foundFile.toPath(), respBody);
                exchange.close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                OutputStream respBody = exchange.getResponseBody();
                Files.copy(notFoundFile.toPath(), respBody);
                exchange.close();
            }

            exchange.getResponseBody().close();
        }
    }
}
