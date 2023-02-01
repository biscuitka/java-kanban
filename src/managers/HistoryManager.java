package managers;

import tasks.Task;

import java.util.List;

/**
 * интерфейс для управления историей просмотров
 */
public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
