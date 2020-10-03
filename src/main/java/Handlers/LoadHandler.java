package Handlers;

import XPOJOS.Request.LoadRequest;
import XPOJOS.Response.LoadResponse;
import SerializeDeserialize.*;
import Services.Load;

import java.io.*;

import java.net.HttpURLConnection;
import com.sun.net.httpserver.*;

public class LoadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {

                Load loadService = new Load();
                LoadResponse response = new LoadResponse();


                InputStream reqBody = exchange.getRequestBody();
                String reqData = ReadWrite.readString(reqBody);


                LoadRequest loadRequest = Deserializer.deserialize(reqData, LoadRequest.class);
                response = loadService.load(loadRequest);


                if (response.isSuccess()) {
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
