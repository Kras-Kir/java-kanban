package test;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Test;
import status.Status;

import static org.junit.jupiter.api.Assertions.*;

 class TestSubtask {

     TaskManager manager = new InMemoryTaskManager();
  @Test
  void subtaskEpicSubtask() {
   Subtask subtask = new Subtask("subtask","", Status.NEW, 1);
   manager.addSubtask(subtask);
   Epic epic = new Epic("epic","");
   epic.setId(subtask.getId());
   manager.addEpic(epic);
   assertNotEquals(epic.getId(), subtask.getId(), "");

  }

  // Проверяем, что подзадача не хранит старый id
  @Test
  void testRemovedSubTaskDoesNotKeepOldId() {
   // Создаём подзадачу
   Subtask subtask = new Subtask("1", "", Status.NEW, 10);

   // Добавляем подзадачу в менеджер
   manager.addSubtask(subtask);

   // Удаляем подзадачу
   manager.deleteByIdSubtask(subtask.getId());

   // Проверяем, что подзадача не хранит старый id
   assertNull(subtask.getId());
  }
}
