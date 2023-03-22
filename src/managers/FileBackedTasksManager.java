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

        File file = new File("history.csv");
        // Создаем пустой FileBackedTasksManager, который будет сохранять данные в histrory.csv
        FileBackedTasksManager saveManager = new FileBackedTasksManager(file);

        //создание задач разных типов для записи в файл
        Task taskOne = new Task();
        taskOne.setName("простая задача1");
        taskOne.setDescription("описание задачи1");
        saveManager.createTask(taskOne);

        Task taskTwo = new Task();
        taskTwo.setName("простая задача2");
        taskTwo.setDescription("описание задачи2");
        saveManager.createTask(taskTwo);

        Epic epicTaskOne = new Epic();
        epicTaskOne.setName("Эпик1");
        epicTaskOne.setDescription("Описание эпика1");
        saveManager.createEpic(epicTaskOne);

        SubTask subtaskOne = new SubTask();
        subtaskOne.setName("Подзадача1 эпика1");
        subtaskOne.setDescription("описание подзадачи1");
        subtaskOne.setEpicTask(epicTaskOne);
        epicTaskOne.getSubTasks().add(subtaskOne);
        saveManager.createSubtask(subtaskOne);

        SubTask subtaskTwo = new SubTask();
        subtaskTwo.setName("подзадача2 эпика1");
        subtaskTwo.setDescription("описание подзадачи2");
        subtaskTwo.setEpicTask(epicTaskOne);
        epicTaskOne.getSubTasks().add(subtaskTwo);
        saveManager.createSubtask(subtaskTwo);

        Epic epicTaskTwo = new Epic();
        epicTaskTwo.setName("Эпик2");
        epicTaskTwo.setDescription("описание эпика2");
        saveManager.createEpic(epicTaskTwo);


        System.out.println("тест создания tasks- " + "\n" + taskOne + "\n" + taskTwo);
        System.out.println();

        System.out.println("тест создания epicTask - " + "\n" + epicTaskOne + "\n" + epicTaskTwo);
        System.out.println();

        System.out.println("тест создания subTask - " + "\n" + subtaskOne + "\n" + subtaskTwo);
        System.out.println();

        // Создаем второй FileBackedTasksManager, который прочитает все что есть в histrory.csv
        FileBackedTasksManager loadManager = loadFromFile(file);

        // Вывод всех задач и истории из saveManager и из loadManager

        System.out.println("тестирование FileBackedTasksManager из loadManager");
        System.out.println("список простых задач- " + "\n" + loadManager.getListOfTasks());
        System.out.println("список эпиков- " + "\n" + loadManager.getListOfEpicTasks());
        System.out.println("список сабтасок- " + "\n" + loadManager.getListOfSubTasks());
        System.out.println();

        System.out.println("тестируем получение по id 2 " + loadManager.getTaskById(2));
        System.out.println();

        System.out.println("тестируем получение по id 6 " + loadManager.getEpicTaskById(6));
        System.out.println();
        System.out.println("подзадачи определенного эпика - " + loadManager.getSubtasksOfEpic(3));
        System.out.println();
        System.out.println("История просмотров задач:\n" + loadManager.getHistory());
        System.out.println();

        System.out.println("тестирование FileBackedTasksManager из saveManager");
        System.out.println("список простых задач- " + "\n" + saveManager.getListOfTasks());
        System.out.println("список эпиков- " + "\n" + saveManager.getListOfEpicTasks());
        System.out.println("список сабтасок- " + "\n" + saveManager.getListOfSubTasks());
        System.out.println();

        System.out.println("тестируем получение по id 2 " + saveManager.getTaskById(2));
        System.out.println();

        System.out.println("подзадачи определенного эпика - " + saveManager.getSubtasksOfEpic(3));
        System.out.println();

        System.out.println("тестируем получение по id 6 " + saveManager.getEpicTaskById(6));
        System.out.println();

        System.out.println("История просмотров задач:\n" + saveManager.getHistory());


        // для тестирования удаления на примере loadManager
       /*
        System.out.println("дальше удаляем по номеру задачу(2)");
        loadManager.deleteTaskById(2);
        System.out.println("История просмотров задач после удаления задачи(2):\n" + loadManager.getHistory());
        System.out.println();

        System.out.println("дальше снова удаляем по номеру эпик(1, id3)");
        loadManager.deleteEpicTaskById(3);
        System.out.println("История просмотров задач после удаления эпика(1, id3), с ней должна удалиться " +
                "ее подзадача1:\n" + loadManager.getHistory());

        System.out.println("список простых задач после удаления по номеру- " + "\n" + loadManager.getListOfTasks());
        System.out.println("список эпиков после удаления по номеру- " + "\n" + loadManager.getListOfEpicTasks());
        System.out.println("список сабтасок после удаления по номеру- " + "\n" + loadManager.getListOfSubTasks());
        System.out.println("История просмотров задач:\n" + loadManager.getHistory());
        System.out.println();

        System.out.println("тестируем полное удаление");
        loadManager.deleteAllTasks();
        loadManager.deleteAllEpicTasks();
        loadManager.deleteAllSubTasks();

        System.out.println("список простых задач после удаления- " + "\n" + loadManager.getListOfTasks());
        System.out.println("список эпиков после удаления- " + "\n" + loadManager.getListOfEpicTasks());
        System.out.println("список сабтасок после удаления- " + "\n" + loadManager.getListOfSubTasks());
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
                    id = task.getId();
                    tasksManager.createTaskFromFile(task, id);
                } else if (type.equalsIgnoreCase("EPIC")) {
                    Epic epic = (Epic) fromString(line, tasksManager);
                    id = epic.getId();
                    tasksManager.createEpicFromFile(epic, id);
                } else if (type.equalsIgnoreCase("SUBTASK")) {
                    SubTask subTask = (SubTask) fromString(line, tasksManager);
                    id = subTask.getId();
                    tasksManager.createSubtaskFromFile(subTask, id);
                }
            } else {
                historyLine = line;
            }
        }
        List<Integer> idOfTasks = historyFromString(historyLine);
        for (Integer idHistory : idOfTasks) {
            tasksManager.addTaskToHistoryById(idHistory);
        }
        return tasksManager;
    }


    private void addTaskToHistoryById(int id) {
        if (taskStorage.containsKey(id)) {
            getTaskById(id);
        }
        if (epicTaskStorage.containsKey(id)) {
            getEpicTaskById(id);
        }
        if (subTasksStorage.containsKey(id)) {
            getSubTaskById(id);
        }
        throw new RuntimeException("Задача с запрошенным Id не найдена");
    }


    /**
     * метод перевода истории в строку
     *
     * @return строка с историей просмотров
     */
    private static String historyToString(List<Task> history) {
        String result = "";
        if (history != null) {
            List<String> identifiers = new ArrayList<>();
            for (Task task : history) {
                identifiers.add(Integer.toString(task.getId()));
            }
            result = String.join(",", identifiers);

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
                subTask.setEpicTask(fileBacked.epicTaskStorage.get(Integer.parseInt(epicId)));
                subTask.getEpicTask().getSubTasks().add(subTask);
                return subTask;
            default:
                throw new RuntimeException("Задача с запрошенным Id не найдена");
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
        taskStorage.put(id, task);
    }


    /**
     * метод создает эпик из файла с ее первоначальным номером
     *
     * @param epic задача из файла
     * @param id   номер из файла
     */
    public void createEpicFromFile(Epic epic, int id) {
        epicTaskStorage.put(id, epic);
    }


    /**
     * метод создает сабтаску из файла с ее первоначальным номером
     *
     * @param subtask задача из файла
     * @param id      номер из файла
     */
    public void createSubtaskFromFile(SubTask subtask, int id) {
        subTasksStorage.put(id, subtask);
    }


    @Override
    public void createTask(Task task) {
        task.setId(setActualId());
        taskStorage.put(setActualId(), task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(setActualId());
        epicTaskStorage.put(setActualId(), epic);
        save();
    }

    @Override
    public void createSubtask(SubTask subtask) {
        subtask.setId(setActualId());
        subTasksStorage.put(setActualId(), subtask);
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
        Task task = super.getTaskById(id);
        save();
        return task;

    }

    @Override
    public Epic getEpicTaskById(int id) {
        Epic epic = super.getEpicTaskById(id);
        save();
        return epic;
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

