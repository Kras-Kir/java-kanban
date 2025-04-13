package test;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TestSubtask {

    TaskManager manager = new InMemoryTaskManager();

    @Test
    void subtaskEpicSubtask() {
        LocalDateTime now = LocalDateTime.now();
        Subtask subtask = new Subtask("subtask", "", Status.NEW, 1, Duration.ofMinutes(30), now);
        manager.addSubtask(subtask);
        Epic epic = new Epic("epic", "");
        epic.setId(subtask.getId());
        manager.addEpic(epic);
        assertNotEquals(epic.getId(), subtask.getId(), "");
    }

    @Test
    void testRemovedSubTaskDoesNotKeepOldId() {
        LocalDateTime now = LocalDateTime.now();
        Subtask subtask = new Subtask("1", "", Status.NEW, 10, Duration.ofMinutes(30), now);
        manager.addSubtask(subtask);
        manager.deleteByIdSubtask(subtask.getId());
        assertNull(subtask.getId());
    }

    @Test
    void testSubtaskTimeFields() {
        LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 10, 0);
        Duration duration = Duration.ofHours(2);
        Subtask subtask = new Subtask("Subtask", "Desc", Status.NEW, 1, duration, startTime);

        assertEquals(duration, subtask.getDuration());
        assertEquals(startTime, subtask.getStartTime());
        assertEquals(startTime.plus(duration), subtask.getEndTime());
        assertEquals(1, subtask.getEpicId());
    }
}
