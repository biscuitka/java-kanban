package tasks;
import manager.Manager;

/**
 * класс представляет отдельно стоящую задачу
 */
public class Task {
    private int id;
    private String name;
    private String description;
    private StatusOfTask status;

    /**
     * в конструкторе задается статус задания NEW
     */
    public Task() {
        status = StatusOfTask.NEW;
    }


    /**
     * методы get и set
     * @return поля
     */
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StatusOfTask getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(StatusOfTask status) {
        this.status = status;
    }
}
