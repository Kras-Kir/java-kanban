package serv;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    // Основной метод для успешного ответа
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }


    // Метод для ответа "Не найдено" (404)
    protected void sendNotFound(HttpExchange h) throws IOException {
        String response = "{\"error\":\"Not found\"}";
        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    // Метод для ответа о конфликте задач (406)
    protected void sendHasInteractions(HttpExchange h) throws IOException {
        String response = "{\"error\":\"Task time conflict\"}";
        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}
