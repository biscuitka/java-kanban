package managers;

/**
 * класс узла списка задач
 * @param <Task>
 */
public class Node<Task> {
    public Task data;
    public Node<Task> next;
    public Node<Task> prev;

    public Node(Node<Task> prev, Task data, Node<Task> next) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}