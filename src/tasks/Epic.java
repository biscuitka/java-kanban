package tasks;

import java.util.ArrayList;

/**
 * класс наследует у tasks.Task и представляет большие задачи
 */
public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>();

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
    public StatusOfTask getStatusOfTask() {
        boolean isNewStatus = true;
        boolean isDoneStatus = true;

        if (subTasks.isEmpty()) {
            return StatusOfTask.NEW;
        }

        for (Task subTask : subTasks) {
            if (subTask.getStatusOfTask() != StatusOfTask.NEW){
                isNewStatus = false;
            }
            if (subTask.getStatusOfTask() != StatusOfTask.DONE){
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
}
