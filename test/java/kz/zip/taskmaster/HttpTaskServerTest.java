package kz.zip.taskmaster;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kz.zip.taskmaster.http.HttpTaskServer;
import kz.zip.taskmaster.http.adapter.DurationAdapter;
import kz.zip.taskmaster.http.adapter.LocalDateTimeAdapter;
import kz.zip.taskmaster.model.Epic;
import kz.zip.taskmaster.model.Subtask;
import kz.zip.taskmaster.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static HttpClient client;
    private static HttpTaskServer server;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpTaskServer();
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void testCreateTask() throws IOException, InterruptedException {
        Task task = new Task(
                "Task 1",
                "Test description",
                Duration.ofMinutes(60),
                LocalDateTime.of(2025, 1, 1, 12, 0));
        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void testCreateIntersectionTask() throws IOException, InterruptedException {
        Task task1 = new Task(
                "Task 1",
                "Test description 1",
                Duration.ofMinutes(60),
                LocalDateTime.of(2025, 1, 1, 12, 0));
        Task task2 = new Task(
                "Task 2",
                "Test description 2",
                Duration.ofMinutes(60),
                LocalDateTime.of(2025, 1, 1, 12, 30));
        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }

    @Test
    void testGetTasks() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(0, tasks.length);

        Task task = new Task(
                "Task 1",
                "Test description",
                Duration.ofMinutes(60),
                LocalDateTime.of(2025, 1, 1, 12, 0));
        String taskJson = gson.toJson(task);

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, tasks.length);
    }

    @Test
    void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task(
                "Task 1",
                "Test description",
                Duration.ofMinutes(60),
                LocalDateTime.of(2025, 1, 1, 12, 0));
        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/1"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Task taskFromJson = gson.fromJson(response.body(), Task.class);
        assertEquals(1, taskFromJson.getId());
        assertEquals(taskFromJson.getName(), task.getName());
        assertEquals(taskFromJson.getDescription(), task.getDescription());
    }

    @Test
    void testDeleteAllTasks() throws IOException, InterruptedException {
        Task task = new Task(
                "Task 1",
                "Test description",
                Duration.ofMinutes(60),
                LocalDateTime.of(2025, 1, 1, 12, 0));
        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, tasks.length);

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        tasks = gson.fromJson(getResponse.body(), Task[].class);
        assertEquals(0, tasks.length);
    }

    @Test
    void testCreateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic description");
        String epicJson = gson.toJson(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void testGetEpics() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/epics"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Epic[] epics = gson.fromJson(response.body(), Epic[].class);
        assertEquals(0, epics.length);

        Epic epic = new Epic("Epic 1", "Epic description");
        String epicJson = gson.toJson(epic);

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/epics"))
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        epics = gson.fromJson(response.body(), Epic[].class);
        assertEquals(1, epics.length);
    }

    @Test
    void testCreateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic description");
        String epicJson = gson.toJson(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Subtask subtask = new Subtask(
                "Subtask 1",
                "Subtask description",
                1,
                Duration.ofMinutes(120),
                LocalDateTime.of(2025, 1, 1, 14, 0)
        );
        String subtaskJson = gson.toJson(subtask);

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void testCreateSubtaskWithoutEpic() throws IOException, InterruptedException {
        Subtask subtask = new Subtask(
                "Subtask 1",
                "Subtask description",
                999,
                Duration.ofMinutes(120),
                LocalDateTime.of(2025, 1, 1, 14, 0)
        );
        String subtaskJson = gson.toJson(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void testGetSubtasks() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Subtask[] subtasks = gson.fromJson(response.body(), Subtask[].class);
        assertEquals(0, subtasks.length);

        Epic epic = new Epic("Epic 1", "Epic description");
        String epicJson = gson.toJson(epic);

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = new Subtask(
                "Subtask 1",
                "Subtask description",
                1,
                Duration.ofMinutes(120),
                LocalDateTime.of(2025, 1, 1, 14, 0)
        );
        String subtaskJson = gson.toJson(subtask);

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/subtasks"))
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        subtasks = gson.fromJson(response.body(), Subtask[].class);
        assertEquals(1, subtasks.length);
    }
}


