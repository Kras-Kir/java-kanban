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
