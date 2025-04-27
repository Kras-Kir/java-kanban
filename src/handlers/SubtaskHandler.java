package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Subtask;
import serv.BaseHttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }


    public void handle(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String path = h.getRequestURI().getPath();

        switch (method) {
            case "GET":
                handleGetRequest(h, path);
                break;
            case "POST":
                handlePostRequest(h);
                break;
            case "DELETE":
                handleDeleteRequest(h, path);
                break;
            default:
                sendText(h, "{\"error\":\"Method not allowed\"}");
                h.sendResponseHeaders(405, -1);
        }
        h.close();
    }

    private void handleGetRequest(HttpExchange h, String path) throws IOException {
        if (path.equals("/subtasks")) {
            List<Subtask> subtasks = taskManager.getSubtasks();
            sendText(h, gson.toJson(subtasks));
        } else if (path.matches("/subtasks/\\d+")) {
            int id = Integer.parseInt(path.substring(10));
            Subtask subtask = taskManager.subtaskById(id);
            if (subtask != null) {
                sendText(h, gson.toJson(subtask));
            } else {
                sendNotFound(h);
            }
        } else {
            sendNotFound(h);
        }
    }

    private void handlePostRequest(HttpExchange h) throws IOException {
        try {
            String body = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(body, Subtask.class);

            if (subtask.getId() == 0) {
                taskManager.addSubtask(subtask);
                sendText(h, "{\"result\":\"Subtask created\", \"id\":" + subtask.getId() + "}");
            } else {
                taskManager.updateSubtask(subtask);
                sendText(h, "{\"result\":\"Subtask updated\"}");
            }
        } catch (JsonSyntaxException e) {
            sendText(h, "{\"error\":\"Invalid JSON\"}");
        }
    }

    private void handleDeleteRequest(HttpExchange h, String path) throws IOException {
        if (path.matches("/subtasks/\\d+")) {
            int id = Integer.parseInt(path.substring(10));
            Subtask subtask = taskManager.subtaskById(id);
            if (subtask != null) {
                taskManager.deleteByIdSubtask(id);
                sendText(h, "{\"result\":\"Subtask deleted\"}");
            } else {
                sendNotFound(h);
            }
        } else {
            sendNotFound(h);
        }
    }
}
