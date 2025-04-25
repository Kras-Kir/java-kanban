import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import status.Status;
import util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    private static void printAllTasks(TaskManager manager) {
        System.out.println("\nЗадачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }

        System.out.println("\nЭпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);
            System.out.println("  Длительность: " + epic.getDuration().toMinutes() + " минут");
            System.out.println("  Время начала: " + epic.getStartTime());
            System.out.println("  Время завершения: " + epic.getEndTime());

            for (Task subtask : manager.getEpicSubtask(epic.getId())) {
                System.out.println("--> " + subtask);
            }
        }

        System.out.println("\nПодзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("\nИстория:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager manager = Managers.getDefault();
        LocalDateTime baseTime = LocalDateTime.now();

        // Создаем задачи с временными параметрами
        Task task1 = new Task("Начать обучение на Яндекс Практикум", "", Status.NEW,
                Duration.ofHours(2), baseTime);
        Task task2 = new Task("Выбрать курс", "", Status.NEW,
                Duration.ofHours(1), baseTime.plusHours(3));

        manager.addTask(task1);
        manager.addTask(task2);

        // Создаем эпики и подзадачи
        Epic epic1 = new Epic("Покупка квартиры", "");
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Выбор района кв", "", Status.NEW, epic1.getId(),
                Duration.ofDays(1), baseTime.plusDays(1));
        Subtask subtask2 = new Subtask("Выбор этажа", "", Status.NEW, epic1.getId(),
                Duration.ofHours(4), baseTime.plusDays(2));

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic epic2 = new Epic("Покупка машины", "");
        manager.addEpic(epic2);
        Subtask subtask12 = new Subtask("Выбор марки", "", Status.NEW, epic2.getId(),
                Duration.ofHours(3), baseTime.plusDays(3));

        manager.addSubtask(subtask12);

        // Получаем задачи для создания истории
        manager.taskById(task1.getId());
        manager.epicById(epic1.getId());
        manager.subtaskById(subtask1.getId());

        System.out.println("\nСписок задач:");
        manager.getTasks().forEach(System.out::println);

        System.out.println("\nСписок эпиков:");
        manager.getEpics().forEach(System.out::println);

        System.out.println("\nСписок подзадач:");
        manager.getSubtasks().forEach(System.out::println);

        System.out.println("\nИстория просмотров:");
        manager.getHistory().forEach(System.out::println);

        printAllTasks(manager);

        // Обновляем статусы задач
        task1.setStatus(Status.DONE);
        manager.updateTask(task1);
        task2.setStatus(Status.DONE);
        manager.updateTask(task2);

        subtask1.setStatus(Status.IN_PROGRES);
        subtask2.setStatus(Status.DONE);
        subtask12.setStatus(Status.DONE);

        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        manager.updateSubtask(subtask12);

        System.out.println("\nПосле обновления статусов:");
        printAllTasks(manager);

        // Удаляем некоторые задачи
        manager.deleteByIdTask(task1.getId());
        manager.deleteByIdEpic(epic2.getId());
        manager.deleteByIdSubtask(subtask2.getId());

        System.out.println("\nПосле удаления:");
        printAllTasks(manager);

        // Проверка пересечения по времени
        try {
            Subtask conflictSubtask = new Subtask("Конфликтная задача", "", Status.NEW, epic1.getId(),
                    Duration.ofHours(5), baseTime.plusDays(1).plusHours(2));
            manager.addSubtask(conflictSubtask);
        } catch (IllegalArgumentException e) {
            System.out.println("\nОшибка при добавлении задачи: " + e.getMessage());
        }
    }
}
