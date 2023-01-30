package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * класс по работе всеми видами задач
 */
public class Manager {
    private static int id = 1;
    HashMap<Integer, Task> taskStorage = new HashMap<>();
    HashMap<Integer, Epic> epicTaskStorage = new HashMap<>();
    HashMap<Integer, SubTask> subTasksStorage = new HashMap<>();


    /**
     * метод по созданию простых задач
     * @param task принимает простую задачу
     */
    public void createTask(Task task) {
        task.setId(id++);
        taskStorage.put(task.getId(), task);
    }

    /**
     * метод по созданию больших задач
     * @param epic принимает большую задачу
     */
    public void createEpic(Epic epic) {
        epic.setId(id++);
        epicTaskStorage.put(epic.getId(), epic);
    }

    /**
     * метод по созданию подзадач для эпиков
     * @param subtask принимает подзадачу
     */
    public void createSubtask(SubTask subtask) {
        subtask.setId(id++);
        subTasksStorage.put(subtask.getId(), subtask);
    }

    /**
     * метод по получению списка всех задач
     * @return список задач
     */
    public ArrayList<Task> getListOfTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    /**
     * метод по получению списка всех эпиков
     * @return список эпиков
     */
    public ArrayList<Task> getListOfEpicTasks() {
        return new ArrayList<>(epicTaskStorage.values());
    }

    /**
     * метод по получению списка всех подзадач
     * @return список подзадач
     */
    public ArrayList<Task> getListOfSubTasks() {
        return new ArrayList<>(subTasksStorage.values());
    }

    /**
     * метод удаляет все задачи.
     */
    public void deleteAllTasks() {
        taskStorage.clear();
    }

    /**
     * метод удаляет все эпики, а с ними все сабтаски
     */
    public void deleteAllEpicTasks() {
        epicTaskStorage.clear();
        subTasksStorage.clear();
    }

    /**
     * метод удаляет все подзадачи и чистим списки сабтасок у эпиков
     */
    public void deleteAllSubTasks() {
        subTasksStorage.clear();
        for (Epic epic : epicTaskStorage.values()) {
            epic.getSubTasks().clear();
        }
    }

    /**
     * метод получения задачи по идентификатору
     * @param id принимает id по которому нужно получить задачу
     * @return возвращает найденную задачу
     */
    public Task getTaskById(int id) {
        return taskStorage.get(id);
    }

    /**
     * метод получения эпика по идентификатору
     * @param id принимает id по которому нужно получить эпик
     * @return возвращает найденную задачу
     */
    public Epic getEpicTaskById(int id) {
        return epicTaskStorage.get(id);
    }

    /**
     * метод получения подзадачи по идентификатору
     * @param id принимает id по которому нужно получить подзадачу
     * @return возвращает найденную задачу
     */
    public SubTask getSubTaskById(int id) {
        return subTasksStorage.get(id);
    }

    /**
     * метод для получения списка всех подзадач определённого эпика.
     * @param id принимает номер эпика
     * @return список подзадач
     */
    public ArrayList<SubTask> getSubtasksOfEpic(int id) {
        return getEpicTaskById(id).getSubTasks();
    }

    /**
     * метод по обновлению задачи
     * @param task обновленная задача
     */
    public void updateTask(Task task) {
        taskStorage.put(task.getId(), task);
    }

    /**
     * метод по обновлению эпика
     * @param epic обновленный эпик
     */
    public void updateEpicTask(Epic epic) {
        epicTaskStorage.put(epic.getId(), epic);
    }

    /**
     * метод по обновлению подзадачи
     * @param subTask обновленная подзадача
     */
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
     * @param id принимает номер искомой задачи
     */
    public void deleteTaskById(int id) {
        if (taskStorage.containsKey(id)) // эту строку можно удалить, кажется в данном случае она не имеет смысла?
            taskStorage.remove(id);

    }

    /**
     * метод удаления эпика по номеру.
     * @param id принимает номер искомого эпика
     */
    public void deleteEpicTaskById(int id) {
        if (epicTaskStorage.containsKey(id)) {
            Epic epic = getEpicTaskById(id);
            for (SubTask subTask : epic.getSubTasks()) {
                subTasksStorage.remove(subTask.getId());
            }
            epicTaskStorage.remove(id);
        }
    }

    /**
     * метод удаления подзадачи по номеру.
     * @param id принимает номер искомой подзадачи
     */
    public void deleteSubTaskById(int id) {
        if (subTasksStorage.containsKey(id)) {
            SubTask subTask = getSubTaskById(id);
            subTask.getEpicTask().getSubTasks().remove(subTask);
            subTasksStorage.remove(id);
        }
    }

    public HashMap<Integer, Task> getTaskStorage() {
        return taskStorage;
    }

    public HashMap<Integer, Epic> getEpicTaskStorage() {
        return epicTaskStorage;
    }

    public HashMap<Integer, SubTask> getSubTasksStorage() {
        return subTasksStorage;
    }

}

