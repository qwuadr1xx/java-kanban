package kz.zip.taskmaster.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import kz.zip.taskmaster.model.Epic;
import kz.zip.taskmaster.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("GET".equalsIgnoreCase(method)) {
                if ("/epics".equals(path)) {
                    handleGetEpics(exchange);
                } else if (path.startsWith("/epics/")) {
                    handleGetEpicById(exchange);
                } else if (path.startsWith("/epics/") && path.endsWith("subtasks")) {
                    handleGetEpicSubtasks(exchange);
                } else {
                    sendNotFound(exchange, "Путь не найден");
                }
            } else if ("POST".equalsIgnoreCase(method)) {
                if ("/epics".equals(path)) {
                    handleCreateEpic(exchange);
                } else {
                    sendNotFound(exchange, "Путь не найден");
                }
            } else if ("DELETE".equalsIgnoreCase(method)) {
                if ("/epics".equals(path)) {
                    handleDeleteAllEpics(exchange);
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

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getListOfEpics();
        String response = gson.toJson(epics);
        sendTest(exchange, 200, response);
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String body = path.replace("/epics/", "");

        long number;
        try {
            number = Long.parseLong(body);
        } catch (NumberFormatException e) {
            sendNotFound(exchange, "id должен состоять из цифр");
            return;
        }

        Epic epic;
        if (!taskManager.isEpicIdInData(number)) {
            sendNotFound(exchange, "Задача с таким id не найдена");
            return;
        }

        epic = taskManager.getEpicById(number);
        String response = gson.toJson(epic);
        sendTest(exchange, 200, response);
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String body = path.replace("/epics/", "").replace("/subtasks", "");

        long number;
        try {
            number = Long.parseLong(body);
        } catch (NumberFormatException e) {
            sendNotFound(exchange, "id должен состоять из цифр");
            return;
        }

        Epic epic;
        if (!taskManager.isEpicIdInData(number)) {
            sendNotFound(exchange, "Задача с таким id не найдена");
            return;
        }

        epic = taskManager.getEpicById(number);
        String response = gson.toJson(epic.getSubtaskList());
        sendTest(exchange, 200, response);
    }

    private void handleCreateEpic(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);
        if (epic.getId() == 0) {
            taskManager.addEpic(epic);
        } else {
            taskManager.updateEpic(epic);
        }
        sendTest(exchange, 201, "Задача добавлена");
    }

    private void handleDeleteAllEpics(HttpExchange exchange) throws IOException {
        taskManager.clearEpics();
        taskManager.clearSubtasks();
        sendTest(exchange, 200, "Все задачи удалены");
    }
}
