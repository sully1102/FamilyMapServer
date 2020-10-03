package Handlers;

import SerializeDeserialize.*;
import Services.Fill;
import XPOJOS.Response.*;

import java.io.*;

import java.net.HttpURLConnection;
import com.sun.net.httpserver.*;

public class FillHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {

                Fill fillService = new Fill();
                FillResponse response = new FillResponse();


                String uri = exchange.getRequestURI().toString();
                StringBuilder url = new StringBuilder(uri);
                url.deleteCharAt(0);
                String[] path = url.toString().split("/");


                String username = path[1];
                int numGenerations = 101;

                if(path.length == 3) {
                    numGenerations = Integer.parseInt(path[2]);
                }


                response = fillService.fill(username, numGenerations);


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
        } catch(IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
