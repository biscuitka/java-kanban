package tests;

import managers.TaskManager;
import managers.TimeIntersectionException;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {

    /**
     * нужный менеджер назначается в наследнике
     */
    protected T taskManager;
    private static int id = 1;

    protected static Task createTestTask() {
        Task task = new Task();
        task.setId(id++);
        task.setName("простая задача");
        task.setDescription("описание простой задачи");
        task.setStartTime(LocalDateTime.of(2000, Month.JUNE, 1, 10, 20));
        task.setDuration(10);
        return task;
    }

    protected static Epic createTestEpic() {
        Epic epicTask = new Epic();
        epicTask.setId(id++);
        epicTask.setName("Эпик");
        epicTask.setDescription("Описание эпика");
        return epicTask;
    }

    protected static Subtask createTestSubtask() {
        Subtask subtask = new Subtask();
        subtask.setId(id++);
        subtask.setName("Подзадача эпика");
        subtask.setDescription("описание подзадачи");
        subtask.setStartTime(LocalDateTime.of(2000, Month.JUNE, 1, 10, 20));
        subtask.setDuration(20);
        return subtask;
    }


    @Test
    void shouldCreateTask() {
        Task task = createTestTask();
        taskManager.createTask(task);
        final List<Task> tasks = taskManager.getListOfTasks();
        final int taskId = taskManager.createTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertNotNull(task.getStatus(), "статус не задан");
        assertEquals(StatusOfTask.NEW, task.getStatus(), "статус не совпадает");
        assertEquals(List.of(task), tasks, "Списки задач не совпали");
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");


    }

    @Test
    void shouldCreateEpic(){
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        final List<Task> epics = taskManager.getListOfEpicTasks();
        final int epicId = taskManager.createEpic(epic);
        final Task savedEpic = taskManager.getEpicTaskById(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertNotNull(epic.getStatus(), "статус не задан");
        assertEquals(StatusOfTask.NEW, epic.getStatus(), "статус не совпадает");
        assertEquals(List.of(epic), epics, "Списки эпиков не совпали");
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(Collections.emptyList(), epic.getSubTasksIds(), "у нового эпика список сабтасок не пуст");
    }

    @Test
    void shouldCreateSubtask() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        final List<Task> subtasks = taskManager.getListOfSubTasks();
        final int subtaskId = taskManager.createSubtask(subTask);
        final Task savedSubtask = taskManager.getSubTaskById(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subTask, savedSubtask, "Задачи не совпадают.");
        assertNotNull(subTask.getStatus(), "статус не задан");
        assertEquals(StatusOfTask.NEW, subTask.getStatus(), "статус не совпадает");
        assertEquals(List.of(subTask), subtasks, "Списки сабтасок не совпали");
        assertNotNull(subtasks, "Сабтаски не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтасок.");
        assertEquals(epic.getId(), subTask.getEpicId(), "номер эпика у сабтаски не совпадает");
        assertEquals(List.of(2,3), epic.getSubTasksIds(), "список сабтасок у эпика не совпадает");
    }

    @Test
    void shouldDeleteAllTasks() {
        Task task = createTestTask();
        taskManager.createTask(task);
        taskManager.deleteAllTasks();
        assertEquals(Collections.emptyList(), taskManager.getListOfTasks(), "список задач не пуст");
    }

    @Test
    void shouldDeleteAllEpicTasksAndTheirSubtasks() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);

        taskManager.deleteAllEpicTasks();
        assertEquals(Collections.emptyList(), taskManager.getListOfEpicTasks(), "список эпиков не пуст");
        assertEquals(Collections.emptyList(), taskManager.getListOfSubTasks(), "список сабтасок эпика не пуст");
    }

    @Test
    void shouldDeleteAllSubTasksAndTheirLinkWithEpic() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);

        taskManager.deleteAllSubTasks();
        assertEquals(Collections.emptyList(), taskManager.getListOfSubTasks(), "список сабтасок не пуст");
        assertEquals(Collections.emptyList(), taskManager.getSubtasksOfEpic(epic.getId()),
                "список сабтасок эпика не пуст");
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        Task task = createTestTask();
        taskManager.createTask(task);
        assertEquals(task, taskManager.getTaskById(task.getId()), "вызов задачи по номеру не совпадает");
    }

    @Test
    void shouldGetEpicTaskById() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        assertEquals(epic, taskManager.getEpicTaskById(epic.getId()), "вызов эпика по номеру не совпадает");
    }

    @Test
    void shouldGetSubTaskById() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        assertEquals(subTask, taskManager.getSubTaskById(subTask.getId()), "вызов сабтаски по номеру " +
                "не совпадает");
    }

    @Test
    void shouldGetSubtasksOfEpic() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        assertEquals(List.of(subTask), taskManager.getSubtasksOfEpic(epic.getId()),
                "сабтаски эпика не совпадают");
    }

    @Test
    void shouldUpdateStatusOfTaskToInProgress() {
        Task task = createTestTask();
        taskManager.createTask(task);
        task.setStatus(StatusOfTask.IN_PROGRESS);
        taskManager.updateTask(task);
        assertEquals(StatusOfTask.IN_PROGRESS, taskManager.getTaskById(task.getId()).getStatus());
    }

    @Test
    void shouldUpdateStatusOfTaskToDone() {
        Task task = createTestTask();
        taskManager.createTask(task);
        task.setStatus(StatusOfTask.DONE);
        taskManager.updateTask(task);
        assertEquals(StatusOfTask.DONE, taskManager.getTaskById(task.getId()).getStatus());
    }

    @Test
    void shouldUpdateStatusOfEpicAndSubtaskToInProgress() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        subTask.setStatus(StatusOfTask.IN_PROGRESS);
        taskManager.updateSubTask(subTask);
        assertEquals(StatusOfTask.IN_PROGRESS, taskManager.getSubTaskById(subTask.getId()).getStatus());
        assertEquals(StatusOfTask.IN_PROGRESS, taskManager.getEpicTaskById(epic.getId()).getStatus());
    }

    @Test
    void shouldUpdateStatusOfEpicToInProgressIfOneSubtaskInProgress() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask1 = createTestSubtask();
        Subtask subTask2 = createTestSubtask();
        subTask1.setEpicId(epic.getId());
        subTask2.setEpicId(epic.getId());
        taskManager.createSubtask(subTask1);
        taskManager.createSubtask(subTask2);
        subTask1.setStatus(StatusOfTask.IN_PROGRESS);
        subTask2.setStatus(StatusOfTask.DONE);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);
        assertEquals(StatusOfTask.IN_PROGRESS, taskManager.getEpicTaskById(epic.getId()).getStatus());
    }

    @Test
    void shouldUpdateStatusOfEpicToInProgressIfSubtasksNewAndDone() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask1 = createTestSubtask();
        Subtask subTask2 = createTestSubtask();
        subTask1.setEpicId(epic.getId());
        subTask2.setEpicId(epic.getId());
        taskManager.createSubtask(subTask1);
        taskManager.createSubtask(subTask2);
        subTask2.setStatus(StatusOfTask.DONE);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);
        assertEquals(StatusOfTask.IN_PROGRESS, taskManager.getEpicTaskById(epic.getId()).getStatus());
    }


    @Test
    void shouldNotChangeStatusOfEpicIfListOfSubtaskIsEmpty() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        taskManager.updateEpic(epic);
        assertEquals(StatusOfTask.NEW, taskManager.getEpicTaskById(epic.getId()).getStatus());
    }

    @Test
    void shouldUpdateDurationStartTimeEndTimeOfEpicTask() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        taskManager.updateSubTask(subTask);
        assertEquals(20, taskManager.getEpicTaskById(epic.getId()).getDuration(taskManager), "некорректная" +
                "продолжительность эпика");
        assertEquals(LocalDateTime.of(2000, Month.JUNE, 1, 10, 20),
                taskManager.getEpicTaskById(epic.getId()).getStartTime(taskManager), "некорректный старт");
        assertEquals(LocalDateTime.of(2000, Month.JUNE, 1, 10, 40),
                taskManager.getEpicTaskById(epic.getId()).getEndTime(taskManager), "некорректный финиш");
    }

    @Test
    void shouldNotUpdateDurationStartTimeEndTimeOfEpicTaskWithoutSubtasks() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        assertEquals(0, taskManager.getEpicTaskById(epic.getId()).getDuration(taskManager), "продолжительность" +
                "не равна нулю");
        assertNull(taskManager.getEpicTaskById(epic.getId()).getStartTime(taskManager), "старт не null");
        assertNull(taskManager.getEpicTaskById(epic.getId()).getEndTime(taskManager), "финиш не null");
    }

    @Test
    void shouldDeleteTaskById() {
        Task task = createTestTask();
        taskManager.createTask(task);
        taskManager.deleteTaskById(task.getId());
        assertFalse(taskManager.getListOfTasks().contains(task), "задача не удалилась");
    }

    @Test
    void shouldDeleteEpicTaskByIdAndHisSubtasks() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);

        taskManager.deleteEpicTaskById(epic.getId());
        assertFalse(taskManager.getListOfEpicTasks().contains(epic), "эпик не удалился");
        assertTrue(epic.getSubTasksIds().isEmpty(), "сабтаски эпика не удалились");
    }

    @Test
    void shouldDeleteSubTaskByIdAndTheirLinkWithEpic() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);

        taskManager.deleteSubTaskById(subTask.getId());
        assertFalse(taskManager.getListOfSubTasks().contains(subTask), "сабтаска не удалилась");
        assertTrue(epic.getSubTasksIds().isEmpty(), "сабтаска у эпика не удалилась");
    }

    @Test
    void shouldNotDeleteTaskByWrongId() {
        Task task = createTestTask();
        taskManager.createTask(task);
        taskManager.deleteTaskById(0);
        assertTrue(taskManager.getListOfTasks().contains(task), "задача удалилась по ложному номеру");
    }

    @Test
    void shouldNotDeleteEpicTaskByWrongId() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        taskManager.deleteEpicTaskById(0);
        assertTrue(taskManager.getListOfEpicTasks().contains(epic), "эпик удалился по ложному номеру");
    }

    @Test
    void shouldNotDeleteSubTaskByWrongId() {
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);

        taskManager.deleteSubTaskById(0);
        assertTrue(taskManager.getListOfSubTasks().contains(subTask), "сабтаска удалилась по ложному номеру");
    }

    @Test
    void shouldReturnEmptyHistoryIfNoTasks() {
        taskManager.getTaskById(0);
        taskManager.getSubTaskById(0);
        taskManager.getEpicTaskById(0);
        assertTrue(taskManager.getHistory().isEmpty());
    }


    @Test
    void shouldGetHistoryWithTasks() {
        Task task = createTestTask();
        taskManager.createTask(task);
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);

        taskManager.getTaskById(task.getId());
        taskManager.getEpicTaskById(epic.getId());
        taskManager.getSubTaskById(subTask.getId());
        List<Task> history = taskManager.getHistory();
        assertFalse(history.isEmpty(), "задачи в историю не добавились");
        assertEquals(3, history.size());
        assertTrue(history.contains(task), "простая задача в историю не добавилась");
        assertTrue(history.contains(epic), "эпик в историю не добавился");
        assertTrue(history.contains(subTask), "сабтаска в историю не добавилась");
    }

    @Test
    void shouldThrowExceptionWhenTimeIntersection() {
        Task task1 = new Task();
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(60);
        taskManager.createTask(task1);

        Task task2 = new Task();
        task2.setStartTime(LocalDateTime.now().plusMinutes(30));

        final TimeIntersectionException exception = assertThrows(
                TimeIntersectionException.class,
                () -> taskManager.createTask(task2));

        assertEquals("Пересечение по времени с другими задачами", exception.getMessage());
    }

    @Test
    void shouldGetListPrioritizedTasks(){
        Task task1 = new Task();
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(10);
        taskManager.createTask(task1);
        Task task2 = new Task();
        task2.setStartTime(LocalDateTime.now().minusMinutes(30));
        task2.setDuration(10);
        taskManager.createTask(task2);
        Task task3 = new Task();
        task3.setStartTime(LocalDateTime.now().minusHours(2));
        task3.setDuration(20);
        taskManager.createTask(task3);
        Epic epic = createTestEpic();
        taskManager.createEpic(epic);
        Subtask subTask = createTestSubtask();
        subTask.setEpicId(epic.getId());
        subTask.setStartTime(LocalDateTime.now().minusDays(1));
        subTask.setDuration(90);
        taskManager.createSubtask(subTask);
        assertEquals(List.of(subTask, task3, task2, task1), taskManager.getPrioritizedTasks(),
                "задачи не отсортировались по времени");
    }
}




