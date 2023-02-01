package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * класс по работе всеми видами задач
 */
public interface TaskManager {
       /**
     * метод по созданию простых задач
     * @param task принимает простую задачу
     */
    public void createTask(Task task);

    /**
     * метод по созданию больших задач
     * @param epic принимает большую задачу
     */
    public void createEpic(Epic epic);

    /**
     * метод по созданию подзадач для эпиков
     * @param subtask принимает подзадачу
     */
    public void createSubtask(SubTask subtask);

    /**
     * метод по получению списка всех задач
     */
    public ArrayList<Task> getListOfTasks();

    /**
     * метод по получению списка всех эпиков
     */
    public ArrayList<Task> getListOfEpicTasks();

    /**
     * метод по получению списка всех подзадач
     */
    public ArrayList<Task> getListOfSubTasks();

    /**
     * метод удаляет все задачи.
     */
    public void deleteAllTasks();

    /**
     * метод удаляет все эпики, а с ними все сабтаски
     */
    public void deleteAllEpicTasks();

    /**
     * метод удаляет все подзадачи и чистим списки сабтасок у эпиков
     */
    public void deleteAllSubTasks();

    /**
     * метод получения задачи по идентификатору
     * @param id принимает id по которому нужно получить задачу
     */
    public Task getTaskById(int id);

    /**
     * метод получения эпика по идентификатору
     * @param id принимает id по которому нужно получить эпик
     */
    public Epic getEpicTaskById(int id);

    /**
     * метод получения подзадачи по идентификатору
     * @param id принимает id по которому нужно получить подзадачу
     */
    public SubTask getSubTaskById(int id);

    /**
     * метод для получения списка всех подзадач определённого эпика.
     * @param id принимает номер эпика
     */
    public ArrayList<SubTask> getSubtasksOfEpic(int id);

    /**
     * метод по обновлению задачи
     * @param task обновленная задача
     */
    public void updateTask(Task task);

    /**
     * метод по обновлению эпика
     * @param epic обновленный эпик
     */
    public void updateEpicTask(Epic epic);

    /**
     * метод по обновлению подзадачи
     * @param subTask обновленная подзадача
     */
    public void updateSubTask(SubTask subTask);


    /**
     * метод удаления задачи по номеру.
     * @param id принимает номер искомой задачи
     */
    public void deleteTaskById(int id);

    /**
     * метод удаления эпика по номеру.
     * @param id принимает номер искомого эпика
     */
    public void deleteEpicTaskById(int id);

    /**
     * метод удаления подзадачи по номеру.
     * @param id принимает номер искомой подзадачи
     */
    public void deleteSubTaskById(int id);

    public HashMap<Integer, Task> getTaskStorage();

    public HashMap<Integer, Epic> getEpicTaskStorage();

    public HashMap<Integer, SubTask> getSubTasksStorage();

}

