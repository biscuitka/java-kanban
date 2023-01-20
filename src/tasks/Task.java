package tasks;

/**
 * класс представляет отдельно стоящую задачу
 */
public class Task {
    private String name;
    private String taskDescription;
    private StatusOfTask statusOfTask;

    /**
     * конструктор без параметров
     */
    public Task() {
    }

    /**
     * методы get и set
     * @return поля
     */
    public String getName() {
        return name;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public StatusOfTask getStatusOfTask() {
        return statusOfTask;
    }

    public void setStatusOfTask(StatusOfTask statusOfTask) {
        this.statusOfTask = statusOfTask;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @Override
    public String toString() {
        return "tasks.Task{" +
                "name='" + name + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", statusOfTask=" + statusOfTask +
                '}';
    }
}
