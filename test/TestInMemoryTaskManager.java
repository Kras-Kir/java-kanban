package test;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestInMemoryTaskManager {

    TaskManager manager = new InMemoryTaskManager();
    private final LocalDateTime testStartTime = LocalDateTime.of(2023, 1, 1, 10, 0);
    private final Duration testDuration = Duration.ofHours(2);

    @Test
    void taskManagerShouldAddAndFindTasks() {
        Task task = new Task("Task", "", Status.NEW, testDuration, testStartTime);
        task.setId(1);

        Epic epic = new Epic("Epic", "");
        epic.setId(2);

        Subtask subtask = new Subtask("Subtask", "", Status.NEW, epic.getId(), testDuration, testStartTime.plusHours(1));
        subtask.setId(3);

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        assertEquals(task, manager.taskById(task.getId()), "Задача найдена по идентификатору");
        assertEquals(epic, manager.epicById(epic.getId()), "Эпик найден по идентификатору");
        assertEquals(subtask, manager.subtaskById(subtask.getId()), "Подзадача найдена по идентификатору");

        // Проверка временных параметров
        assertEquals(testDuration, manager.taskById(task.getId()).getDuration());
        assertEquals(testStartTime, manager.taskById(task.getId()).getStartTime());
        assertEquals(testDuration, manager.subtaskById(subtask.getId()).getDuration());
        assertEquals(testStartTime.plusHours(1), manager.subtaskById(subtask.getId()).getStartTime());
    }

    @Test
    void tasksWithAssignedAndGeneratedIdsShouldNotConflict() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, testDuration, testStartTime);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, testDuration.plusHours(1), testStartTime.plusHours(2));

        manager.addTask(task1);
        task1.setId(1);
        manager.addTask(task2);

        assertNotEquals(task1.getId(), task2.getId(), "Задачи не конфликтуют");

        // Проверка временных параметров
        assertEquals(testDuration, manager.taskById(task1.getId()).getDuration());
        assertEquals(testStartTime, manager.taskById(task1.getId()).getStartTime());
        assertEquals(testDuration.plusHours(1), manager.taskById(task2.getId()).getDuration());
        assertEquals(testStartTime.plusHours(2), manager.taskById(task2.getId()).getStartTime());
    }

    @Test
    void taskShouldRemainUnchangedWhenAddedToManager() {
        Task task = new Task("Task", "Description", Status.NEW, testDuration, testStartTime);
        task.setId(1);
        manager.addTask(task);

        Task managerTask = manager.taskById(task.getId());
        assertEquals(task.getName(), managerTask.getName(), "Название задачи осталось неизменным");
        assertEquals(task.getDescription(), managerTask.getDescription(), "Описание задачи осталось неизменным");
        assertEquals(task.getId(), managerTask.getId(), "id задачи остался неизменным");
        assertEquals(task.getStatus(), managerTask.getStatus(), "Статус задачи остался неизменным");
        assertEquals(task.getDuration(), managerTask.getDuration(), "Длительность задачи осталась неизменной");
        assertEquals(task.getStartTime(), managerTask.getStartTime(), "Время начала задачи осталось неизменным");
        assertEquals(task.getEndTime(), managerTask.getEndTime(), "Время завершения задачи осталось неизменным");
    }

    @Test
    void epicShouldUpdateTimeWhenSubtasksAdded() {
        Epic epic = new Epic("Epic", "");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "", Status.NEW, epic.getId(),
                Duration.ofHours(1), testStartTime);
        Subtask subtask2 = new Subtask("Subtask 2", "", Status.NEW, epic.getId(),
                Duration.ofHours(2), testStartTime.plusHours(1));

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        // Проверка временных параметров эпика
        assertEquals(Duration.ofHours(3), manager.epicById(epic.getId()).getDuration());
        assertEquals(testStartTime, manager.epicById(epic.getId()).getStartTime());
        assertEquals(testStartTime.plusHours(3), manager.epicById(epic.getId()).getEndTime());
    }
}
