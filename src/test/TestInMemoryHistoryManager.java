package test;
import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.*;
import model.Task;
import org.junit.jupiter.api.Test;
import status.Status;
import util.Managers;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

 class TestInMemoryHistoryManager {
     HistoryManager historyManager = Managers.getDefaultHistory();
     TaskManager manager = new InMemoryTaskManager();

     @BeforeEach
     void setUp() {
         HistoryManager historyManager = Managers.getDefaultHistory();
         TaskManager manager = new InMemoryTaskManager();
     }

     @Test
     void historyManagerShouldPreserveTaskState() {
         Task task = new Task("Task", "Description", Status.NEW);
         historyManager.add(task);
         task.setName("TaskUpdate");
         manager.updateTask(task);

         Task historyTask = historyManager.getHistory().get(0);
         assertEquals(task, historyTask, "HistoryManager сохранил предыдущие состояние задачи");
     }

     @Test
     void testAddTasksAndCheckOrder() {
         Task task1 = new Task("1","",Status.NEW);
         Task task2 = new Task("2","",Status.NEW);
         Task task3 = new Task("3","",Status.NEW);

         manager.addTask(task1);
         manager.addTask(task2);
         manager.addTask(task3);

         historyManager.add(task1);
         historyManager.add(task2);
         historyManager.add(task3);

         List<Task> tasks = historyManager.getHistory();
         assertEquals(3, tasks.size());
         assertEquals(task1, tasks.get(0));
         assertEquals(task2, tasks.get(1));
         assertEquals(task3, tasks.get(2));
     }

     @Test
     void testRemoveTaskFromHistory() {
         Task task1 = new Task("1","",Status.NEW);
         Task task2 = new Task("2","",Status.NEW);
         Task task3 = new Task("3","",Status.NEW);

         manager.addTask(task1);
         manager.addTask(task2);
         manager.addTask(task3);

         historyManager.add(task1);
         historyManager.add(task2);
         historyManager.add(task3);
         //Добавляем task2 тем самым удаляем его предыдущее вхождение в список
         historyManager.add(task2);


         List<Task> tasks = historyManager.getHistory();
         assertEquals(3, tasks.size());
         assertEquals(task1, tasks.get(0));
         assertEquals(task2, tasks.get(2));
         assertEquals(task3, tasks.get(1));
     }
}
