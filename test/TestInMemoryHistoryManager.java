package test;

import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestInMemoryHistoryManager {
    private HistoryManager historyManager;
    private TaskManager manager;
    private final LocalDateTime testStartTime = LocalDateTime.of(2023, 1, 1, 10, 0);
    private final Duration testDuration = Duration.ofHours(2);

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        manager = new InMemoryTaskManager();
    }

    @Test
    void historyManagerShouldPreserveTaskState() {
        Task task = new Task("Task", "Description", Status.NEW, testDuration, testStartTime);
        historyManager.add(task);

        // Изменяем задачу
        task.setName("TaskUpdate");
        task.setDuration(testDuration.plusHours(1));
        task.setStartTime(testStartTime.plusDays(1));
        manager.updateTask(task);

        Task historyTask = historyManager.getHistory().get(0);
        assertEquals("Task", historyTask.getName(), "HistoryManager сохранил предыдущее название задачи");
        assertEquals(testDuration, historyTask.getDuration(), "HistoryManager сохранил предыдущую длительность");
        assertEquals(testStartTime, historyTask.getStartTime(), "HistoryManager сохранил предыдущее время начала");
    }

    @Test
    void testAddTasksAndCheckOrder() {
        Task task1 = new Task("1", "", Status.NEW, testDuration, testStartTime);
        Task task2 = new Task("2", "", Status.NEW, testDuration.plusHours(1), testStartTime.plusHours(3));
        Task task3 = new Task("3", "", Status.NEW, testDuration.plusHours(2), testStartTime.plusHours(6));

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> tasks = historyManager.getHistory();
        assertEquals(3, tasks.size());

        // Проверяем порядок и сохранение всех полей
        assertEquals(task1, tasks.get(0));
        assertEquals(testDuration, tasks.get(0).getDuration());
        assertEquals(testStartTime, tasks.get(0).getStartTime());

        assertEquals(task2, tasks.get(1));
        assertEquals(testDuration.plusHours(1), tasks.get(1).getDuration());
        assertEquals(testStartTime.plusHours(3), tasks.get(1).getStartTime());

        assertEquals(task3, tasks.get(2));
        assertEquals(testDuration.plusHours(2), tasks.get(2).getDuration());
        assertEquals(testStartTime.plusHours(6), tasks.get(2).getStartTime());
    }

    @Test
    void testRemoveTaskFromHistory() {
        Task task1 = new Task("1", "", Status.NEW, testDuration, testStartTime);
        Task task2 = new Task("2", "", Status.NEW, testDuration, testStartTime.plusHours(2));
        Task task3 = new Task("3", "", Status.NEW, testDuration, testStartTime.plusHours(4));

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        // Добавляем task2 тем самым удаляем его предыдущее вхождение в список
        historyManager.add(task2);

        List<Task> tasks = historyManager.getHistory();
        assertEquals(3, tasks.size());

        // Проверяем порядок и сохранение временных параметров
        assertEquals(task1, tasks.get(0));
        assertEquals(testStartTime, tasks.get(0).getStartTime());

        assertEquals(task3, tasks.get(1));
        assertEquals(testStartTime.plusHours(4), tasks.get(1).getStartTime());

        assertEquals(task2, tasks.get(2));
        assertEquals(testStartTime.plusHours(2), tasks.get(2).getStartTime());
    }

    @Test
    void testHistoryPreservesTimeFieldsAfterUpdate() {
        Task task = new Task("Task", "", Status.NEW, testDuration, testStartTime);
        manager.addTask(task);
        historyManager.add(task);

        // Обновляем задачу
        Task updatedTask = new Task("Updated", "", Status.IN_PROGRES,
                testDuration.plusHours(1), testStartTime.plusDays(1));
        updatedTask.setId(task.getId());
        manager.updateTask(updatedTask);

        // Проверяем, что история сохранила оригинальные значения
        Task historyTask = historyManager.getHistory().get(0);
        assertEquals("Task", historyTask.getName());
        assertEquals(testDuration, historyTask.getDuration());
        assertEquals(testStartTime, historyTask.getStartTime());
        assertEquals(testStartTime.plus(testDuration), historyTask.getEndTime());
    }
}
