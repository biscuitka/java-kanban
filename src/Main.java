import tasks.Epic;
import tasks.StatusOfTask;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        /**
         * тестирование
         */
        Manager manager = new Manager();

        /**
         * создание 2х простых задач
         */
        Task taskOne = new Task(); // создание задачи
        taskOne.setName("простая задача1");
        taskOne.setTaskDescription("описание задачи1");
        taskOne.setStatusOfTask(StatusOfTask.NEW);
        manager.addTaskToStorage(taskOne); // // добавили в хранилище

        Task taskTwo = new Task(); // создание задачи
        taskTwo.setName("простая задача2");
        taskTwo.setTaskDescription("описание задачи2");
        taskTwo.setStatusOfTask(StatusOfTask.NEW);
        manager.addTaskToStorage(taskTwo); // // добавили в хранилище

        System.out.println("тест создания tasks- " + "\n" + taskOne + "\n" + taskTwo);
        System.out.println();

        /**
         * создание 1го эпика с 2мя подзадачами
         */
        Epic epicTaskOne = new Epic(); // создание эпика
        epicTaskOne.setName("Эпик1");
        epicTaskOne.setTaskDescription("Описание эпика1");
        epicTaskOne.setStatusOfTask(StatusOfTask.NEW);
        manager.addEpicToStorage(epicTaskOne); // добавили в хранилище

        SubTask subtaskOne = new SubTask(); // создание подзадач
        subtaskOne.setName("Подзадача1 эпика1");
        subtaskOne.setTaskDescription("описание подзадачи1");
        subtaskOne.setStatusOfTask(StatusOfTask.NEW);
        manager.addSubtaskToStorage(subtaskOne); // добавили в хранилище
        subtaskOne.setEpicTask(epicTaskOne); // сообщили подзадаче о ее эпике
        epicTaskOne.getSubTasks().add(subtaskOne); // сообщили эпику о его подзадаче

        SubTask subtaskTwo = new SubTask();
        subtaskTwo.setName("подзадача2 эпика1");
        subtaskTwo.setTaskDescription("описание подзадачи2");
        subtaskTwo.setStatusOfTask(StatusOfTask.NEW);
        manager.addSubtaskToStorage(subtaskTwo); // добавили в хранилище
        subtaskTwo.setEpicTask(epicTaskOne); // сообщили подзадаче о ее эпике
        epicTaskOne.getSubTasks().add(subtaskTwo); // сообщили эпику о его подзадаче

        /**
         * создание 2го эпика с 1й подзадачей
         */
        Epic epicTaskTwo = new Epic(); // создание эпика
        epicTaskTwo.setName("Эпик2");
        epicTaskTwo.setTaskDescription("описание эпика2");
        epicTaskTwo.setStatusOfTask(StatusOfTask.NEW);
        manager.addEpicToStorage(epicTaskTwo); // добавили в хранилище

        SubTask subTaskThree = new SubTask();
        subTaskThree.setName("подзадача3 эпика2");
        subTaskThree.setTaskDescription("описание подзадачи3");
        subTaskThree.setStatusOfTask(StatusOfTask.NEW);
        manager.addSubtaskToStorage(subTaskThree); // добавили в хранилище

        subTaskThree.setEpicTask(epicTaskTwo); // сообщили подзадаче о ее эпике
        epicTaskTwo.getSubTasks().add(subTaskThree); // сообщили эпику о его подзадаче


        System.out.println("тест создания epicTask - " + "\n" + epicTaskOne + "\n" + epicTaskTwo);
        System.out.println();

        System.out.println("тест создания subTask - " + "\n" + subtaskOne + "\n" + subtaskTwo);
        System.out.println();


        /**
         * тестирование сохранения, получения списков, обновления и удаления задач.
         */
        System.out.println("тест сохранения задач " + "\n" + manager.taskStorage +
                "\n" + manager.epicTaskStorage + "\n" + manager.subTasksStorage);
        System.out.println();

        System.out.println("обновляем задачу");
        manager.updateTask(taskOne);

        System.out.println("хранилище после обновления - " + manager.taskStorage);

        ArrayList<Task> test = manager.getListOfTasks();
        System.out.println("тест по получению списка- " + "\n" + test);
        System.out.println();

        System.out.println("подзадачи определенного эпика - " + manager.getSubtasksOfEpic(epicTaskOne));
        System.out.println();

        System.out.println("тестируем получение по id 2 " + manager.getTaskById(2));
        System.out.println("тестируем получение по id 4 " + manager.getTaskById(4));
        System.out.println("тестируем получение по id 1 " + manager.getTaskById(1));
        System.out.println();

        System.out.println("дальше удаляем по номеру");
        manager.removeTaskById(2);
        manager.removeTaskById(6);
        manager.removeTaskById(1);

        System.out.println("тестируем получение по id 2 после удаления по номеру" + manager.getTaskById(2));
        System.out.println("тестируем получение по id 4 после удаления по номеру" + manager.getTaskById(4));
        System.out.println("тестируем получение по id 1 после удаления по номеру" + manager.getTaskById(1));
        ArrayList<Task> test2 = manager.getListOfTasks();
        System.out.println("список после удаления по номеру - " + test2);
        System.out.println();

        System.out.println("тестируем полное удаление");
        manager.clearAllTasks();

        ArrayList<Task> test3 = manager.getListOfTasks();
        System.out.println("получение списка после удаления - " + test3);

        System.out.print("если удалить несуществующую задачу - ");
        manager.removeTaskById(6);


    }
}
