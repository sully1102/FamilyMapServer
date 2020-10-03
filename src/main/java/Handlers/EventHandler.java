package Handlers;

import SerializeDeserialize.*;
import Services.EventID;
import Services.EventAll;
import XPOJOS.Response.*;

import java.io.*;

import java.net.HttpURLConnection;
import com.sun.net.httpserver.*;

public class EventHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("GET")) {
                if(exchange.getRequestHeaders().containsKey("Authorization")) {

                    EventIDResponse responseID = new EventIDResponse();
                    EventAllResponse responseAll = new EventAllResponse();

                    EventID eventIDService = new EventID();
                    EventAll eventAllService = new EventAll();


                    String authID = exchange.getRequestHeaders().getFirst("Authorization");


                    String uri = exchange.getRequestURI().toString();
                    StringBuilder url = new StringBuilder(uri);
                    url.deleteCharAt(0);
                    String[] paths = url.toString().split("/");


                    if(paths.length == 1) {

                        responseAll = eventAllService.getEvents(authID);

                        if(responseAll.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        String json = Serializer.serialize(responseAll);
                        OutputStream os = exchange.getResponseBody();
                        ReadWrite.writeString(json, os);

                    } else if(paths.length == 2){

                        String eventID = paths[1];
                        responseID = eventIDService.getEvent(eventID, authID);

                        if(responseID.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        String json = Serializer.serialize(responseID);
                        OutputStream os = exchange.getResponseBody();
                        ReadWrite.writeString(json, os);

                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                }
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
