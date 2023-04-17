package api.handlers;

import com.google.gson.Gson;
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
    private final Gson gson = Managers.getGson();

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
                    response = gson.toJson(taskManager.getListOfEpicTasks());
                } else {
                    int id = Integer.parseInt(query.split("=")[1]);
                    Epic epic = taskManager.getEpicTaskById(id);
                    if (epic != null) {
                        response = gson.toJson(epic);
                    } else {
                        codeStatus = 404;
                        response = "Эпик не найден";
                    }
                }
                break;

            case "POST":
                Epic epic = null;
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JsonElement element = JsonParser.parseString(body);
                TypeOfTask type = gson.fromJson(element.getAsJsonObject().get("type"), TypeOfTask.class);
                Integer id = gson.fromJson(element.getAsJsonObject().get("id"), Integer.class);
                if (type.equals(TypeOfTask.EPIC)) {
                    epic = gson.fromJson(element, Epic.class);
                }
                assert epic != null;
                if (epic.getId() == 0) {
                    taskManager.createEpic(epic);
                    response = "Эпик добавлен под id: " + epic.getId();
                } else {
                    taskManager.updateEpic(epic);
                    response = "Эпик обновлен";
                }
                break;

            case "DELETE":
                if (query == null) {
                    taskManager.deleteAllEpicTasks();
                    response = "Все эпики удалены";
                } else {
                    int id2 = Integer.parseInt(query.split("=")[1]);
                    taskManager.deleteEpicTaskById(id2);
                    response = "Эпик с id: " + id2 + " удален";
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
