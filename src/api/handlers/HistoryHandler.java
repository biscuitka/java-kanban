package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Хэндлер работает с историей просмотров
 */
public class HistoryHandler implements HttpHandler {
    private final TaskManager taskManager;
    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String response;
        int codeStatus = 200;

        if (requestMethod.equals("GET")) {
            response = Managers.getGson().toJson(taskManager.getHistory());
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
