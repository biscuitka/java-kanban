package api.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Хэндлер работает со списком приоритетных задач
 */
public class AllTasksHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = Managers.getGson();

    public AllTasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange)  {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response;
        int codeStatus = 200;

        if (requestMethod.equals("GET") && path.equals("/tasks/")) {
            response = gson.toJson(taskManager.getPrioritizedTasks());
        } else {
            codeStatus = 400;
            response = "Некорректный запрос, ожидалось GET";
        }

        try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(codeStatus, 0);
            outputStream.write(response.getBytes());
        } catch (IOException exp){
            System.out.println("Пустой ответ.");
        }
    }
}
