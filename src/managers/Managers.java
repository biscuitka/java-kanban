package managers;

/**
 * утилитарный класс, отвечает за создание менеджера задач
 */
public class Managers {

    /**
     * подбирает нужную реализацию и возвращает объект правильного типа
     * @return возвращает объект-менеджер
     */
    public static managers.InMemoryTaskManager getDefault(){
        return new managers.InMemoryTaskManager();
    }

    /**
     *
     * @return возвращает объект - историю просмотров
     */
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}


