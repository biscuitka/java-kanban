package managers;

import java.io.File;

/**
 * утилитарный класс, отвечает за создание менеджера задач
 */
public class Managers {


    /*
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    */

    public static FileBackedTasksManager getDefault() {
        return FileBackedTasksManager.
                loadFromFile(new File("history.csv"));
    }


    /**
     * @return возвращает объект - историю просмотров
     */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


