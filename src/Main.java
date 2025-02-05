import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager manager = new TaskManager();

        Task task1 = new Task("Начать обучение на Яндекс Практикум", "",manager.counterId(),Status.NEW);
        Task task2 = new Task("Выбрать курс", "", manager.counterId(), Status.NEW);

        manager.addTask(task1);
        manager.addTask(task2);


        Epic epic1 = new Epic("Покупка квартиры", "", manager.counterId(),Status.NEW,
                new ArrayList<Integer>());
        Subtask subtask1 = new Subtask("Выбор района кв", "", manager.counterId(), Status.NEW, epic1.id);
        Subtask subtask2 = new Subtask("Выбор этажа", "", manager.counterId(), Status.NEW,epic1.id);

        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic epic2 = new Epic("Покупка машины", "", manager.counterId(), Status.NEW, new ArrayList<>());
        Subtask subtask1_2 = new Subtask("Выбор марки", "", manager.counterId(), Status.NEW,epic2.id);

        manager.addEpic(epic2);
        manager.addSubtask(subtask1_2);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());



        Task task1update = new Task("Начать обучение на Яндекс Практикум", "", task1.id,Status.DONE);
        Task task2update = new Task("Выбрать курс", "", task2.id, Status.DONE);
        Subtask epic1subtask1 = new Subtask("Выбор района кв", "", subtask1.id, Status.DONE, epic1.id);
        Subtask epic1subtask2 = new Subtask("Выбор этажа", "", subtask2.id, Status.NEW, epic1.id);
        Subtask epic2subtask1 = new Subtask("Выбор марки","", subtask1_2.id, Status.DONE, epic2.id);

        manager.updateTask(task1update);
        manager.updateTask(task2update);
        manager.updateSubtask(epic1subtask1);
        manager.updateSubtask(epic1subtask2);
        manager.updateSubtask(epic2subtask1);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        



        manager.deleteByIdTask(1);
        manager.deleteByIdEpic(6);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }
}
