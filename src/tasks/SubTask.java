package tasks;

import managers.TypeOfTask;

/**
 * класс наследует у tasks.Task и представляет подзадачи Эпика,
 * содержит новое поле ссылку на эпик
 */
public class SubTask extends Task {
    private Epic epicTask;

    public SubTask() {
        setType(TypeOfTask.SUBTASK);
    }

    @Override
    public String toStringInFile() {
        String format = "%s,%s,%s,%s,%s,%s";
        return String.format(format,getId(),getType(),getName(),getDescription(),getStatus(),epicTask.getId());
    }

    public Epic getEpicTask() {
        return epicTask;
    }

    public void setEpicTask(Epic epicTask) {
        this.epicTask = epicTask;
    }


}
