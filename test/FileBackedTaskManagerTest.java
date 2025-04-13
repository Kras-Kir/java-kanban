package test;

import manager.FileBackedTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import status.Status;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void shouldHandleEmptyFile() throws IOException {
        File tempFile = File.createTempFile("empty", ".csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            assertNull(reader.readLine());

            FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(tempFile);
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.ofMinutes(30);
            manager.addTask(new Task("Test", "", Status.NEW, duration, now));

            try (BufferedReader newReader = new BufferedReader(new FileReader(tempFile))) {
                assertEquals("id,type,name,status,description,epic,duration,startTime", newReader.readLine());
                String line = newReader.readLine();
                assertTrue(line.contains("Test"));
                assertTrue(line.contains("30"));
                assertTrue(line.contains(now.toString()));
                assertNull(newReader.readLine());
            }
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void shouldSaveMultipleTasks() throws IOException {
        File tempFile = File.createTempFile("tasks", ".csv");

        try {
            FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.ofHours(1);

            Epic epic = new Epic("Эпик", "");
            manager.addEpic(epic);

            Subtask subtask = new Subtask("Подзадача", "", Status.NEW, epic.getId(), duration, now);
            manager.addSubtask(subtask);

            Task task = new Task("Задача", "", Status.DONE, duration, now.plusHours(1));
            manager.addTask(task);

            assertTrue(tempFile.exists());
            assertTrue(tempFile.length() > 0);

            // Проверяем содержимое файла
            try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
                // Пропускаем заголовок
                reader.readLine();

                // Проверяем эпик
                String epicLine = reader.readLine();
                assertTrue(epicLine.contains("Эпик"));
                assertTrue(epicLine.contains("EPIC"));

                // Проверяем подзадачу
                String subtaskLine = reader.readLine();
                assertTrue(subtaskLine.contains("Подзадача"));
                assertTrue(subtaskLine.contains("SUBTASK"));
                assertTrue(subtaskLine.contains("60")); // duration в минутах
                assertTrue(subtaskLine.contains(now.toString()));
                assertTrue(subtaskLine.contains(String.valueOf(epic.getId())));

                // Проверяем задачу
                String taskLine = reader.readLine();
                assertTrue(taskLine.contains("Задача"));
                assertTrue(taskLine.contains("TASK"));
                assertTrue(taskLine.contains("60"));
                assertTrue(taskLine.contains(now.plusHours(1).toString()));
            }

        } finally {
            tempFile.delete();
        }
    }

}
