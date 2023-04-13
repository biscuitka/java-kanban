package api;

import api.handlers.*;
import com.sun.net.httpserver.HttpServer;
import managers.TaskManager;
import java.io.IOException;
import java.net.InetSocketAddress;


/**
 * слушает порт 8080 и принимает запросы
 */
public class HttpTaskServer {
    private HttpServer httpServer;
    private static final int PORT = 8080;
    public HttpTaskServer(TaskManager taskManager) {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks/", new AllTasksHandler(taskManager));
            httpServer.createContext("/tasks/task/", new TaskHandler(taskManager));
            httpServer.createContext("/tasks/subtask/", new SubtaskHandler(taskManager));
            httpServer.createContext("/tasks/epic/", new EpicHandler(taskManager));
            httpServer.createContext("/tasks/history", new HistoryHandler(taskManager));
        } catch (IOException exp) {
            System.out.println("Ошибка создания сервера");
        }
    }

    public void start() {
        System.out.println("HttpTaskServer запущен на " + PORT + " порту!");
        System.out.println("http://localhost:" + PORT);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("HttpServer порт: " + PORT + " остановлен!");
    }

}
