package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

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
    public void createTaskToStorage(Task task) {
        task.setId(id);
        taskStorage.put(task.getId(), task);
        id++;
    }

    /**
     * метод по созданию больших задач
     * @param epic принимает большую задачу
     */
    public void createEpicToStorage(Epic epic) {
        epic.setId(id);
        epicTaskStorage.put(epic.getId(), epic);
        id++;
    }

    /**
     * метод по созданию подзадач для эпиков
     * @param subtask принимает подзадачу
     */
    public void createSubtaskToStorage(SubTask subtask) {
        subtask.setId(id);
        subTasksStorage.put(subtask.getId(), subtask);
        id++;
    }

    /**
     * метод по получению списка всех задач
     * @return список задач
     */
    public ArrayList<Task> getListOfTasks() {
        ArrayList<Task> listOfTasks = new ArrayList<>();
        for(Integer id : taskStorage.keySet()){
            listOfTasks.add(taskStorage.get(id));
        }
        return listOfTasks;
    }

    /**
     * метод по получению списка всех эпиков
     * @return список эпиков
     */
    public ArrayList<Task> getListOfEpicTasks() {
        ArrayList<Task> listOfTasks = new ArrayList<>();
        for(Integer id : epicTaskStorage.keySet()){
            listOfTasks.add(epicTaskStorage.get(id));
        }
        return listOfTasks;
    }

    /**
     * метод по получению списка всех подзадач
     * @return список подзадач
     */
    public ArrayList<Task> getListOfSubTasks() {
        ArrayList<Task> listOfTasks = new ArrayList<>();
        for(Integer id : subTasksStorage.keySet()){
            listOfTasks.add(subTasksStorage.get(id));
        }
        return listOfTasks;
    }

    /**
     * метод удаляет все задачи и после этого возвращает нумерацию к единице.
     */
    public void deleteAllTasks() {
        taskStorage.clear();
    }

    /**
     * метод удаляет все эпики и после этого возвращает нумерацию к единице.
     */
    public void deleteAllEpicTasks() {
        epicTaskStorage.clear();
    }

    /**
     * метод удаляет все подзадачи и после этого возвращает нумерацию к единице.
     */
    public void deleteAllSubTasks() {
        subTasksStorage.clear();
    }

    /**
     * метод получения задачи по идентификатору
     * @param id принимает id по которому нужно получить задачу
     * @return возвращает найденную задачу
     */
    public Task getTaskById(int id) {
        Task someTask = null;
        if (taskStorage.get(id) != null) {
            someTask = taskStorage.get(id);
        }
        return someTask;
    }

    /**
     * метод получения эпика по идентификатору
     * @param id принимает id по которому нужно получить эпик
     * @return возвращает найденную задачу
     */
    public Epic getEpicTaskById(int id) {
        Epic someTask = null;
         if (epicTaskStorage.get(id) != null) {
            someTask = epicTaskStorage.get(id);
        }
        return someTask;
    }

    /**
     * метод получения подзадачи по идентификатору
     * @param id принимает id по которому нужно получить подзадачу
     * @return возвращает найденную задачу
     */
    public SubTask getSubTaskById(int id) {
        SubTask someTask = null;
        if (subTasksStorage.get(id) != null){
            someTask = subTasksStorage.get(id);
        }
        return someTask;
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
    }

    /**
     * метод удаления задачи по номеру.
     * @param id принимает номер искомой задачи
     */
    public void deleteTaskById(int id) {
        if (taskStorage.containsKey(id)) {
            taskStorage.remove(id);
        } else {
            System.out.println("Задачи с таким id нет");
        }
    }
    public void deleteEpicTaskById(int id) {
        if (epicTaskStorage.containsKey(id)) { // проверяем наличие эпика по ключу
            Epic epic = getEpicTaskById(id); // если есть, то подтягиваем его сюда
            for(SubTask subTask: epic.getSubTasks()) { // проходим циклом по его подзадачам
                subTasksStorage.remove(subTask); // удаляем из хранилища найденные сабтаски этого эпика
            }
            epicTaskStorage.remove(id); // удаляем сам эпик
        } else {
            System.out.println("Задачи с таким id нет");
        }
    }
    public void deleteSubTaskById(int id) {
        if (subTasksStorage.containsKey(id)) { // проверяем наличие сабтаски по ключу
            SubTask subTask = getSubTaskById(id); // если есть, то также подтягиваем ее сюда
            subTask.getEpicTask().getSubTasks().remove(subTask); // обратились к сабтаску, получили ссылку на его эпик,
            // получили список сабтаск этого эпика, удалили из этого списка сабтаску
            subTasksStorage.remove(id); // удаляем сабтаску по номеру
        } else {
            System.out.println("Задачи с таким id нет");
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

