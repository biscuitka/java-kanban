package managers;

import tasks.Epic;
import tasks.StatusOfTask;
import tasks.SubTask;
import tasks.Task;


import java.io.*;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * класс для автосохранения в файл
 */
public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;
    private static final String FIRST_lINE = "id,type,name,description,status,epic\n";


    /**
     * менеджер получает файл в конструкторе и сохраняет его в поле
     *
     * @param file файл
     */
    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {

        // тестирование
        TaskManager manager = Managers.getDefault();

/*
       //создание задач разных типов для записи в файл
        Task taskOne = new Task();
        taskOne.setName("простая задача1");
        taskOne.setDescription("описание задачи1");
        manager.createTask(taskOne);

        Task taskTwo = new Task();
        taskTwo.setName("простая задача2");
        taskTwo.setDescription("описание задачи2");
        manager.createTask(taskTwo);

        Epic epicTaskOne = new Epic();
        epicTaskOne.setName("Эпик1");
        epicTaskOne.setDescription("Описание эпика1");
        manager.createEpic(epicTaskOne);

        SubTask subtaskOne = new SubTask();
        subtaskOne.setName("Подзадача1 эпика1");
        subtaskOne.setDescription("описание подзадачи1");
        subtaskOne.setEpicTask(epicTaskOne);
        epicTaskOne.getSubTasks().add(subtaskOne);
        manager.createSubtask(subtaskOne);

        SubTask subtaskTwo = new SubTask();
        subtaskTwo.setName("подзадача2 эпика1");
        subtaskTwo.setDescription("описание подзадачи2");
        subtaskTwo.setEpicTask(epicTaskOne);
        epicTaskOne.getSubTasks().add(subtaskTwo);
        manager.createSubtask(subtaskTwo);

        Epic epicTaskTwo = new Epic();
        epicTaskTwo.setName("Эпик2");
        epicTaskTwo.setDescription("описание эпика2");
        manager.createEpic(epicTaskTwo);


        System.out.println("тест создания tasks- " + "\n" + taskOne + "\n" + taskTwo);
        System.out.println();

        System.out.println("тест создания epicTask - " + "\n" + epicTaskOne + "\n" + epicTaskTwo);
        System.out.println();

        System.out.println("тест создания subTask - " + "\n" + subtaskOne + "\n" + subtaskTwo);
        System.out.println();
*/


        // тестирование получения и истории
        System.out.println("список простых задач- " + "\n" + manager.getListOfTasks());
        System.out.println("список эпиков- " + "\n" + manager.getListOfEpicTasks());
        System.out.println("список сабтасок- " + "\n" + manager.getListOfSubTasks());
        System.out.println();

        System.out.println("тестируем получение по id 2 " + manager.getTaskById(2));
        System.out.println();

        System.out.println("тестируем получение по id 3 " + manager.getEpicTaskById(3));
        System.out.println();

        System.out.println("подзадачи определенного эпика - " + manager.getSubtasksOfEpic(3));
        System.out.println();
        System.out.println("История просмотров задач:\n" + manager.getHistory());

        /*
        // для тестирования удаления
        System.out.println("дальше удаляем по номеру задачу(2)");
        manager.deleteTaskById(2);
        System.out.println("История просмотров задач после удаления задачи(2):\n" + manager.getHistory());
        System.out.println();

        System.out.println("дальше снова удаляем по номеру эпик(1, id3)");
        manager.deleteEpicTaskById(3);
        System.out.println("История просмотров задач после удаления эпика(1, id3), с ней должна удалиться ее подзадача1:\n" + manager.getHistory());

        System.out.println("список простых задач после удаления по номеру- " + "\n" + manager.getListOfTasks());
        System.out.println("список эпиков после удаления по номеру- " + "\n" + manager.getListOfEpicTasks());
        System.out.println("список сабтасок после удаления по номеру- " + "\n" + manager.getListOfSubTasks());
        System.out.println("История просмотров задач:\n" + manager.getHistory());
        System.out.println();

        System.out.println("тестируем полное удаление");
        manager.deleteAllTasks();
        manager.deleteAllEpicTasks();
        manager.deleteAllSubTasks();

        System.out.println("список простых задач после удаления- " + "\n" + manager.getListOfTasks());
        System.out.println("список эпиков после удаления- " + "\n" + manager.getListOfEpicTasks());
        System.out.println("список сабтасок после удаления- " + "\n" + manager.getListOfSubTasks());

         */
    }

    /**
     * Сохраняет текущее состояние менеджера в файл.
     * Сохраняет все типы задач и историю просмотров в файл csv
     */
    public void save() {

        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(FIRST_lINE);

            for (Task task : getListOfTasks()) {
                fileWriter.write(task.toStringInFile() + "\n");
            }
            for (Task epic : getListOfEpicTasks()) {
                fileWriter.write(epic.toStringInFile() + "\n");
            }
            for (Task subtask : getListOfSubTasks()) {
                fileWriter.write(subtask.toStringInFile() + "\n");
            }

            fileWriter.write("\n");
            String history = historyToString(getHistory());
            fileWriter.write(history);


        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка создания файла");
        }

    }

    /**
     * восстанавливает данные менеджера из файла при запуске программы
     *
     * @param file файл с данными задач
     * @return менеджер
     */
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        String path = file.getPath();
        String taskLine = "";
        String historyLine = "";
        boolean isTask = true;
        int id;

        try {
            taskLine = Files.readString(Path.of(path));
        } catch (IOException exp) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }
        String[] lines = taskLine.split("\n");
        for (String line : lines) {
            if (line.isEmpty()) {
                isTask = false;
            }
            if (line.equals(FIRST_lINE)) {
                continue;
            }
            if (isTask) {
                String type = String.valueOf(line.split(",")[1]);

                if (type.equalsIgnoreCase("TASK")) {
                    Task task = fromString(line, tasksManager);
                    assert task != null;
                    id = task.getId();
                    tasksManager.createTaskFromFile(task, id);
                } else if (type.equalsIgnoreCase("EPIC")) {
                    Epic epic = (Epic) fromString(line, tasksManager);
                    assert epic != null;
                    id = epic.getId();
                    tasksManager.createEpicFromFile(epic, id);
                } else if (type.equalsIgnoreCase("SUBTASK")) {
                    SubTask subTask = (SubTask) fromString(line, tasksManager);
                    assert subTask != null;
                    id = subTask.getId();
                    tasksManager.createSubtaskFromFile(subTask, id);
                }
            } else {
                historyLine = line;
            }
        }
        List<Integer> idOfTasks = historyFromString(historyLine);
        for (Integer idHistory : idOfTasks) {
            tasksManager.getHistoryManager().add(allTasks(idHistory, tasksManager));

        }
        return tasksManager;
    }


    private static Task allTasks(int id, InMemoryTaskManager inMemoryTaskManager) {
        Task task = inMemoryTaskManager.getTaskStorage().get(id);
        if (!(task == null)) {
            return task;
        }
        Task epic = inMemoryTaskManager.getEpicTaskStorage().get(id);
        if (!(epic == null)) {
            return epic;
        }
        Task subtask = inMemoryTaskManager.getSubTasksStorage().get(id);
        if (!(subtask == null)) {
            return subtask;
        }
        return null;
    }


    /**
     * метод перевода истории в строку
     *
     * @return строка с историей просмотров
     */
    private static String historyToString(List<Task> history) {
        String result = "";
        if (history != null) {
            List<String> id = new ArrayList<>();
            for (Task task : history) {
                id.add(Integer.toString(task.getId()));
            }
            result = String.join(",", id);

        }
        return result;
    }

    /**
     * метод создания задачи из строки
     *
     * @param value строка с задачей
     * @return задачу
     */
    private static Task fromString(String value, FileBackedTasksManager fileBacked) {
        String[] parts = value.split(",", 6);

        int id = Integer.parseInt(parts[0]);
        TypeOfTask type = TypeOfTask.valueOf(parts[1]);
        String name = parts[2];
        String description = parts[3];
        StatusOfTask status = StatusOfTask.valueOf(parts[4]);
        String epicId = parts[5].trim();


        switch (type) {
            case TASK:
                Task task = new Task();
                task.setId(id);
                task.setName(name);
                task.setStatus(status);
                task.setDescription(description);
                return task;
            case EPIC:
                Epic epic = new Epic();
                epic.setId(id);
                epic.setName(name);
                epic.setStatus(status);
                epic.setDescription(description);
                return epic;
            case SUBTASK:
                SubTask subTask = new SubTask();
                subTask.setId(id);
                subTask.setName(name);
                subTask.setStatus(status);
                subTask.setDescription(description);
                subTask.setEpicTask(fileBacked.getEpicTaskStorage().get(Integer.parseInt(epicId)));
                subTask.getEpicTask().getSubTasks().add(subTask);
                return subTask;
            default:
                return null;
        }
    }


    /**
     * метод для восстановления истории из строки
     *
     * @param value строка с историей
     * @return список id задач из истории просмотров
     */
    private static List<Integer> historyFromString(String value) {
        String[] idOfTasks = value.split(",");
        List<Integer> listId = new ArrayList<>();
        for (String id : idOfTasks) {
            if (!id.equals("")) {
                listId.add(Integer.parseInt(id));
            }
        }
        return listId;
    }


    /**
     * метод создает задачу из файла с ее первоначальным номером
     *
     * @param task задача из файла
     * @param id   номер из файла
     */
    public void createTaskFromFile(Task task, int id) {
        getTaskStorage().put(id, task);
    }


    /**
     * метод создает эпик из файла с ее первоначальным номером
     *
     * @param epic задача из файла
     * @param id   номер из файла
     */
    public void createEpicFromFile(Epic epic, int id) {
        getEpicTaskStorage().put(id, epic);
    }


    /**
     * метод создает сабтаску из файла с ее первоначальным номером
     *
     * @param subtask задача из файла
     * @param id      номер из файла
     */
    public void createSubtaskFromFile(SubTask subtask, int id) {
        getSubTasksStorage().put(id, subtask);
    }

    /**
     * Метод проверяет есть ли в мапах id. Если есть - то он его увеличивает пока не найдет свободный
     *
     * @return уникальный id
     */
    private int setActualId() {
        int uniqueId = 1;
        while (getTaskStorage().containsKey(uniqueId) || getEpicTaskStorage().containsKey(uniqueId) ||
                getSubTasksStorage().containsKey(uniqueId)) {
            uniqueId++;
        }
        return uniqueId;
    }


    @Override
    public void createTask(Task task) {
        task.setId(setActualId());
        getTaskStorage().put(setActualId(), task);
        save();

    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(setActualId());
        getEpicTaskStorage().put(setActualId(), epic);
        save();
    }

    @Override
    public void createSubtask(SubTask subtask) {
        subtask.setId(setActualId());
        getSubTasksStorage().put(setActualId(), subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        save();
        return super.getTaskById(id);

    }

    @Override
    public Epic getEpicTaskById(int id) {
        save();
        return super.getEpicTaskById(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask task = super.getSubTaskById(id);
        save();
        return task;
    }


    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicTask(Epic epic) {
        super.updateEpicTask(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicTaskById(int id) {
        super.deleteEpicTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

}

