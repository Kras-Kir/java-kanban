package test;
import model.Subtask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import manager.*;
import model.Epic;
import status.Status;

import java.util.ArrayList;

class TestEpic {
    TaskManager manager = new InMemoryTaskManager();
    @Test
    void testEpicAddEpicSubtask() {
        Epic epic = new Epic("epic", "");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("subtask","", Status.NEW, epic.getId());
        subtask.setId(epic.getId());
        manager.addSubtask(subtask);
        assertNotEquals(epic.getId(), subtask.getId(), "");

    }

    @Test
    void testEpicDoesNotKeepOutdatedSubTaskIds() {
        // Создаём эпик
        Epic epic = new Epic( "1", "");
        manager.addEpic(epic);

        // Создаём подзадачи и добавляем их в эпик
        Subtask subtask1 = new Subtask( "1", "", Status.NEW,epic.getId());
        Subtask subtask2 = new Subtask( "2", "", Status.NEW,epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        // Удаляем одну из подзадач
        manager.deleteByIdSubtask(subtask1.getId());

        // Проверяем, что эпик не хранит не актуальный id
        ArrayList<Integer> subtaskId = epic.getSubtaskId();
        assertFalse(subtaskId.contains(subtask1.getId()));
    }


}