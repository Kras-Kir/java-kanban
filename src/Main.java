import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager manager = new TaskManager();

        Task task1 = new Task("Начать обучение на Яндекс Практикум", "",Status.NEW);
        Task task2 = new Task("Выбрать курс", "",  Status.NEW);

        manager.addTask(task1);
        manager.addTask(task2);


        Epic epic1 = new Epic("Покупка квартиры", "");
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Выбор района кв", "", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Выбор этажа", "", Status.NEW,epic1.getId());



        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic epic2 = new Epic("Покупка машины", "");
        manager.addEpic(epic2);
        Subtask subtask1_2 = new Subtask("Выбор марки", "", Status.NEW,epic2.id);


        manager.addSubtask(subtask1_2);
        System.out.println("Список задач");
        System.out.println(manager.getTasks());
        System.out.println("Список эпиков");
        System.out.println(manager.getEpics());
        System.out.println("Список подзадач");
        System.out.println(manager.getSubtasks());




        task1.setStatus(Status.DONE);
        manager.updateTask(task1);
        System.out.println("Ид таска обнов" + task1.getId());
        task2.setStatus(Status.DONE);
        manager.updateTask(task2);
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        subtask1_2.setStatus(Status.DONE);




        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        manager.updateSubtask(subtask1_2);

        System.out.println("Список задач после обновления");
        System.out.println(manager.getTasks());
        System.out.println("Список эпиков после обновления");
        System.out.println(manager.getEpics());
        System.out.println("Список подзадач после обновления");
        System.out.println(manager.getSubtasks());

        



        manager.deleteByIdTask(1);
        manager.deleteByIdEpic(6);
        manager.deleteByIdSubtask(5);



        System.out.println("Список задач после удаления");
        System.out.println(manager.getTasks());
        System.out.println("Список эпиков после удаления");
        System.out.println(manager.getEpics());
        System.out.println("Список подзадач после удаления");
        System.out.println(manager.getSubtasks());


    }
}
