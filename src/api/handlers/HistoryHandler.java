package api.handlers;

import com.google.gson.Gson;
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
    private final Gson gson = Managers.getGson();
    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        String response;
        int codeStatus = 200;

        if (requestMethod.equals("GET")) {
            response = gson.toJson(taskManager.getHistory());
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
