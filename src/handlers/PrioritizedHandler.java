package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Task;
import serv.BaseHttpHandler;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }


    public void handle(HttpExchange h) throws IOException {
        if (!h.getRequestMethod().equals("GET")) {
            sendText(h, "{\"error\":\"Method not allowed\"}");
            h.sendResponseHeaders(405, -1);
            h.close();
            return;
        }

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        sendText(h, gson.toJson(prioritizedTasks));
        h.close();
    }
}
