package test;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import status.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestInMemoryTaskManager {

    TaskManager manager = new InMemoryTaskManager();

    @Test
    void taskManagerShouldAddAndFindTasks() {
        Task task = new Task("Task", "", Status.NEW);
        task.setId(1);
        Epic epic = new Epic("Epic", "");
        epic.setId(2);
        Subtask subtask = new Subtask("Subtask", "",Status.NEW, epic.getId());
        subtask.setId(3);


        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        assertEquals(task, manager.taskById(task.getId()), "Задача найдена по идентификатору");
        assertEquals(epic, manager.epicById(epic.getId()), "Эпик найден по идентификатору");
        assertEquals(subtask, manager.subtaskById(subtask.getId()), "Подзадача найдена по идентификатору");
    }

    @Test
    void tasksWithAssignedAndGeneratedIdsShouldNotConflict() {
        Task task1 = new Task("Task 1", "Description 1",Status.NEW);
        Task task2 = new Task("Task 2", "Description 2",Status.NEW);
        manager.addTask(task1);
        task1.setId(1);
        manager.addTask(task2);

        assertNotEquals(task1.getId(), task2.getId(), "Задачи не конфликтуют");
    }

    @Test
    void taskShouldRemainUnchangedWhenAddedToManager() {
        Task task = new Task("Task", "Description",Status.NEW);
        task.setId(1);
        manager.addTask(task);

        Task managerTask = manager.taskById(task.getId());
        assertEquals(task.getName(), managerTask.getName(), "Название задачи осталось неизменным");
        assertEquals(task.getDescription(), managerTask.getDescription(), "Описание задачи осталось неизменным");
        assertEquals(task.getId(), managerTask.getId(), "id задачи остался неизменным");
        assertEquals(task.getStatus(), managerTask.getStatus(), "Статус задачи остался неизменным");
    }
}
