package Handlers;

import SerializeDeserialize.*;
import Services.Clear;
import XPOJOS.Response.ClearResponse;

import java.io.*;

import java.net.HttpURLConnection;

import com.sun.net.httpserver.*;

public class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {

                Clear clearService = new Clear();
                ClearResponse response = clearService.clearAll();


                if(response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                String json = Serializer.serialize(response);
                OutputStream os = exchange.getResponseBody();
                ReadWrite.writeString(json, os);

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

            exchange.getResponseBody().close();
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
