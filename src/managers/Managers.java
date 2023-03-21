package managers;

import java.io.File;

/**
 * утилитарный класс, отвечает за создание менеджера задач
 */
public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    /**
     * @return возвращает объект - историю просмотров
     */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


