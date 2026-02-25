package controller;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public interface HttpHandlerLogic {
    void handle(HttpExchange exchange) throws IOException;
    boolean authenticate(HttpExchange exchange) throws IOException;
    void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException;
}