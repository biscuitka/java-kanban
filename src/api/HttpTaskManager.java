package api;


import com.google.gson.Gson;
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
    private final Gson gson = Managers.getGson();

    public HttpTaskManager(String url) {
        this.kvClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        kvClient.put("tasks", gson.toJson(getListOfTasks()));
        kvClient.put("subtasks", gson.toJson(getListOfSubTasks()));
        kvClient.put("epics", gson.toJson(getListOfEpicTasks()));
        kvClient.put("history", gson.toJson(getHistory()));
    }

    public void loadFromKVServer() {
        List<Task> tasks = new ArrayList<>();
        List<Subtask> subtasks = new ArrayList<>();
        List<Epic> epics = new ArrayList<>();

        JsonArray taskElements = JsonParser.parseString(kvClient.load("tasks")).getAsJsonArray();
        for (JsonElement element : taskElements) {
            tasks.add(gson.fromJson(element, Task.class));
        }
        for (Task task : tasks){
            taskStorage.put(task.getId(), task);
            addToPrioritizedTasks(task);
        }

        JsonArray subElements = JsonParser.parseString(kvClient.load("subtasks")).getAsJsonArray();
        for (JsonElement element : subElements) {
            subtasks.add(gson.fromJson(element, Subtask.class));
        }
        for (Subtask subtask : subtasks){
            subTasksStorage.put(subtask.getId(), subtask);
            addToPrioritizedTasks(subtask);
        }

        JsonArray epicElements = JsonParser.parseString(kvClient.load("epics")).getAsJsonArray();
        for (JsonElement element : epicElements) {
            epics.add(gson.fromJson(element, Epic.class));
        }
        for (Epic epic : epics){
            epicTaskStorage.put(epic.getId(), epic);
            addToPrioritizedTasks(epic);
        }

        String stringHistoryIds = JsonParser.parseString(kvClient.load("history")).getAsString();
        List<Integer> historyIds = new ArrayList<>(historyFromString(stringHistoryIds));
        for (Integer id : historyIds) {
            addTaskToHistoryById(id);
        }
    }


}
