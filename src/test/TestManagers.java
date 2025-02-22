package test;
import manager.HistoryManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.Managers;

class TestManagers {
    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefault();

     @Test
     void managersShouldBeInitialized() {
         assertNotNull(taskManager, "taskManager проинициализирован");
         assertNotNull(historyManager, "historyManager проинициализирован");
     }
}
