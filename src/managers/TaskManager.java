package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * интерфейс для управления задачами
 */
public interface TaskManager {


    /**
     * метод по созданию простых задач
     *
     * @param task принимает простую задачу
     */
    int createTask(Task task);

    /**
     * метод по созданию больших задач
     *
     * @param epic принимает большую задачу
     */
    int createEpic(Epic epic);

    /**
     * метод по созданию подзадач для эпиков
     *
     * @param subtask принимает подзадачу
     */
    int createSubtask(Subtask subtask);

    /**
     * метод по получению списка всех задач
     */
    ArrayList<Task> getListOfTasks();

    /**
     * метод по получению списка всех эпиков
     */
    ArrayList<Task> getListOfEpicTasks();

    /**
     * метод по получению списка всех подзадач
     */
    ArrayList<Task> getListOfSubTasks();

    /**
     * метод удаляет все задачи.
     */
    void deleteAllTasks();

    /**
     * метод удаляет все эпики, а с ними все сабтаски
     */
    void deleteAllEpicTasks();

    /**
     * метод удаляет все подзадачи и чистим списки сабтасок у эпиков
     */
    void deleteAllSubTasks();

    /**
     * метод получения задачи по идентификатору
     *
     * @param id принимает id по которому нужно получить задачу
     */
    Task getTaskById(int id);

    /**
     * метод получения эпика по идентификатору
     *
     * @param id принимает id по которому нужно получить эпик
     */
    Epic getEpicTaskById(int id);

    /**
     * метод получения подзадачи по идентификатору
     *
     * @param id принимает id по которому нужно получить подзадачу
     */
    Subtask getSubTaskById(int id);

    /**
     * метод для получения списка всех подзадач определённого эпика.
     *
     * @param id принимает номер эпика
     */
    ArrayList<Subtask> getSubtasksOfEpic(int id);

    /**
     * метод по обновлению задачи
     *
     * @param task обновленная задача
     */
    public void updateTask(Task task);

    /**
     * метод по обновлению эпика
     *
     * @param epic обновленный эпик
     */
    void updateEpic(Epic epic);

    /**
     * метод по обновлению подзадачи
     *
     * @param subTask обновленная подзадача
     */
    void updateSubTask(Subtask subTask);


    /**
     * метод удаления задачи по номеру.
     *
     * @param id принимает номер искомой задачи
     */
    void deleteTaskById(int id);

    /**
     * метод удаления эпика по номеру.
     *
     * @param id принимает номер искомого эпика
     */
    void deleteEpicTaskById(int id);

    /**
     * метод удаления подзадачи по номеру.
     *
     * @param id принимает номер искомой подзадачи
     */
    void deleteSubTaskById(int id);

    List<Task> getHistory();


    List<Task> getPrioritizedTasks();
}


