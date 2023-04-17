package tests;

import api.HttpTaskManager;
import api.HttpTaskServer;

import api.KVServer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    Gson gson = Managers.getGson();
    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    TaskManager taskManager;
    HttpRequest request;
    HttpClient client;

    private Task createHttpTask() throws IOException, InterruptedException {
        Task task = new Task();
        task.setName("простая задача");
        task.setDescription("описание простой задачи");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(10);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client = HttpClient.newHttpClient();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        return task;
    }

    private Epic createHttpEpic() throws IOException, InterruptedException {
        Epic epic = new Epic();
        epic.setName("Эпик");
        epic.setDescription("Описание эпика");
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client = HttpClient.newHttpClient();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        return epic;
    }

    private Subtask createHttpSubtask(int epicId) throws IOException, InterruptedException {
        Subtask subtask = new Subtask();
        subtask.setName("Подзадача эпика");
        subtask.setDescription("описание подзадачи");
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(20);
        subtask.setEpicId(epicId);
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client = HttpClient.newHttpClient();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        return subtask;
    }

    @BeforeEach
    void startServers() throws IOException {
        client = HttpClient.newHttpClient();
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new HttpTaskManager("http://localhost:8078/");
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

    }

    @AfterEach
    void stopServer() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void shouldCreateTask() throws IOException, InterruptedException {
        createHttpTask();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неправильный код ответа" + response.statusCode());
        System.out.println(response.body());
    }

    @Test
    void shouldCreateSubtask() throws IOException, InterruptedException {
        createHttpEpic();
        createHttpSubtask(1);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неправильный код ответа" + response.statusCode());
    }

    @Test
    void shouldCreateEpic() throws IOException, InterruptedException {
        createHttpEpic();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неправильный код ответа" + response.statusCode());
    }

    @Test
    void shouldGetAllTasks() throws IOException, InterruptedException {
        createHttpTask();
        createHttpTask();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldGetAllSubtasks() throws IOException, InterruptedException {
        createHttpEpic();
        createHttpSubtask(1);
        createHttpSubtask(1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldGetAllEpics() throws IOException, InterruptedException {
        createHttpEpic();
        createHttpEpic();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

    }

    @Test
    void shouldDeleteAllTasks() throws IOException, InterruptedException {
        createHttpTask();
        createHttpTask();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

    }

    @Test
    void shouldDeleteAllSubtasks() throws IOException, InterruptedException {
        createHttpEpic();
        createHttpSubtask(1);
        createHttpSubtask(1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    void shouldDeleteAllEpics() throws IOException, InterruptedException {
        createHttpEpic();
        createHttpEpic();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        createHttpTask();
        createHttpTask();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldGetSubtaskById() throws IOException, InterruptedException {
        createHttpEpic();
        createHttpSubtask(1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldGetEpicById() throws IOException, InterruptedException {
        createHttpEpic();
        createHttpEpic();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldDeleteTaskById() throws IOException, InterruptedException {
        createHttpTask();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldDeleteSubtaskById() throws IOException, InterruptedException {
        createHttpEpic();
        createHttpSubtask(1);
        createHttpSubtask(1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldDeleteEpicById() throws IOException, InterruptedException {
        createHttpEpic();
        createHttpEpic();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldGetPrioritizedTasks() throws IOException, InterruptedException {
        createHttpTask();
        Task task1 = createHttpTask();
        task1.setStartTime(LocalDateTime.now().plusDays(1));
        taskManager.updateTask(task1);
        Task task2 = createHttpTask();
        task2.setStartTime(LocalDateTime.now().plusDays(2));
        taskManager.updateTask(task2);
        createHttpEpic();
        Subtask subtask = createHttpSubtask(3);
        subtask.setStartTime(LocalDateTime.now().minusHours(5));
        taskManager.updateTask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonArray jsonList = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(taskManager.getPrioritizedTasks().size(), jsonList.size());
    }

    @Test
    void shouldGetHistory() throws IOException, InterruptedException {
        createHttpTask();
        createHttpEpic();
        createHttpSubtask(2);
        taskManager.getTaskById(1);
        taskManager.getEpicTaskById(2);
        taskManager.getSubTaskById(3);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonArray jsonHistory = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(taskManager.getHistory().size(), jsonHistory.size());
    }

}