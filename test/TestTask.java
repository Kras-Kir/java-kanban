package test;

import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import status.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestTask {
    @Test
    void testTaskEqualityById() {
        Task task1 = new Task("Task 1", "", Status.NEW);
        Task task2 = new Task("Task 1", "", Status.NEW);
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2, "Задачи равны");
    }

    @Test
    void testSubtaskAndEpicEqualityById() {
        Subtask subtask1 = new Subtask("Subtask 1", "1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Subtask 1", "1", Status.NEW, 1);
        subtask1.setId(1);
        subtask2.setId(1);
        assertEquals(subtask1, subtask2, "Подзадачи равны");
    }
}
