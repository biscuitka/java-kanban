package api.handlers;

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

    public AllTasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response;
        int codeStatus = 200;

        if (requestMethod.equals("GET") && path.equals("/tasks/")) {
            response = Managers.getGson().toJson(taskManager.getPrioritizedTasks());
        } else {
            codeStatus = 400;
            response = "Некорректный запрос, ожидалось GET";
        }
        exchange.sendResponseHeaders(codeStatus, 0);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        }
    }
}
