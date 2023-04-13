package api.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.Managers;
import managers.TaskManager;
import managers.TypeOfTask;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Хэндлер работает с сабтасками
 */
public class SubtaskHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
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
                    response = Managers.getGson().toJson(taskManager.getListOfSubTasks());
                } else {
                    int id = java.lang.Integer.parseInt(query.split("=")[1]);
                    Subtask subTask = taskManager.getSubTaskById(id);
                    if (subTask != null) {
                        response = Managers.getGson().toJson(subTask);
                    } else {
                        codeStatus = 404;
                        response = "Подзадача не найдена";
                    }
                }
                break;
            case "POST":
                    Subtask subTask = null;
                    InputStream inputStream = exchange.getRequestBody(); // извлекли тело запроса
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8); // создали строку из тела запроса
                    JsonElement element = JsonParser.parseString(body);
                    TypeOfTask type = Managers.getGson()
                            .fromJson(element.getAsJsonObject().get("type"), TypeOfTask.class);
                    if (type.equals(TypeOfTask.SUBTASK)){
                        subTask = Managers.getGson().fromJson(element, Subtask.class);
                    }
                    if (query == null) {
                        taskManager.createSubtask(subTask);
                        assert subTask != null;
                        response = "Подзадача добавлена под id: " + subTask.getId();
                    } else {
                        int id = java.lang.Integer.parseInt(query.split("=")[1]);
                        if (taskManager.getSubTaskById(id) != null) {
                            taskManager.updateSubTask(subTask);
                            response = "Подзадача обновлена";
                        }
                    }
                    break;
            case "DELETE":
                if (query == null) {
                    taskManager.deleteAllSubTasks();
                    response = "Все подзадачи удалены";
                } else {
                    int id = java.lang.Integer.parseInt(query.split("=")[1]);
                    if (taskManager.getListOfSubTasks().get(id) != null) {
                        taskManager.deleteSubTaskById(id);
                        response = "Подзадача с id: " + id + " удалена";
                    } else {
                        codeStatus = 404;
                        response = "Подзадача не найдена";
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
