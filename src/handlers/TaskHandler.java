package handlers;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Task;
import serv.BaseHttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
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
        if (path.equals("/tasks")) {
            List<Task> tasks = taskManager.getTasks();
            sendText(h, gson.toJson(tasks));
        } else if (path.matches("/tasks/\\d+")) {
            int id = Integer.parseInt(path.substring(7));
            Task task = taskManager.taskById(id);
            if (task != null) {
                sendText(h, gson.toJson(task));
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
            Task task = gson.fromJson(body, Task.class);

            if (task.getId() == 0) {
                taskManager.addTask(task);
                sendText(h, "{\"result\":\"Task created\"}");
            } else {
                taskManager.updateTask(task);
                sendText(h, "{\"result\":\"Task updated\"}");
            }
        } catch (JsonSyntaxException e) {
            sendText(h, "{\"error\":\"Invalid JSON\"}");
        }
    }


    private void handleDeleteRequest(HttpExchange h, String path) throws IOException {
        if (path.matches("/tasks/\\d+")) {
            int id = Integer.parseInt(path.substring(7));
            Task task = taskManager.taskById(id);
            if (task != null) {
                taskManager.deleteByIdTask(id);
                sendText(h, "{\"result\":\"Task deleted\"}");
            } else {
                sendNotFound(h);
            }
        } else {
            sendNotFound(h);
        }
    }
}
