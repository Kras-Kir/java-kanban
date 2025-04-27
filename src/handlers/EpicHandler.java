package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import serv.BaseHttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
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
        if (path.equals("/epics")) {
            // Получение всех эпиков
            List<Epic> epics = taskManager.getEpics();
            sendText(h, gson.toJson(epics));
        } else if (path.matches("/epics/\\d+")) {
            // Получение эпика по ID
            int id = Integer.parseInt(path.substring(6));
            Epic epic = taskManager.epicById(id);
            if (epic != null) {
                sendText(h, gson.toJson(epic));
            } else {
                sendNotFound(h);
            }
        } else if (path.matches("/epics/\\d+/subtasks")) {
            // Получение подзадач эпика
            int epicId = Integer.parseInt(path.split("/")[2]);
            List<Subtask> subtasks = taskManager.getEpicSubtask(epicId);
            sendText(h, gson.toJson(subtasks));
        } else {
            sendNotFound(h);
        }
    }

    private void handlePostRequest(HttpExchange h) throws IOException {
        try {
            String body = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(body, Epic.class);

            if (epic.getId() == 0) {
                taskManager.addEpic(epic);
                sendText(h, "{\"result\":\"Epic created\", \"id\":" + epic.getId() + "}");
            } else {
                taskManager.updateEpic(epic);
                sendText(h, "{\"result\":\"Epic updated\"}");
            }
        } catch (JsonSyntaxException e) {
            sendText(h, "{\"error\":\"Invalid JSON\"}");
        }
    }

    private void handleDeleteRequest(HttpExchange h, String path) throws IOException {
        if (path.matches("/epics/\\d+")) {
            int id = Integer.parseInt(path.substring(6));
            Epic epic = taskManager.epicById(id);
            if (epic != null) {
                taskManager.deleteByIdEpic(id);
                sendText(h, "{\"result\":\"Epic deleted\"}");
            } else {
                sendNotFound(h);
            }
        } else {
            sendNotFound(h);
        }
    }
}
