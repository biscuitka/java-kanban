package tasks;

/**
 * класс наследует у tasks.Task и представляет подзадачи Эпика,
 * содержит новое поле ссылку на эпик
 */
public class SubTask extends Task {
    private Epic epicTask;

    public Epic getEpicTask() {
        return epicTask;
    }

    public void setEpicTask(Epic epicTask) {
        this.epicTask = epicTask;
    }


}
