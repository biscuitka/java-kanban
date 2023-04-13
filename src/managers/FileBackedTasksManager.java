package managers;

import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Subtask;
import tasks.Task;


import java.io.*;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


/**
 * класс для автосохранения в файл
 */
public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    private static final String DEFAULT_FILE_NAME = "history.csv";
    private static final String FIRST_lINE = "id,type,name,description,status,startTime,duration,epicId\n";

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager() {
        this.file = new File(DEFAULT_FILE_NAME);
    }

    public static void main(String[] args) {

        File file = new File("history.csv");
        // Создаем пустой FileBackedTasksManager, который будет сохранять данные в histrory.csv
        FileBackedTasksManager saveManager = new FileBackedTasksManager(file);

        //создание задач разных типов для записи в файл
        Task taskOne = new Task();
        taskOne.setName("простая задача1");
        taskOne.setDescription("описание задачи1");
        taskOne.setStartTime(LocalDateTime.now());
        taskOne.setDuration(30);
        saveManager.createTask(taskOne);

        Task taskTwo = new Task();
        taskTwo.setName("простая задача2");
        taskTwo.setDescription("описание задачи2");
        taskTwo.setStartTime(LocalDateTime.now().plusMinutes(30));
        taskTwo.setDuration(10);
        saveManager.createTask(taskTwo);

        Epic epicTaskOne = new Epic();
        epicTaskOne.setName("Эпик1");
        epicTaskOne.setDescription("Описание эпика1");
        saveManager.createEpic(epicTaskOne);

        Subtask subtaskOne = new Subtask();
        subtaskOne.setName("Подзадача1 эпика1");
        subtaskOne.setDescription("описание подзадачи1");
        subtaskOne.setStartTime(LocalDateTime.now().plusMinutes(90));
        subtaskOne.setDuration(10);
        subtaskOne.setEpicId(epicTaskOne.getId());
        saveManager.createSubtask(subtaskOne);


        Subtask subtaskTwo = new Subtask();
        subtaskTwo.setName("подзадача2 эпика1");
        subtaskTwo.setDescription("описание подзадачи2");
        subtaskTwo.setStartTime(LocalDateTime.of(2023, Month.MARCH, 28, 9, 10));
        subtaskTwo.setDuration(10);
        subtaskTwo.setEpicId(epicTaskOne.getId());
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
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания файла");
        }

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
            throw new ManagerSaveException("Не удалось найти файл для записи данных");
        }

    }

    /**
     * восстанавливает данные менеджера из файла при запуске программы
     *
     * @return менеджер
     */
    public static FileBackedTasksManager loadFromFile(File file) {
        if (file.exists()) {
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
                        Task task = tasksManager.fromString(line);
                        id = task.getId();
                        tasksManager.createTaskFromFile(task, id);
                    } else if (type.equalsIgnoreCase("EPIC")) {
                        Epic epic = (Epic) tasksManager.fromString(line);
                        id = epic.getId();
                        tasksManager.createEpicFromFile(epic, id);
                    } else if (type.equalsIgnoreCase("SUBTASK")) {
                        Subtask subTask = (Subtask) tasksManager.fromString(line);
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
        } else {
            return new FileBackedTasksManager(file);
        }
    }


    private void addTaskToHistoryById(int id) {
        if (taskStorage.containsKey(id)) {
            getTaskById(id);
            return;
        }
        if (epicTaskStorage.containsKey(id)) {
            getEpicTaskById(id);
            return;
        }
        if (subTasksStorage.containsKey(id)) {
            getSubTaskById(id);
            return;
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
                identifiers.add(java.lang.Integer.toString(task.getId()));
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
    private Task fromString(String value) {
        String[] parts = value.split(",", 8);
        TypeOfTask type = TypeOfTask.valueOf(parts[1]);
        Task createdTask;
        switch (type) {
            case TASK:
                createdTask = new Task();
                break;
            case EPIC:
                createdTask = new Epic();
                break;
            case SUBTASK:
                createdTask = new Subtask();
                ((Subtask) createdTask).setEpicId(Integer.parseInt(parts[7].trim()));
                break;
            default:
                throw new RuntimeException("Задача с запрошенным Id не найдена");
        }
        createdTask.setId(java.lang.Integer.parseInt(parts[0]));
        createdTask.setName(parts[2]);
        createdTask.setDescription(parts[3]);
        createdTask.setStatus(StatusOfTask.valueOf(parts[4]));

        if (type == TypeOfTask.TASK || type == TypeOfTask.SUBTASK) {
            createdTask.setStartTime(LocalDateTime.parse(parts[5], Task.formatter));
            createdTask.setDuration(Long.parseLong(parts[6]));
        }
        return createdTask;
    }


    /**
     * метод для восстановления истории из строки
     *
     * @param value строка с историей
     * @return список id задач из истории просмотров
     */
    private static List<java.lang.Integer> historyFromString(String value) {
        String[] idOfTasks = value.split(",");
        List<java.lang.Integer> listId = new ArrayList<>();
        for (String id : idOfTasks) {
            if (!id.equals("")) {
                listId.add(java.lang.Integer.parseInt(id));
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
    public void createSubtaskFromFile(Subtask subtask, int id) {
        subTasksStorage.put(id, subtask);
    }


    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int id = super.createSubtask(subtask);
        save();
        return id;
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
    public Subtask getSubTaskById(int id) {
        Subtask task = super.getSubTaskById(id);
        save();
        return task;
    }


    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(Subtask subTask) {
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

