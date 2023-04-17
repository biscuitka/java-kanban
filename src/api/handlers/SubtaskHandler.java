package api.handlers;

import com.google.gson.Gson;
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
    private final Gson gson = Managers.getGson();

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
                    response = gson.toJson(taskManager.getListOfSubTasks());
                } else {
                    int id = Integer.parseInt(query.split("=")[1]);
                    Subtask subTask = taskManager.getSubTaskById(id);
                    if (subTask != null) {
                        response = gson.toJson(subTask);
                    } else {
                        codeStatus = 404;
                        response = "Подзадача не найдена";
                    }
                }
                break;

            case "POST":
                Subtask subTask = null;
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JsonElement element = JsonParser.parseString(body);
                TypeOfTask type = gson.fromJson(element.getAsJsonObject().get("type"), TypeOfTask.class);
                if (type.equals(TypeOfTask.SUBTASK)) {
                    subTask = gson.fromJson(element, Subtask.class);
                }
                assert subTask != null;
                if (subTask.getId() == 0) {
                    taskManager.createSubtask(subTask);
                    response = "Подзадача добавлена под id: " + subTask.getId();
                } else {
                    taskManager.updateSubTask(subTask);
                    response = "Подзадача обновлена";
                }
                break;

            case "DELETE":
                if (query == null) {
                    taskManager.deleteAllSubTasks();
                    response = "Все подзадачи удалены";
                } else {
                    int id2 = Integer.parseInt(query.split("=")[1]);
                    taskManager.deleteSubTaskById(id2);
                    response = "Подзадача с id: " + id2 + " удалена";
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
