package api;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import managers.FileBackedTasksManager;
import managers.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient kvClient;

    public HttpTaskManager(String url) {
        this.kvClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        kvClient.put("tasks", Managers.getGson().toJson(getListOfTasks()));
        kvClient.put("subtasks", Managers.getGson().toJson(getListOfSubTasks()));
        kvClient.put("epics", Managers.getGson().toJson(getListOfEpicTasks()));
        kvClient.put("history", Managers.getGson().toJson(getHistory()));
    }

    public void loadFromKVServer() {
        List<Task> tasks = new ArrayList<>();
        List<Subtask> subtasks = new ArrayList<>();
        List<Epic> epics = new ArrayList<>();

        JsonArray taskElements = JsonParser.parseString(kvClient.load("tasks")).getAsJsonArray();
        for (JsonElement element : taskElements) {
            tasks.add(Managers.getGson().fromJson(element, Task.class));
        }

        JsonArray subElements = JsonParser.parseString(kvClient.load("subtasks")).getAsJsonArray();
        for (JsonElement element : subElements) {
            subtasks.add(Managers.getGson().fromJson(element, Subtask.class));
        }

        JsonArray epicElements = JsonParser.parseString(kvClient.load("epics")).getAsJsonArray();
        for (JsonElement element : epicElements) {
            epics.add(Managers.getGson().fromJson(element, Epic.class));
        }

    }


}
