package tasks;

import managers.Managers;
import managers.TaskManager;
import managers.TypeOfTask;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * класс наследует у tasks.Task и представляет большие задачи
 */
public class Epic extends Task {
    private ArrayList<Integer> subTasksIds = new ArrayList<>();

    public Epic() {
        setType(TypeOfTask.EPIC);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void setSubTasksIds(ArrayList<Integer> subTasksIds) {
        this.subTasksIds = subTasksIds;
    }


    /**
     * переопределение метода для управления статусом эпика в зависимости от статусов его подзадач
     *
     * @return статус эпика
     */

    public void updateStatus(TaskManager taskManager) {
        boolean isNewStatus = true;
        boolean isDoneStatus = true;
        List<Subtask> subtasks = new ArrayList<>(Collections.emptyList());
        if (subTasksIds.isEmpty()) {
            setStatus(StatusOfTask.NEW);
            return;
        }

        for (Integer id : getSubTasksIds()) {
            Subtask subtask = taskManager.getSubTaskById(id);
            subtasks.add(subtask);

        }
        for (Task subTask : subtasks) {
            if (subTask.getStatus() != StatusOfTask.NEW) {
                isNewStatus = false;
            }
            if (subTask.getStatus() != StatusOfTask.DONE) {
                isDoneStatus = false;
            }
        }
        if (isNewStatus && isDoneStatus) {
            setStatus(StatusOfTask.IN_PROGRESS);
            return;
        }
        if (isNewStatus) {
            setStatus(StatusOfTask.NEW);
            return;
        }
        if (isDoneStatus) {
            setStatus(StatusOfTask.DONE);
            return;
        }
        setStatus(StatusOfTask.IN_PROGRESS);
    }


    @Override
    public String toString() {
        return "Эпик{" +
                "id='" + getId() + '\'' +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subTasks=" + getSubTasksIds() +
                '}';
    }

    @Override
    public String toStringInFile() {
        String format = "%s,%s,%s,%s,%s,%s";
        return String.format(format, getId(), getType(), getName(), getDescription(), getStatus(), " ");

    }

    public LocalDateTime getEndTime(TaskManager taskManager) {
        if (subTasksIds.size() == 0) {
            return null;
        }
        LocalDateTime endTime = taskManager.getSubtasksOfEpic(getId()).get(0).getEndTime();
        for (Subtask subTask : taskManager.getSubtasksOfEpic(getId())) {
            if (subTask.getEndTime().isAfter(endTime))
                endTime = subTask.getEndTime();
        }
        return endTime;
    }


    public LocalDateTime getStartTime(TaskManager taskManager) {
        if (subTasksIds.size() == 0) {
            return null;
        }
        LocalDateTime startTime = taskManager.getSubtasksOfEpic(getId()).get(0).getStartTime();
        for (Subtask subTask : taskManager.getSubtasksOfEpic(getId())) {
            if (subTask.getStartTime().isBefore(startTime)) {
                startTime = subTask.getStartTime();
            }
        }
        return startTime;
    }


    public long getDuration(TaskManager taskManager) {
        if (subTasksIds.size() == 0) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(getStartTime(taskManager), getEndTime(taskManager));
    }

}

