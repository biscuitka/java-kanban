import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        /**
         * тестирование
         */
        TaskManager manager = Managers.getDefault();

        /**
         * создание 2х простых задач
         */
        Task taskOne = new Task();
        taskOne.setName("простая задача1");
        taskOne.setDescription("описание задачи1");
        manager.createTask(taskOne);

        Task taskTwo = new Task();
        taskTwo.setName("простая задача2");
        taskTwo.setDescription("описание задачи2");
        manager.createTask(taskTwo);

        System.out.println("тест создания tasks- " + "\n" + taskOne + "\n" + taskTwo);
        System.out.println();

        /**
         * создание 1го эпика с 2мя подзадачами
         */
        Epic epicTaskOne = new Epic();
        epicTaskOne.setName("Эпик1");
        epicTaskOne.setDescription("Описание эпика1");
        manager.createEpic(epicTaskOne);

        SubTask subtaskOne = new SubTask();
        subtaskOne.setName("Подзадача1 эпика1");
        subtaskOne.setDescription("описание подзадачи1");
        manager.createSubtask(subtaskOne);
        subtaskOne.setEpicTask(epicTaskOne);
        epicTaskOne.getSubTasks().add(subtaskOne);

        SubTask subtaskTwo = new SubTask();
        subtaskTwo.setName("подзадача2 эпика1");
        subtaskTwo.setDescription("описание подзадачи2");
        manager.createSubtask(subtaskTwo);
        subtaskTwo.setEpicTask(epicTaskOne);
        epicTaskOne.getSubTasks().add(subtaskTwo);

        /**
         * создание 2го эпика с 1й подзадачей
         */
        Epic epicTaskTwo = new Epic();
        epicTaskTwo.setName("Эпик2");
        epicTaskTwo.setDescription("описание эпика2");
        manager.createEpic(epicTaskTwo);

        SubTask subTaskThree = new SubTask();
        subTaskThree.setName("подзадача3 эпика2");
        subTaskThree.setDescription("описание подзадачи3");
        manager.createSubtask(subTaskThree);

        subTaskThree.setEpicTask(epicTaskTwo);
        epicTaskTwo.getSubTasks().add(subTaskThree);


        System.out.println("тест создания epicTask - " + "\n" + epicTaskOne + "\n" + epicTaskTwo);
        System.out.println();

        System.out.println("тест создания subTask - " + "\n" + subtaskOne + "\n" + subtaskTwo);
        System.out.println();


        /**
         * тестирование сохранения, получения списков, обновления и удаления задач.
         */
        System.out.println("тест сохранения задач " + "\n" + manager.getTaskStorage() +
                "\n" + manager.getEpicTaskStorage() + "\n" + manager.getSubTasksStorage());
        System.out.println();

        System.out.println("обновляем задачу");
        manager.updateTask(taskOne);
        manager.updateEpicTask(epicTaskOne);
        manager.updateSubTask(subtaskOne);

        System.out.println("хранилище после обновления - " + manager.getTaskStorage());

        ArrayList<Task> test = manager.getListOfTasks();
        ArrayList<Task> test2 = manager.getListOfEpicTasks();
        ArrayList<Task> test3 = manager.getListOfSubTasks();
        System.out.println("тест по получению списка- " + "\n" + test + "\n" + test2 + "\n" + test3);
        System.out.println();

        System.out.println("подзадачи определенного эпика - " + manager.getSubtasksOfEpic(3));
        System.out.println("История просмотров задач:\n" + TaskManager.historyManager.getHistory());
        System.out.println();

        System.out.println("тестируем получение по id 2 " + manager.getTaskById(2));
        System.out.println("История просмотров задач:\n" + TaskManager.historyManager.getHistory());
        System.out.println("тестируем получение по id 4 " + manager.getEpicTaskById(3));
        System.out.println("История просмотров задач:\n" + TaskManager.historyManager.getHistory());
        System.out.println("тестируем получение по id 1 " + manager.getSubTaskById(4));
        System.out.println("История просмотров задач:\n" + TaskManager.historyManager.getHistory());
        System.out.println();

        System.out.println("дальше удаляем по номеру");
        manager.deleteTaskById(2);
        manager.deleteEpicTaskById(3);
        manager.deleteSubTaskById(4);

        System.out.println("тестируем получение по id 2 после удаления по номеру " + manager.getTaskById(2));
        System.out.println("тестируем получение по id 3 после удаления по номеру " + manager.getEpicTaskById(3));
        System.out.println("тестируем получение по id 4 после удаления по номеру " + manager.getSubTaskById(4));
        ArrayList<Task> test4 = manager.getListOfTasks();
        ArrayList<Task> test5 = manager.getListOfEpicTasks();
        ArrayList<Task> test6 = manager.getListOfSubTasks();
        System.out.println("список после удаления по номеру - " + "\n" + test4 + "\n" + test5 + "\n" + test6);
        System.out.println("История просмотров задач:\n" + TaskManager.historyManager.getHistory());
        System.out.println();

        System.out.println("тестируем полное удаление");
        manager.deleteAllTasks();
        manager.deleteAllEpicTasks();
        manager.deleteAllSubTasks();


        ArrayList<Task> test7 = manager.getListOfTasks();
        ArrayList<Task> test8 = manager.getListOfEpicTasks();
        ArrayList<Task> test9 = manager.getListOfSubTasks();
        System.out.println("получение списка после удаления - " + "\n" + test7 + "\n" + test8 + "\n" + test9);

        System.out.print("если удалить несуществующую задачу - ");
        manager.deleteTaskById(6);


    }
}
