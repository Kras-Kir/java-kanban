package manager;

import java.io.*;
import java.nio.charset.StandardCharsets;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;

import status.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.load();
        return manager;
    }

    private void load() {
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(file, StandardCharsets.UTF_8))) {
            //String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);

            //String[] lines = content.split("\r?\n|\r");
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Task task = fromString(line);
                    if (task != null) {
                        if (task instanceof Epic) {
                            directEpicPut((Epic) task);
                        } else if (task instanceof Subtask) {
                            directSubtaskPut((Subtask) task);
                        } else {
                            directTaskPut(task);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки из файла", e);
        }
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл", e);
        }
    }

    public String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");

        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            sb.append(TaskType.SUBTASK).append(",")
                    .append(subtask.getName()).append(",")
                    .append(subtask.getStatus()).append(",")
                    .append(subtask.getDescription()).append(",")
                    .append(subtask.getEpicId());
        } else if (task instanceof Epic) {
            sb.append(TaskType.EPIC).append(",")
                    .append(task.getName()).append(",")
                    .append(task.getStatus()).append(",")
                    .append(task.getDescription()).append(",");
        } else {
            sb.append(TaskType.TASK).append(",")
                    .append(task.getName()).append(",")
                    .append(task.getStatus()).append(",")
                    .append(task.getDescription()).append(",");
        }

        return sb.toString();
    }

    public Task fromString(String value) {
        String[] parts = value.split(",");
        if (parts.length < 5) return null;

        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];

        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            case SUBTASK:
                if (parts.length < 6) return null;
                int epicId = Integer.parseInt(parts[5]);
                Subtask subtask = new Subtask(name, description, status, epicId);
                subtask.setId(id);
                return subtask;
            default:
                return null;
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void deleteByIdTask(Integer id) {
        super.deleteByIdTask(id);
        save();
    }

    @Override
    public void deleteByIdEpic(Integer id) {
        super.deleteByIdEpic(id);
        save();
    }

    @Override
    public void deleteByIdSubtask(Integer id) {
        super.deleteByIdSubtask(id);
        save();
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
        save();
    }

    @Override
    public void deleteEpic() {
        super.deleteEpic();
        save();
    }

    @Override
    public void deleteSubtask() {
        super.deleteSubtask();
        save();
    }
}
