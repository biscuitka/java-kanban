package tests;

import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    HistoryManager historyManager;
    private static int id = 1;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    protected Task createTestTask() {
        Task task = new Task();
        task.setName("простая задача");
        task.setDescription("описание простой задачи");
        task.setId(id++);
        return task;
    }

    protected Epic createTestEpic() {
        Epic epicTask = new Epic();
        epicTask.setName("Эпик");
        epicTask.setDescription("Описание эпика");
        epicTask.setId(id++);
        return epicTask;
    }

    protected Subtask createTestSubtask() {
        Subtask subtask = new Subtask();
        subtask.setName("Подзадача эпика");
        subtask.setDescription("описание подзадачи");
        subtask.setId(id++);
        return subtask;
    }

    @Test
    public void shouldReturnEmptyHistory() {
        assertEquals(Collections.emptyList(), historyManager.getHistory(), "задачи не добавлены, " +
                "но история не пустая");
        Task task = createTestTask();
        historyManager.add(task);
        historyManager.remove(task.getId());
        assertEquals(Collections.emptyList(), historyManager.getHistory(), "задача добавлена и удалена, " +
                "но история не пустая");
    }

    @Test
    public void shouldReturnHistoryWithoutDuplicateTask() {
        Task task = createTestTask();
        Epic epic = createTestEpic();
        Subtask subTask = createTestSubtask();
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.add(task);
        historyManager.add(epic);

        assertEquals(List.of(subTask,task,epic), historyManager.getHistory());

    }

    @Test
    public void shouldRemoveTask() {
        Task task = createTestTask();
        Task task1 = createTestTask();
        Epic epic = createTestEpic();
        Epic epic1 = createTestEpic();
        Subtask subTask = createTestSubtask();
        Subtask subTask1 = createTestSubtask();

        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(epic1);
        historyManager.add(subTask);
        historyManager.add(subTask1);
        historyManager.remove(epic.getId());
        assertEquals(List.of(task,task1,epic1,subTask,subTask1), historyManager.getHistory(), "ошибка после " +
                "удаления из середины");
        historyManager.remove(task.getId());
        assertEquals(List.of(task1,epic1,subTask,subTask1), historyManager.getHistory(), "ошибка после удаления " +
                "с начала");
        historyManager.remove(subTask1.getId());
        assertEquals(List.of(task1,epic1,subTask), historyManager.getHistory(), "ошибка после удаления " +
                "с конца");
    }

}
