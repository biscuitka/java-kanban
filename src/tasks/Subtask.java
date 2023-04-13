package tasks;

import managers.TypeOfTask;

/**
 * класс наследует у tasks.Task и представляет подзадачи Эпика,
 * содержит новое поле ссылку на эпик
 */
public class Subtask extends Task {
    private int epicId;

    public Subtask() {
        setType(TypeOfTask.SUBTASK);
    }

    @Override
    public String toStringInFile() {
        String format = "%s,%s,%s,%s,%s,%s,%s,%s";
        return String.format(format,getId(),getType(),getName(),getDescription(),getStatus(),
                getStartTime().format(formatter), getDuration(),getEpicId());
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
