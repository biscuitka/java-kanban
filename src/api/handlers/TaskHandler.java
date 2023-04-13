package api.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.Managers;
import managers.TaskManager;
import managers.TypeOfTask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Хэндлер работает с простыми задачами - запрос списка задач, по номеру, обновить, создать и удалить.
 */
public class TaskHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        String response = "";
        int codeStatus = 200;

        switch (requestMethod) {
            case "GET":
                if (query == null) {
                    response = Managers.getGson().toJson(taskManager.getListOfTasks());
                } else {
                    int id = Integer.parseInt(query.split("=")[1]);
                    Task task = taskManager.getTaskById(id);
                    if (task != null) {
                        response = Managers.getGson().toJson(task);
                    } else {
                        codeStatus = 404;
                        response = "Задача не найдена";
                    }
                }

                break;
            case "POST":
                Task task = null;
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JsonElement element = JsonParser.parseString(body);
                TypeOfTask type = Managers.getGson()
                        .fromJson(element.getAsJsonObject().get("type"), TypeOfTask.class);
                if (type.equals(TypeOfTask.TASK)){
                    task = Managers.getGson().fromJson(element, Task.class);
                }
                if (query == null) {
                    taskManager.createTask(task);
                    assert task != null;
                    response = "Простая задача добавлена под id: " + task.getId();
                } else {
                    int id = Integer.parseInt(query.split("=")[1]);
                    if (taskManager.getListOfTasks().get(id) != null) {
                        taskManager.updateTask(task);
                        response = "Простая задача обновлена";
                    }
                }
                break;
            case "DELETE":
                if (query == null) {
                    taskManager.deleteAllTasks();
                    response = "Все простые задачи удалены";
                } else {
                    int id = Integer.parseInt(query.split("=")[1]);
                    if (taskManager.getListOfTasks().get(id) != null) {
                        taskManager.deleteTaskById(id);
                        response = "Задача с id: " + id + " удалена";
                    } else {
                        codeStatus = 404;
                        response = "Задача не найдена";
                    }
                }
                break;
            default:
                codeStatus = 405;
                response = "Некорректный запрос, ожидалось GET/POST/DELETE";
                break;
        }

        exchange.sendResponseHeaders(codeStatus, 0);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        }
    }

}


