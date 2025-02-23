package test;
import model.Subtask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import manager.*;
import model.Epic;
import status.Status;

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


    //не понимаю как организовать проверку, что объект Epic нельзя добавить в самого себя в виде подзадачи;


}