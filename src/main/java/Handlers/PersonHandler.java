package Handlers;

import SerializeDeserialize.*;
import Services.PersonID;
import Services.PersonAll;
import XPOJOS.Response.*;

import java.io.*;

import java.net.HttpURLConnection;
import com.sun.net.httpserver.*;

public class PersonHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("GET")) {
                if(exchange.getRequestHeaders().containsKey("Authorization")) {

                    PersonIDResponse responseID = new PersonIDResponse();
                    PersonAllResponse responseAll = new PersonAllResponse();

                    PersonID personIDService = new PersonID();
                    PersonAll personAllService = new PersonAll();


                    String authID = exchange.getRequestHeaders().getFirst("Authorization");


                    String uri = exchange.getRequestURI().toString();
                    StringBuilder url = new StringBuilder(uri);
                    url.deleteCharAt(0);
                    String[] paths = url.toString().split("/");


                    if(paths.length == 1) {

                        responseAll = personAllService.getPeople(authID);

                        if(responseAll.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        String json = Serializer.serialize(responseAll);
                        OutputStream os = exchange.getResponseBody();
                        ReadWrite.writeString(json, os);

                    } else if(paths.length == 2){

                        String personID = paths[1];
                        responseID = personIDService.getPerson(personID, authID);

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
