package tests;

import managers.FileBackedTasksManager;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import static managers.FileBackedTasksManager.loadFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * проверка работы по схранению и восстановлению состояния
 */
class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    File file = new File("history.csv");

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager(file);
    }


    @AfterEach
    public void afterEach() {
        try {
            Files.delete(file.toPath());
        } catch (IOException exp) {
            System.out.println(exp.getMessage());
        }
    }

    @Test
    public void shouldSaveToFile() {
        Task task = createTestTask();
        taskManager.createTask(task);
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        assertEquals(List.of(task), taskManager.getListOfTasks(), "Задачи не записались");
        assertEquals(List.of(epic), taskManager.getListOfEpicTasks(), "Эпики не записались");
        assertEquals(List.of(subTask), taskManager.getListOfSubTasks(), "Сабтаски не записались");
    }

    @Test
    public void shouldSaveAndLoadFromFile() {
        Task task = createTestTask();
        taskManager.createTask(task);
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        FileBackedTasksManager loadManager = loadFromFile(file);
        assertEquals(List.of(task), loadManager.getListOfTasks(), "Задачи не выгрузились");
        assertEquals(List.of(epic), loadManager.getListOfEpicTasks(), "Эпики не выгрузились");
        assertEquals(List.of(subTask), loadManager.getListOfSubTasks(), "Сабтаски не выгрузились");

    }


    @Test
    public void shouldSaveAndLoadFromFileEmptyListsOfAllTypesOfTasks() {
        FileBackedTasksManager saveManager = new FileBackedTasksManager(file);
        saveManager.save();
        FileBackedTasksManager loadManager = loadFromFile(file);
        assertTrue(loadManager.getListOfTasks().isEmpty(), "Список задач не пустой");
        assertTrue(loadManager.getListOfEpicTasks().isEmpty(), "Список эпиков не пустой");
        assertTrue(loadManager.getListOfSubTasks().isEmpty(), "Список сабтасок не пустой");
    }

    @Test
    public void shouldSaveAndLoadFromFileListOfHistory() {
        Task task = createTestTask();
        taskManager.createTask(task);
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        taskManager.getTaskById(task.getId());
        taskManager.getEpicTaskById(epic.getId());
        FileBackedTasksManager loadManager = loadFromFile(file);
        assertEquals(2, loadManager.getHistory().size(), "Список истории не совпадает");
    }

    @Test
    public void shouldSaveAndLoadFromFileEmptyListOfHistory() {
        FileBackedTasksManager saveManager = new FileBackedTasksManager(file);
        saveManager.save();
        FileBackedTasksManager loadManager = loadFromFile(file);
        assertEquals(Collections.emptyList(), loadManager.getHistory(), "Список истории не пуст");
    }

}
