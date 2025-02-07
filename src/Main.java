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



        Task task1update = new Task("Начать обучение на Яндекс Практикум", "",Status.DONE);
        manager.updateTask(1,task1update);
        Task task2update = new Task("Выбрать курс", "", Status.DONE);
        manager.updateTask(2,task2update);
        Subtask epic1subtask1 = new Subtask("Выбор района кв", "", Status.NEW, epic1.getId());
        Subtask epic1subtask2 = new Subtask("Выбор этажа", "",Status.DONE, epic1.getId());
        Subtask epic2subtask1 = new Subtask("Выбор марки","",  Status.DONE, epic2.getId());


        manager.updateSubtask(4,epic1subtask1);
        manager.updateSubtask(5,epic1subtask2);
        manager.updateSubtask(7,epic2subtask1);

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
