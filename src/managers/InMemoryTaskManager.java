package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> taskStorage = new HashMap<>();
    protected final HashMap<Integer, Epic> epicTaskStorage = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasksStorage = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();


    /**
     * сортирует задачи по StartTime
     */
    private final Comparator<Task> taskTimeComparator = Comparator.comparing(task -> task.getStartTime());
    /**
     * хранит заранее отсортированные задачи
     */
    protected Set<Task> prioritizedTasks = new TreeSet<>(taskTimeComparator);

    /**
     * Метод проверяет есть ли в мапах id. Если есть - то он его увеличивает пока не найдет свободный
     *
     * @return уникальный id
     */
    private int setUniqueId() {
        int uniqueId = 1;
        while (taskStorage.containsKey(uniqueId) || epicTaskStorage.containsKey(uniqueId)
                || subTasksStorage.containsKey(uniqueId)) {
            uniqueId++;
        }
        return uniqueId;
    }

    /**
     * метод по созданию простых задач
     *
     * @param task принимает простую задачу
     */
    @Override
    public int createTask(Task task) {
        task.setId(setUniqueId());
        addToPrioritizedTasks(task);
        taskStorage.put(task.getId(), task);

        return task.getId();
    }


    /**
     * метод по созданию больших задач
     *
     * @param epic принимает большую задачу
     */
    @Override
    public int createEpic(Epic epic) {
        epic.setId(setUniqueId());
        epicTaskStorage.put(epic.getId(), epic);
        return epic.getId();
    }

    /**
     * метод по созданию подзадач для эпиков
     *
     * @param subtask принимает подзадачу
     */
    @Override
    public int createSubtask(SubTask subtask) {
        subtask.setId(setUniqueId());
        addToPrioritizedTasks(subtask);
        subTasksStorage.put(subtask.getId(), subtask);
        return subtask.getId();
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
        prioritizedTasks.removeIf(task -> TypeOfTask.TASK == task.getType());
    }

    /**
     * метод удаляет все эпики, а с ними все сабтаски
     */
    @Override
    public void deleteAllEpicTasks() {
        epicTaskStorage.clear();
        subTasksStorage.clear();
        prioritizedTasks.removeIf(task -> TypeOfTask.SUBTASK == task.getType());
    }

    /**
     * метод удаляет все подзадачи и чистим списки сабтасок у эпиков
     */
    @Override
    public void deleteAllSubTasks() {
        subTasksStorage.clear();
        prioritizedTasks.removeIf(task -> TypeOfTask.SUBTASK == task.getType());
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
        return epicTaskStorage.get(id).getSubTasks();
    }

    /**
     * метод по обновлению задачи
     *
     * @param task обновленная задача
     */
    @Override
    public void updateTask(Task task) {
        if (isTimeIntersection(task)) {
            throw new TimeIntersectionException("Пересечение по времени с другими задачами");
        }
        prioritizedTasks.add(task);
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
        if (isTimeIntersection(subTask)) {
            throw new TimeIntersectionException("Пересечение по времени с другими задачами");
        }
        prioritizedTasks.add(subTask);
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
        if (taskStorage.containsKey(id)) {
            prioritizedTasks.remove(taskStorage.get(id));
            taskStorage.remove(id);
            historyManager.remove(id);
        }
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
            epic.getSubTasks().clear();
            for (SubTask subTask : epic.getSubTasks()) {
                subTasksStorage.remove(subTask.getId());
                historyManager.remove(subTask.getId());
                prioritizedTasks.remove(subTask);
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
            prioritizedTasks.remove(subTasksStorage.get(id));
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

    public HistoryManager getHistoryManager() {
        return historyManager;
    }


    /**
     * возвращает список задач и подзадач в заданном порядке
     *
     * @return список
     */
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
    private void addToPrioritizedTasks(Task task){
        if (isTimeIntersection(task)) {
            throw new TimeIntersectionException("Пересечение по времени с другими задачами");
        }
        prioritizedTasks.add(task);
    }

    /**
     * проверяет, что задачи и подзадачи не пересекаются во времени
     */
    public boolean isTimeIntersection(Task task) {
        List<Task> tasks = getPrioritizedTasks();
        if (!tasks.isEmpty()) {
            for (Task priorityTask : tasks) {
                if (priorityTask.getStartTime() != null && priorityTask.getEndTime() != null) {
                    if (task.getStartTime().isBefore(priorityTask.getStartTime())
                            && (task.getEndTime().isAfter(priorityTask.getStartTime()))) {
                        return true; // подправила так, надеюсь ты об этом писал=)
                    }
                    if (task.getStartTime().isAfter(priorityTask.getStartTime())
                            && (task.getEndTime().isBefore(priorityTask.getEndTime()))) {
                       return true;
                    }
                }
            }
        }
        return false;
    }

}

