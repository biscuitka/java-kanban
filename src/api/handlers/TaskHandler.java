package api.handlers;

import com.google.gson.Gson;
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
    private final Gson gson = Managers.getGson();

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
                    response = gson.toJson(taskManager.getListOfTasks());
                } else {
                    int id = Integer.parseInt(query.split("=")[1]);
                    Task task = taskManager.getTaskById(id);
                    if (task != null) {
                        response = gson.toJson(task);
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
                Integer id = gson.fromJson(element.getAsJsonObject().get("id"), Integer.class);
                TypeOfTask type = gson.fromJson(element.getAsJsonObject().get("type"), TypeOfTask.class);
                if (type.equals(TypeOfTask.TASK)) {
                    task = gson.fromJson(element, Task.class);
                }
                if (id == null) {
                    taskManager.createTask(task);
                    assert task != null;
                    response = "Простая задача добавлена под id: " + task.getId();
                } else {
                    taskManager.updateTask(task);
                    response = "Простая задача обновлена";
                }
                break;

            case "DELETE":
                if (query == null) {
                    taskManager.deleteAllTasks();
                    response = "Все простые задачи удалены";
                } else {
                    int id2 = Integer.parseInt(query.split("=")[1]);
                    taskManager.deleteTaskById(id2);
                    response = "Задача с id: " + id2 + " удалена";
                }
                break;

            default:
                codeStatus = 405;
                response = "Некорректный запрос, ожидалось GET/POST/DELETE";
                break;
        }

        try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(codeStatus, 0);
            outputStream.write(response.getBytes());
        } catch (IOException exp) {
            System.out.println("Пустой ответ.");
        }
    }

}


