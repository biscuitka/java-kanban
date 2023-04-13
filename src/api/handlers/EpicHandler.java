package api.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.Managers;
import managers.TaskManager;
import managers.TypeOfTask;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


/**
 * Хэндлер работает с эпиками
 */
public class EpicHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
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
                    response = Managers.getGson().toJson(taskManager.getListOfEpicTasks());
                } else {
                    int id = Integer.parseInt(query.split("=")[1]);
                    Epic epic = taskManager.getEpicTaskById(id);
                    if (epic != null) {
                        response = Managers.getGson().toJson(epic);
                    } else {
                        codeStatus = 404;
                        response = "Эпик не найден";
                    }
                }
                break;
            case "POST":
                Epic epic = null;
                InputStream inputStream = exchange.getRequestBody(); // извлекли тело запроса
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8); // создали строку из тела запроса
                JsonElement element = JsonParser.parseString(body);
                TypeOfTask type = Managers.getGson()
                        .fromJson(element.getAsJsonObject().get("type"), TypeOfTask.class);
                if (type.equals(TypeOfTask.EPIC)){
                    epic = Managers.getGson().fromJson(element, Epic.class);
                }
                if (query == null) {
                    taskManager.createEpic(epic);
                    assert epic != null;
                    response = "Эпик добавлен под id: " + epic.getId();
                } else {
                    int id = Integer.parseInt(query.split("=")[1]);
                    if (taskManager.getListOfEpicTasks().get(id) != null) {
                        taskManager.updateEpic(epic);
                        response = "Эпик обновлен";
                    }
                }
                break;
            case "DELETE":
                if (query == null) {
                    taskManager.deleteAllEpicTasks();
                    response = "Все эпики удалены";
                } else {
                    int id =Integer.parseInt(query.split("=")[1]);
                    if (taskManager.getListOfEpicTasks().get(id) != null) {
                        taskManager.deleteEpicTaskById(id);
                        response = "Эпик с id: " + id + " удален";
                    } else {
                        codeStatus = 404;
                        response = "Эпик не найден";
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
