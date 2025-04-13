package test;

import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestTask {

    @Test
    void testTaskEqualityById() {
        Task task1 = new Task("Task 1", "", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task 1", "", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2, "Задачи равны");
    }

    @Test
    void testSubtaskAndEpicEqualityById() {
        LocalDateTime now = LocalDateTime.now();
        Subtask subtask1 = new Subtask("Subtask 1", "1", Status.NEW, 1, Duration.ofMinutes(30), now);
        Subtask subtask2 = new Subtask("Subtask 1", "1", Status.NEW, 1, Duration.ofMinutes(30), now);
        subtask1.setId(1);
        subtask2.setId(1);
        assertEquals(subtask1, subtask2, "Подзадачи равны");
    }

    @Test
    void testTaskTimeFields() {
        LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 10, 0);
        Duration duration = Duration.ofHours(2);
        Task task = new Task("Task", "Desc", Status.NEW, duration, startTime);

        assertEquals(duration, task.getDuration());
        assertEquals(startTime, task.getStartTime());
        assertEquals(startTime.plus(duration), task.getEndTime());
    }
}
