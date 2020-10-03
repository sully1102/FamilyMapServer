package Handlers;

import XPOJOS.Request.RegisterRequest;
import XPOJOS.Response.RegisterResponse;
import SerializeDeserialize.*;
import Services.Register;

import java.io.*;

import java.net.HttpURLConnection;
import com.sun.net.httpserver.*;

public class RegisterHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                Register registerService = new Register();
                RegisterResponse response = new RegisterResponse();

                InputStream reqBody = exchange.getRequestBody();
                String reqData = ReadWrite.readString(reqBody);


                RegisterRequest registerRequest = Deserializer.deserialize(reqData, RegisterRequest.class);
                response = registerService.register(registerRequest);


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
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
