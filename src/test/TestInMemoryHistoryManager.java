package test;
import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.Test;
import status.Status;
import util.Managers;

import static org.junit.jupiter.api.Assertions.*;

 class TestInMemoryHistoryManager {
     HistoryManager historyManager = Managers.getDefaultHistory();
     TaskManager manager = new InMemoryTaskManager();

     @Test
     void historyManagerShouldPreserveTaskState() {
         Task task = new Task("Task", "Description", Status.NEW);
         historyManager.add(task);
         task.setName("TaskUpdate");
         manager.updateTask(task);

         Task historyTask = historyManager.getHistory().get(0);
         assertEquals(task, historyTask, "HistoryManager сохранил предыдущие состояние задачи");
     }
}
