package test;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestEpic {
    TaskManager manager = new InMemoryTaskManager();

    @Test
    void testEpicAddEpicSubtask() {
        Epic epic = new Epic("epic", "");
        manager.addEpic(epic);
        LocalDateTime now = LocalDateTime.now();
        Subtask subtask = new Subtask("subtask", "", Status.NEW, epic.getId(), Duration.ofMinutes(30), now);
        subtask.setId(epic.getId());
        manager.addSubtask(subtask);
        assertNotEquals(epic.getId(), subtask.getId(), "");
    }

    @Test
    void testEpicDoesNotKeepOutdatedSubTaskIds() {
        Epic epic = new Epic("1", "");
        manager.addEpic(epic);
        LocalDateTime now = LocalDateTime.now();

        Subtask subtask1 = new Subtask("1", "", Status.NEW, epic.getId(), Duration.ofMinutes(30), now);
        Subtask subtask2 = new Subtask("2", "", Status.NEW, epic.getId(), Duration.ofMinutes(45), now.plusHours(1));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        manager.deleteByIdSubtask(subtask1.getId());

        ArrayList<Integer> subtaskId = epic.getSubtaskId();
        assertFalse(subtaskId.contains(subtask1.getId()));
    }
    
}