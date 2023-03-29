
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) {


        // тестирование
        TaskManager manager = Managers.getDefault();

        //создание задач разных типов
        Task taskOne = new Task();
        taskOne.setName("простая задача1");
        taskOne.setDescription("описание задачи1");
        taskOne.setStartTime(LocalDateTime.now());
        taskOne.setDuration(10);
        manager.createTask(taskOne);

        Task taskTwo = new Task();
        taskTwo.setName("простая задача2");
        taskTwo.setDescription("описание задачи2");
        taskTwo.setStartTime(LocalDateTime.now().plusMinutes(15));
        taskTwo.setDuration(20);
        manager.createTask(taskTwo);

        System.out.println("тест создания tasks- " + "\n" + taskOne + "\n" + taskTwo);
        System.out.println();

        Epic epicTaskOne = new Epic();
        epicTaskOne.setName("Эпик1");
        epicTaskOne.setDescription("Описание эпика1");
        manager.createEpic(epicTaskOne);

        SubTask subtaskOne = new SubTask();
        subtaskOne.setName("Подзадача1 эпика1");
        subtaskOne.setDescription("описание подзадачи1");
        subtaskOne.setStartTime(LocalDateTime.now().plusMinutes(10));
        subtaskOne.setDuration(20);
        manager.createSubtask(subtaskOne);
        subtaskOne.setEpicTask(epicTaskOne);
        epicTaskOne.getSubTasks().add(subtaskOne);

        SubTask subtaskTwo = new SubTask();
        subtaskTwo.setName("подзадача2 эпика1");
        subtaskTwo.setDescription("описание подзадачи2");
        manager.createSubtask(subtaskTwo);
        subtaskTwo.setEpicTask(epicTaskOne);
        epicTaskOne.getSubTasks().add(subtaskTwo);

        SubTask subTaskThree = new SubTask();
        subTaskThree.setName("подзадача3 эпика2");
        subTaskThree.setDescription("описание подзадачи3");
        manager.createSubtask(subTaskThree);
        subTaskThree.setEpicTask(epicTaskOne);
        epicTaskOne.getSubTasks().add(subTaskThree);

        Epic epicTaskTwo = new Epic();
        epicTaskTwo.setName("Эпик2");
        epicTaskTwo.setDescription("описание эпика2");
        manager.createEpic(epicTaskTwo);

        System.out.println("тест создания epicTask - " + "\n" + epicTaskOne + "\n" + epicTaskTwo);
        System.out.println();

        System.out.println("тест создания subTask - " + "\n" + subtaskOne + "\n" + subtaskTwo + "\n" + subTaskThree);
        System.out.println();



        System.out.println("тестирование InMemoryTaskManager");
        // тестирование получения и истории
        System.out.println("список простых задач- " + "\n" + manager.getListOfTasks());
        System.out.println("список эпиков- " + "\n" + manager.getListOfEpicTasks());
        System.out.println("список сабтасок- " + "\n" + manager.getListOfSubTasks());
        System.out.println();

        System.out.println("подзадачи определенного эпика - " + manager.getSubtasksOfEpic(3));
        System.out.println("История просмотров задач:\n" + manager.getHistory());
        System.out.println();

        System.out.println("тестируем получение по id 2 " + manager.getTaskById(2));
        System.out.println();

        System.out.println("тестируем получение по id 3 " + manager.getEpicTaskById(3));
        System.out.println("История просмотров задач:\n" + manager.getHistory());
        System.out.println();


        /*
        // для тестирования удаления
        System.out.println("дальше удаляем по номеру задачу(2)");
        manager.deleteTaskById(2);
        System.out.println("История просмотров задач после удаления задачи(2):\n" + manager.getHistory());
        System.out.println();

        System.out.println("дальше снова удаляем по номеру эпик(1, id3)");
        manager.deleteEpicTaskById(3);
        System.out.println("История просмотров задач после удаления эпика(1, id3), с ней должна удалиться ее подзадача1:\n" + manager.getHistory());

        System.out.println("список простых задач после удаления по номеру- " + "\n" + manager.getListOfTasks());
        System.out.println("список эпиков после удаления по номеру- " + "\n" + manager.getListOfEpicTasks());
        System.out.println("список сабтасок после удаления по номеру- " + "\n" + manager.getListOfSubTasks());
        System.out.println("История просмотров задач:\n" + manager.getHistory());
        System.out.println();

        System.out.println("тестируем полное удаление");
        manager.deleteAllTasks();
        manager.deleteAllEpicTasks();
        manager.deleteAllSubTasks();

        System.out.println("список простых задач после удаления- " + "\n" + manager.getListOfTasks());
        System.out.println("список эпиков после удаления- " + "\n" + manager.getListOfEpicTasks());
        System.out.println("список сабтасок после удаления- " + "\n" + manager.getListOfSubTasks());

         */
    }
}
