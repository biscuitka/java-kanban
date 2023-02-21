package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public Node<Task> head;
    public Node<Task> tail;
    private final HashMap<Integer, Node<Task>> incomingTask = new HashMap<>();

    /**
     * метод добавляет задачу в список и исключая повторные просмотры - узел и саму задачу из мапы
     *
     * @param task задача
     */
    @Override
    public void add(Task task) {
        if (incomingTask.containsKey(task.getId())) {
            removeNode(incomingTask.get(task.getId()));
            incomingTask.remove(task.getId());
        }
        linkLast(task);
    }

    /**
     * для удаления задачи из просмотра.
     *
     * @param id номер задачи
     */
    @Override
    public void remove(int id) {
        removeNode(incomingTask.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    /**
     * метод добавляет задачу в конец списка
     *
     * @param task задача
     */
    private void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        incomingTask.put(task.getId(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    /**
     * метод принимает узел связного списка и вырезает его
     *
     * @param node узел задачи
     */
    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> prev = node.prev;
            final Node<Task> next = node.next;

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (tail == node) {
                tail = prev;
                tail.next = null;
            } else if (head == node) {
                head = next;
                head.prev = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }

        }
    }

    /**
     * будет собирать задачи из списка в обычный ArrayList, чтобы вывести историю в том порядке,
     * в котором она создавалась, собираем задачи с головы
     *
     * @return список задач
     */
    private List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        Node<Task> actualNode = head;

        while (actualNode != null) {
            taskList.add(actualNode.data);
            actualNode = actualNode.next;
        }
        return taskList;
    }
}




