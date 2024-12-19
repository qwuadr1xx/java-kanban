package kz.zip.taskmaster.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import kz.zip.taskmaster.model.Task;
import kz.zip.taskmaster.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("GET".equalsIgnoreCase(method)) {
                if ("/tasks".equals(path)) {
                    handleGetTasks(exchange);
                } else if (path.startsWith("/tasks/")) {
                    handleGetTaskById(exchange);
                } else {
                    sendNotFound(exchange, "Путь не найден");
                }
            } else if ("POST".equalsIgnoreCase(method)) {
                if ("/tasks".equals(path)) {
                    handleCreateTask(exchange);
                } else {
                    sendNotFound(exchange, "Путь не найден");
                }
            } else if ("DELETE".equalsIgnoreCase(method)) {
                if ("/tasks".equals(path)) {
                    handleDeleteAllTasks(exchange);
                } else {
                    sendNotFound(exchange, "Путь не найден");
                }
            } else {
                sendResponse(exchange, 405, "Метод не поддерживается");
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, "Возникла необрабатываемая проблема");
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getListOfTasks();
        String response = gson.toJson(tasks);
        sendTest(exchange, 200, response);
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String body = path.replace("/tasks/", "");

        long number;
        try {
            number = Long.parseLong(body);
        } catch (NumberFormatException e) {
            sendNotFound(exchange, "id должен состоять из цифр");
            return;
        }

        Task task;
        if (!taskManager.isTaskIdInData(number)) {
            sendNotFound(exchange, "Задача с таким id не найдена");
            return;
        }

        task = taskManager.getTaskById(number);
        String response = gson.toJson(task);
        sendTest(exchange, 200, response);
    }

    private void handleCreateTask(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(body, Task.class);

        if (task.getName() == null) {
            sendResponse(exchange, 400, "Задача должна иметь имя");
        } else if (task.getDescription() == null) {
            sendResponse(exchange, 400, "Задача должна иметь описание");
        }

        boolean isTaskIntersected = (task.getId() == 0)
                ? taskManager.addTask(task)
                : taskManager.updateTask(task);

        if (isTaskIntersected) {
            sendHasInteractions(exchange);
        }

        sendTest(exchange, 201, "Задача добавлена");
    }

    private void handleDeleteAllTasks(HttpExchange exchange) throws IOException {
        taskManager.clearTasks();
        sendTest(exchange, 200, "Все задачи удалены");
    }
}
