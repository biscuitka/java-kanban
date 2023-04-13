package tasks;


import managers.TypeOfTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * класс представляет отдельно стоящую задачу
 */
public class Task {
    private int id;
    private String name;
    private String description;
    private StatusOfTask status;
    private TypeOfTask type;
    private LocalDateTime startTime;
    private long duration;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    /**
     * в конструкторе задается статус задания NEW
     */
    public Task() {
        status = StatusOfTask.NEW;
        setType(TypeOfTask.TASK);
    }
    public LocalDateTime getEndTime(){
        return startTime.plusMinutes(duration);
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


    public String toStringInFile() {
        String format = "%s,%s,%s,%s,%s,%s,%s,%s";
        return String.format(format, getId(), type, getName(), getDescription(), getStatus(),
                getStartTime().format(formatter), getDuration()," ");
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description,
                task.description) && status == task.status && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, type);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", startTime=" + startTime.format(formatter) +
                ", duration=" + duration +
                '}';
    }
}
