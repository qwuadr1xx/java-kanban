package kz.zip.taskmaster.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import kz.zip.taskmaster.model.Task;
import kz.zip.taskmaster.service.TaskManager;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("GET".equalsIgnoreCase(method)) {
                if ("/prioritized".equals(path)) {
                    handleGetHistory(exchange);
                } else {
                    sendNotFound(exchange, "Путь не найден");
                }
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, "Ошибка при обработке запроса");
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        Set<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        String response = gson.toJson(prioritizedTasks);
        sendTest(exchange, 200, response);
    }
}
