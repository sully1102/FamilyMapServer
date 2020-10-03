package Handlers;

import XPOJOS.Request.LoginRequest;
import XPOJOS.Response.LoginResponse;
import SerializeDeserialize.*;
import Services.Login;

import java.io.*;

import java.net.HttpURLConnection;
import com.sun.net.httpserver.*;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {

                Login loginService = new Login();
                LoginResponse response = new LoginResponse();

                InputStream reqBody = exchange.getRequestBody();
                String reqData = ReadWrite.readString(reqBody);


                LoginRequest loginRequest = Deserializer.deserialize(reqData, LoginRequest.class);
                response = loginService.login(loginRequest);


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

