package test;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestInMemoryTaskManager {

    TaskManager manager = new InMemoryTaskManager();
    private final LocalDateTime testStartTime = LocalDateTime.of(2023, 1, 1, 10, 0);
    private final Duration testDuration = Duration.ofHours(2);

    @Test
    void taskShouldRemainUnchangedWhenAddedToManager() {
        Task task = new Task("Task", "Description", Status.NEW, testDuration, testStartTime);
        task.setId(1);
        manager.addTask(task);

        Task managerTask = manager.taskById(task.getId());
        assertEquals(task.getName(), managerTask.getName(), "Название задачи осталось неизменным");
        assertEquals(task.getDescription(), managerTask.getDescription(), "Описание задачи осталось неизменным");
        assertEquals(task.getId(), managerTask.getId(), "id задачи остался неизменным");
        assertEquals(task.getStatus(), managerTask.getStatus(), "Статус задачи остался неизменным");
        assertEquals(task.getDuration(), managerTask.getDuration(), "Длительность задачи осталась неизменной");
        assertEquals(task.getStartTime(), managerTask.getStartTime(), "Время начала задачи осталось неизменным");
        assertEquals(task.getEndTime(), managerTask.getEndTime(), "Время завершения задачи осталось неизменным");
    }

}
