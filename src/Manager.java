import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * класс по работе всеми видами задач
 */
public class Manager {
    private int id = 1;
    HashMap<Integer, Task> taskStorage = new HashMap<>(); //
    HashMap<Integer, Epic> epicTaskStorage = new HashMap<>();
    HashMap<Integer, SubTask> subTasksStorage = new HashMap<>();

    /**
     * метод по созданию простых задач
     * @param task принимает простую задачу
     */
    void addTaskToStorage(Task task) {
        taskStorage.put(id, task);
        id++;
    }

    /**
     * метод по созданию больших задач
     * @param epic принимает большую задачу
     */
    void addEpicToStorage(Epic epic) {
        epicTaskStorage.put(id, epic);
        id++;
    }

    /**
     * метод по созданию подзадач для эпиков
     * @param subtask принимает подзадачу
     */
    void addSubtaskToStorage(SubTask subtask) {
        subTasksStorage.put(id, subtask);
        id++;
    }

    /**
     * метод по получению списка всех задач
     * @return список задач
     */
    ArrayList<Task> getListOfTasks () {
        ArrayList<Task> listOfTasks = new ArrayList<>();
        for(Integer id : taskStorage.keySet()){
            listOfTasks.add(taskStorage.get(id));
        }
        for(Integer id : epicTaskStorage.keySet()){
            listOfTasks.add(epicTaskStorage.get(id));
        }
        for(Integer id : subTasksStorage.keySet()){
            listOfTasks.add(subTasksStorage.get(id));
        }
        return listOfTasks;
    }


    /**
     * метод удаляет все задачи и после этого возвращает нумерацию к единице.
     */
    void clearAllTasks() {
        taskStorage.clear();
        epicTaskStorage.clear();
        subTasksStorage.clear();
        id = 1;
    }

    /**
     * метод получения задачи по идентификатору
     * @param id принимает id по которому нужно получить задачу
     * @return возвращает найденную задачу
     */
    Task getTaskById(int id) {
        Task someTask = null; // объект someTask с начальным значением null
        if (taskStorage.get(id) != null) {
            someTask = taskStorage.get(id);
        } if (epicTaskStorage.get(id) != null) {
            someTask = epicTaskStorage.get(id);
        } if (subTasksStorage.get(id) != null){
            someTask = subTasksStorage.get(id);
        }
        return someTask;
    }

    /**
     * метод для получения списка всех подзадач определённого эпика.
     * @param epic принимает эпик в котором берем список
     * @return список подзадач
     */
    ArrayList<SubTask> getSubtasksOfEpic(Epic epic) {
        return epic.getSubTasks();
    }

    /**
     * метод по обновлению задачи
     * @param task обновленный объект
     */
    public void updateTask(Task task) {
        //Дима, помоги, пожалуйста) Нужно положить ее в хранилище, тем самым заменив старую на новую. Но никак не понимаю каким образом
        taskStorage.put(id, task);
    }

    /**
     * метод удаления задачи по номеру.
     * @param id принимает номер искомой задачи
     */
    void removeTaskById(int id) {
        /*И тут. В этом методе надо учесть взаимосвязь эпиков и подзадач. Сейчас он убирает задачу по номеру.
        Если удалить эпик, в котором лежит подзадача, то эта подзадача останется и будет ссылаться на
        удаленный объект в своем поле epicTask. А при удалении подзадачи она должна также удалится из
        соответствующего списка Epic.subtasks*/
        if (taskStorage.containsKey(id)) {
            taskStorage.remove(id);
        } else if (epicTaskStorage.containsKey(id)) {
            epicTaskStorage.remove(id);
            // ? subTask.setEpicTask(epicTaskStorage.remove(id));
        } else if (subTasksStorage.containsKey(id)) {
            subTasksStorage.remove(id);
            // ? epic.getSubTasks().remove(subTask);
        } else {
            System.out.println("Задачи с таким id нет");
        }
    }
}

