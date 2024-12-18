package kz.zip.taskmaster.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import kz.zip.taskmaster.model.Subtask;
import kz.zip.taskmaster.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("GET".equalsIgnoreCase(method)) {
                if ("/subtasks".equals(path)) {
                    handleGetSubtasks(exchange);
                } else if (path.startsWith("/subtasks/")) {
                    handleGetSubtaskById(exchange);
                } else {
                    sendNotFound(exchange, "Путь не найден");
                }
            } else if ("POST".equalsIgnoreCase(method)) {
                if ("/subtasks".equals(path)) {
                    handleCreateSubtask(exchange);
                } else {
                    sendNotFound(exchange, "Путь не найден");
                }
            } else if ("DELETE".equalsIgnoreCase(method)) {
                if ("/subtasks".equals(path)) {
                    handleDeleteAllSubtasks(exchange);
                } else {
                    sendNotFound(exchange, "Путь не найден");
                }
            } else {
                sendNotFound(exchange, "Метод не поддерживается");
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, "Ошибка при обработке запроса");
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = taskManager.getListOfSubtasks();
        String response = gson.toJson(subtasks);
        sendTest(exchange, 200, response);
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String body = path.replace("/subtasks/", "");

        long number;
        try {
            number = Long.parseLong(body);
        } catch (NumberFormatException e) {
            sendNotFound(exchange, "id должен состоять из цифр");
            return;
        }

        Subtask subtask;
        if (!taskManager.isSubtaskIdInData(number)) {
            sendNotFound(exchange, "Подзадача с таким id не найдена");
            return;
        }

        subtask = taskManager.getSubtaskById(number);
        String response = gson.toJson(subtask);
        sendTest(exchange, 200, response);
    }

    private void handleCreateSubtask(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(body, Subtask.class);

        if (!taskManager.isEpicIdInData(subtask.getEpicId())) {
            sendNotFound(exchange, "Эпик подзадачи с данным id не найден");
            return;
        }

        if (subtask.getId() == 0) {
            if (!taskManager.addSubtask(subtask)) {
                sendTest(exchange, 201, "Задача добавлена");
            } else {
                sendHasInteractions(exchange);
            }
        } else {
            if (!taskManager.updateSubtask(subtask)) {
                sendTest(exchange, 201, "Задача добавлена");
            } else {
                sendHasInteractions(exchange);
            }
        }
    }

    private void handleDeleteAllSubtasks(HttpExchange exchange) throws IOException {
        taskManager.clearSubtasks();
        sendTest(exchange, 200, "Все подзадачи удалены");
    }
}
