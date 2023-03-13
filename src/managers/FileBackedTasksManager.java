package managers;

import tasks.Epic;
import tasks.StatusOfTask;
import tasks.SubTask;
import tasks.Task;


import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;

/**
 * класс для автосохранения в файл
 */
public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    private static final String FIRST_lINE = "id,type,name,description,status,epic\n";


    /**
     * менеджер получает файл в конструкторе и сохраняет его в поле
     *
     * @param file файл
     */
    public FileBackedTasksManager(File file) {
        this.file = file;
    }


    /**
     * Сохраняет текущее состояние менеджера в файл.
     * Сохраняет все типы задач и историю просмотров в файл csv
     */
    public void save() {

        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(FIRST_lINE);

            for (Task task : getListOfTasks()) {
                fileWriter.write(toStringInFile(task) + "\n");
            }
            for (Task epic : getListOfEpicTasks()) {
                fileWriter.write(toStringInFile(epic) + "\n");
            }
            for (Task subtask : getListOfSubTasks()) {
                fileWriter.write(toStringInFile(subtask) + "\n");
            }

            fileWriter.write("\n");
            String history = historyToString(getHistory());
            fileWriter.write(history);


        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            throw new ManagerSaveException("Ошибка создания файла");
        }

    }

    /**
     * метод сохранения задачи в строку
     *
     * @return
     */
    public String toStringInFile(Task task) {
        TypeOfTask type;
        String format = "%s,%s,%s,%s,%s,%s";

        if (task instanceof Epic) {
            type = TypeOfTask.EPIC;
        } else if (task instanceof SubTask) {
            type = TypeOfTask.SUBTASK;
        } else {
            type = TypeOfTask.TASK;
        }
        return String.format(format, task.getId(), type, task.getName(),
                task.getDescription(), task.getStatus(), " ");
    }


    /**
     * восстанавливает данные менеджера из файла при запуске программы
     *
     * @param file
     * @return
     */
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);


        if (!file.exists()) {
            return tasksManager;
        }

        try (BufferedReader buffer = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            buffer.readLine();
            boolean isTask = true;
            while (buffer.ready()) {
                String taskLine = buffer.readLine();
                if (taskLine.isEmpty()) {
                    isTask = false;
                }
                if (isTask) {
                    if (taskLine.equals(FIRST_lINE)) {
                        continue;
                    }
                    //int id = Integer.parseInt(taskLine.split(",")[0]);
                    String type = String.valueOf(taskLine.split(",")[1]);
                    if (type.equalsIgnoreCase("TASK")) {
                        Task task = fromString(taskLine);
                        tasksManager.createTask(task);
                    } else if (type.equalsIgnoreCase("EPIC")) {
                        Epic epic = (Epic) fromString(taskLine);
                        tasksManager.createEpic(epic);
                    } else {
                        SubTask subTask = (SubTask) fromString(taskLine);
                        tasksManager.createSubtask(subTask);
                    }
                } else {
                    String historyLine = taskLine;
                    List<Integer> idOfTasks = historyFromString(historyLine);
                    for (Integer id : idOfTasks){
                        Task task = tasksManager.getSubTaskById(id);
                        if (task != null) {
                            tasksManager.getHistoryManager().add(task);
                        }
                    }
                }
            }

        } catch (IOException exp) {
            System.out.println(exp.getMessage());
            throw new ManagerSaveException("Ошибка чтения файла.");
        }
        return tasksManager;
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
    private static Task fromString(String value) {
        String[] parts = value.split(","); //создаем массив из значений входящей строки
        int id = Integer.parseInt(parts[0]);
        TypeOfTask type = TypeOfTask.valueOf(parts[1]);
        String name = parts[2];
        String description = parts[3];
        StatusOfTask status = StatusOfTask.valueOf(parts[4]);

        //int epicId = Integer.parseInt(parts[5].trim());


        if (type.equals(TypeOfTask.TASK)) {
            Task task = new Task();
            task.setId(id);
            task.setName(name);
            task.setStatus(status);
            task.setDescription(description);
            return task;
        } else if (type.equals(TypeOfTask.EPIC)) {
            Epic epic = new Epic();
            epic.setId(id);
            epic.setName(name);
            epic.setStatus(status);
            epic.setDescription(description);
            return epic;
        } else if (type.equals(TypeOfTask.SUBTASK)) {
            SubTask subTask = new SubTask();
            subTask.setId(id);
            subTask.setName(name);
            subTask.setStatus(status);
            subTask.setDescription(description);
            //subTask.setEpicTask(epicId);
            return subTask;
        } else {
            return null;
        }
    }


    /**
     * метод для восстановления истории из строки
     *
     * @param value строка с историей
     * @return список id задач из истории просмотров
     */
    static List<Integer> historyFromString(String value) {
        String[] idOfTasks = value.split(",");
        List<Integer> listId = new ArrayList<>();
        for (String id : idOfTasks) {
            if (!id.equals("")) {
                listId.add(Integer.parseInt(id));
            }
        }
        return listId;
    }

    @Override
    public void createTask(Task task) {

        super.createTask(task);
        save();

    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(SubTask subtask) {
        super.createSubtask(subtask);
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
        save();
        return super.getSubTaskById(id);
    }

    @Override
    public ArrayList<SubTask> getSubtasksOfEpic(int id) {
        save();
        return super.getSubtasksOfEpic(id);
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
