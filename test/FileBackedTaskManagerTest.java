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


}
