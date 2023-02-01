package managers;

import tasks.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private LinkedList<Task> browsingHistory = new LinkedList<>();
    @Override
    public void add(Task task) {
        if (browsingHistory.size() < 10) {
            browsingHistory.addLast(task);
        } else {
            browsingHistory.removeFirst();
            browsingHistory.addLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory;
    }
}
