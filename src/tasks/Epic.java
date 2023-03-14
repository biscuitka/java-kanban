package tasks;

import managers.TypeOfTask;

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
            if (subTask.getStatus() != StatusOfTask.NEW){
                isNewStatus = false;
            }
            if (subTask.getStatus() != StatusOfTask.DONE){
                isDoneStatus = false;
            }
        }
        if (isNewStatus){
            return StatusOfTask.NEW;
        }
        if (isDoneStatus){
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
                ", status=" + getStatus() +
                ", subTasks=" + subTasks +
                '}';
    }
    @Override
    public String toStringInFile() {
        String format = "%s,%s,%s,%s,%s,%s";
        return String.format(format,getId(),getType(),getName(),getDescription(),getStatus(), " ");
    }

}
