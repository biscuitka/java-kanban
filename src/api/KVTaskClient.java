package api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class KVTaskClient {
    private final String url;
    private final HttpClient client;
    private final String apiToken;

    public KVTaskClient(String url) {
        client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "register/"))
                .GET()
                .build();
        this.url = url;
        this.apiToken = "?API_TOKEN=" + Objects.requireNonNull(sendRequest(request)).body();

    }

    /**
     * должен сохранять состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN=
     *
     * @param key  ключ
     * @param json строка в формате JSON
     */
    public void put(String key, String json) {
        URI urlSave = URI.create(url + "save/" + key + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(urlSave)
                .header("Content-type", "application/json")
                .build();
        sendRequest(request);
    }

    /**
     * должен возвращать состояние менеджера задач через запрос GET /load/<ключ>?API_TOKEN=
     *
     * @param key ключ
     * @return состояние менеджера
     */
    public String load(String key) {
        URI urlLoad = URI.create(url + "load/" + key + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(urlLoad)
                .header("Content-type", "application/json")
                .build();
        return Objects.requireNonNull(sendRequest(request)).body();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exp) {
            System.out.println("Ошибка выполнения запроса по адресу " + request.uri());
            return null;
        }
    }
}
