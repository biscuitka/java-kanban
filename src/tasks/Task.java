package tasks;


import managers.TypeOfTask;

/**
 * класс представляет отдельно стоящую задачу
 */
public class Task {
    private int id;
    private String name;
    private String description;
    private StatusOfTask status;
    private TypeOfTask type;

    /**
     * в конструкторе задается статус задания NEW
     */
    public Task() {
        status = StatusOfTask.NEW;
        setType(TypeOfTask.TASK);
    }


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
    public TypeOfTask getType() {
        return type;
    }

    public void setType(TypeOfTask typeOfTask) {
        type = typeOfTask;
    }


    @Override
    public String toString() {
        return "Задача{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
    public String toStringInFile() {
        String format = "%s,%s,%s,%s,%s,%s";
        return String.format(format,getId(),type,getName(),getDescription(),getStatus()," ");
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
