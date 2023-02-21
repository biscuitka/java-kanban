package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private static int id = 1;
    private final HashMap<Integer, Task> taskStorage = new HashMap<>();
    private final HashMap<Integer, Epic> epicTaskStorage = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasksStorage = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();


    /**
     * метод по созданию простых задач
     *
     * @param task принимает простую задачу
     */
    @Override
    public void createTask(Task task) {
        task.setId(id++);
        taskStorage.put(task.getId(), task);
    }

    /**
     * метод по созданию больших задач
     *
     * @param epic принимает большую задачу
     */
    @Override
    public void createEpic(Epic epic) {
        epic.setId(id++);
        epicTaskStorage.put(epic.getId(), epic);
    }

    /**
     * метод по созданию подзадач для эпиков
     *
     * @param subtask принимает подзадачу
     */
    @Override
    public void createSubtask(SubTask subtask) {
        subtask.setId(id++);
        subTasksStorage.put(subtask.getId(), subtask);
    }

    /**
     * метод по получению списка всех задач
     *
     * @return список задач
     */
    @Override
    public ArrayList<Task> getListOfTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    /**
     * метод по получению списка всех эпиков
     *
     * @return список эпиков
     */
    @Override
    public ArrayList<Task> getListOfEpicTasks() {
        return new ArrayList<>(epicTaskStorage.values());
    }

    /**
     * метод по получению списка всех подзадач
     *
     * @return список подзадач
     */
    @Override
    public ArrayList<Task> getListOfSubTasks() {
        return new ArrayList<>(subTasksStorage.values());
    }

    /**
     * метод удаляет все задачи.
     */
    @Override
    public void deleteAllTasks() {
        taskStorage.clear();
    }

    /**
     * метод удаляет все эпики, а с ними все сабтаски
     */
    @Override
    public void deleteAllEpicTasks() {
        epicTaskStorage.clear();
        subTasksStorage.clear();
    }

    /**
     * метод удаляет все подзадачи и чистим списки сабтасок у эпиков
     */
    @Override
    public void deleteAllSubTasks() {
        subTasksStorage.clear();
        for (Epic epic : epicTaskStorage.values()) {
            epic.getSubTasks().clear();
        }
    }

    /**
     * метод получения задачи по идентификатору
     *
     * @param id принимает id по которому нужно получить задачу
     * @return возвращает найденную задачу
     */
    @Override
    public Task getTaskById(int id) {
        if (taskStorage.get(id) != null) {
            historyManager.add(taskStorage.get(id));
        }
        return taskStorage.get(id);
    }

    /**
     * метод получения эпика по идентификатору
     *
     * @param id принимает id по которому нужно получить эпик
     * @return возвращает найденную задачу
     */
    @Override
    public Epic getEpicTaskById(int id) {
        if (epicTaskStorage.get(id) != null) {
            historyManager.add(epicTaskStorage.get(id));
        }
        return epicTaskStorage.get(id);
    }

    /**
     * метод получения подзадачи по идентификатору
     *
     * @param id принимает id по которому нужно получить подзадачу
     * @return возвращает найденную задачу
     */
    @Override
    public SubTask getSubTaskById(int id) {
        if (subTasksStorage.get(id) != null) {
            historyManager.add(subTasksStorage.get(id));
        }
        return subTasksStorage.get(id);
    }

    /**
     * метод для получения списка всех подзадач определённого эпика.
     *
     * @param id принимает номер эпика
     * @return список подзадач
     */
    @Override
    public ArrayList<SubTask> getSubtasksOfEpic(int id) {
        return getEpicTaskById(id).getSubTasks();
    }

    /**
     * метод по обновлению задачи
     *
     * @param task обновленная задача
     */
    @Override
    public void updateTask(Task task) {
        taskStorage.put(task.getId(), task);
    }

    /**
     * метод по обновлению эпика
     *
     * @param epic обновленный эпик
     */
    @Override
    public void updateEpicTask(Epic epic) {
        epicTaskStorage.put(epic.getId(), epic);
    }

    /**
     * метод по обновлению подзадачи
     *
     * @param subTask обновленная подзадача
     */
    @Override
    public void updateSubTask(SubTask subTask) {
        subTasksStorage.put(subTask.getId(), subTask);

        Epic epic = subTask.getEpicTask();
        List<SubTask> subTasks = epic.getSubTasks();

        for (SubTask subTaskOfEpic : subTasks) {
            if (subTask.getId() == subTaskOfEpic.getId()) {
                subTasks.remove(subTaskOfEpic);
                break;
            }
        }
        subTasks.add(subTask);
    }

    /**
     * метод удаления задачи по номеру.
     *
     * @param id принимает номер искомой задачи
     */
    @Override
    public void deleteTaskById(int id) {
        taskStorage.remove(id);
        historyManager.remove(id); 
    }

    /**
     * метод удаления эпика по номеру.
     *
     * @param id принимает номер искомого эпика
     */
    @Override
    public void deleteEpicTaskById(int id) {
        if (epicTaskStorage.containsKey(id)) {
            Epic epic = getEpicTaskById(id);
            for (SubTask subTask : epic.getSubTasks()) {
                subTasksStorage.remove(subTask.getId());
                historyManager.remove(subTask.getId());
            }
            epicTaskStorage.remove(id);
            historyManager.remove(id);
        }
    }

    /**
     * метод удаления подзадачи по номеру.
     *
     * @param id принимает номер искомой подзадачи
     */
    @Override
    public void deleteSubTaskById(int id) {
        if (subTasksStorage.containsKey(id)) {
            SubTask subTask = getSubTaskById(id);
            subTask.getEpicTask().getSubTasks().remove(subTask);
            subTasksStorage.remove(id);
            historyManager.remove(id);
        }
    }

    /**
     * метод возвращает список просмотренных задач
     *
     * @return список задач
     */
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}

