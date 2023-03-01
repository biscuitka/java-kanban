package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    private final HashMap<Integer, Node> incomingTask = new HashMap<>();

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
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        incomingTask.put(task.getId(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
    }

    /**
     * метод принимает узел связного списка и вырезает его
     *
     * @param node узел задачи
     */
    private void removeNode(Node node) {
        if (node != null) {
            final Node prev = node.getPrev();
            final Node next = node.getNext();

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (tail == node) {
                tail = prev;
                tail.setNext(null);
            } else if (head == node) {
                head = next;
                head.setPrev(null);
            } else {
                prev.setNext(next);
                next.setPrev(prev);
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
        Node actualNode = head;

        while (actualNode != null) {
            taskList.add(actualNode.getData());
            actualNode = actualNode.getNext();
        }
        return taskList;
    }
}




