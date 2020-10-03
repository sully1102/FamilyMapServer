package FamilyMapServer;

import Handlers.*;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

public class Server {

    private HttpServer server;

    public Server() {

    }

    public static void main(String[] args) throws IOException { //where does the 404 come in
        Server server = new Server();
        server.startServer(8080);
    }
    public void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer httpServer = HttpServer.create(serverAddress, 12);
        registerHandlers(httpServer);
        httpServer.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    public void registerHandlers(HttpServer httpServer) {
        httpServer.createContext("/", new AFileHandler());
        httpServer.createContext("/user/register", new RegisterHandler());
        httpServer.createContext("/user/login", new LoginHandler());
        httpServer.createContext("/load", new LoadHandler());
        httpServer.createContext("/clear", new ClearHandler());
        httpServer.createContext("/fill", new FillHandler());
        httpServer.createContext("/person", new PersonHandler());
        httpServer.createContext("/event", new EventHandler());
    }
}
