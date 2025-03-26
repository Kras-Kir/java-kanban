import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import status.Status;
import util.Managers;

public class Main {
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtask(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    public static void main(String[] args) {

        System.out.println("Поехали!");


        TaskManager manager = Managers.getDefault();


        Task task1 = new Task("Начать обучение на Яндекс Практикум", "", Status.NEW);
        Task task2 = new Task("Выбрать курс", "", Status.NEW);

        manager.addTask(task1);
        manager.addTask(task2);


        Epic epic1 = new Epic("Покупка квартиры", "");
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Выбор района кв", "", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Выбор этажа", "", Status.NEW, epic1.getId());


        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic epic2 = new Epic("Покупка машины", "");
        manager.addEpic(epic2);
        Subtask subtask1_2 = new Subtask("Выбор марки", "", Status.NEW, epic2.getId());

        manager.taskById(1);
        manager.epicById(3);
        manager.addSubtask(subtask1_2);
        System.out.println("Список задач");
        System.out.println(manager.getTasks());
        System.out.println("Список эпиков");
        System.out.println(manager.getEpics());
        System.out.println("Список подзадач");
        System.out.println(manager.getSubtasks());
        System.out.println("История просмотров");

        printAllTasks(manager);


        task1.setStatus(Status.DONE);
        manager.updateTask(task1);
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
