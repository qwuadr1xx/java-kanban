package kz.zip.taskmaster.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import kz.zip.taskmaster.model.Task;
import kz.zip.taskmaster.service.TaskManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("GET".equalsIgnoreCase(method)) {
                if ("/history".equals(path)) {
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
        List<Task> history = taskManager.getHistory();
        String response = gson.toJson(history);
        sendTest(exchange, 200, response);
    }
}
