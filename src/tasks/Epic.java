package tasks;

import managers.TypeOfTask;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * класс наследует у tasks.Task и представляет большие задачи
 */
public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic() {
        setType(TypeOfTask.EPIC);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }


    /**
     * переопределение метода для управления статусом эпика в зависимости от статусов его подзадач
     *
     * @return статус эпика
     */
    @Override
    public StatusOfTask getStatus() {
        boolean isNewStatus = true;
        boolean isDoneStatus = true;

        if (subTasks.isEmpty()) {
            return StatusOfTask.NEW;
        }

        for (Task subTask : subTasks) {
            if (subTask.getStatus() != StatusOfTask.NEW) {
                isNewStatus = false;
            }
            if (subTask.getStatus() != StatusOfTask.DONE) {
                isDoneStatus = false;
            }
        }
        if (isNewStatus && isDoneStatus) {
            return StatusOfTask.IN_PROGRESS;
        }
        if (isNewStatus) {
            return StatusOfTask.NEW;
        }
        if (isDoneStatus) {
            return StatusOfTask.DONE;
        }
        return StatusOfTask.IN_PROGRESS;
    }


    @Override
    public String toString() {
        return "Эпик{" +
                "id='" + getId() + '\'' +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subTasks=" + subTasks +
                '}';
    }
    @Override
    public String toStringInFile() {
        String format = "%s,%s,%s,%s,%s,%s";
        return String.format(format, getId(), getType(), getName(), getDescription(), getStatus(), " ");

    }
    @Override
    public LocalDateTime getEndTime() {
        if (subTasks.size() == 0) {
            return null;
        }
        LocalDateTime endTime = subTasks.get(0).getEndTime();
        for (SubTask subTask : subTasks) {
            if (subTask.getEndTime().isAfter(endTime))
                endTime = subTask.getEndTime();
        }
        return endTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (subTasks.size() == 0) {
            return null;
        }
        LocalDateTime startTime = subTasks.get(0).getStartTime();
        for (SubTask subTask : subTasks) {
            if (subTask.getStartTime().isBefore(startTime)){
                startTime = subTask.getStartTime();
            }
        }
        return startTime;
    }

    @Override
    public long getDuration() {
        if (subTasks.size() == 0) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(getStartTime(), getEndTime());
    }

}

