package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import status.Status;
import util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private Integer id = 0;
    public HistoryManager historyManager = Managers.getDefaultHistory();


    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime)
    );


    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    private Integer counterId() {
        id++;
        return id;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    //Получение списка задач
    @Override
    public List<Task> getTasks() {
        return tasks.values().stream()
                .collect(Collectors.toList());
    }

    //Получение списка эпиков
    @Override
    public List<Epic> getEpics() {
        return epics.values().stream()
                .collect(Collectors.toList());
    }

    //Получение списка подзадач
    @Override
    public List<Subtask> getSubtasks() {
        return subtasks.values().stream()
                .collect(Collectors.toList());
    }

    //Удаление всех задач
    @Override
    public void deleteTask() {
        tasks.keySet().forEach(historyManager::remove);
        tasks.clear();
        prioritizedTasks.removeIf(task -> task instanceof Task && !(task instanceof Subtask));
    }

    //Удаление всех эпиков
    @Override
    public void deleteEpic() {
        subtasks.keySet().forEach(historyManager::remove);
        epics.keySet().forEach(historyManager::remove);
        subtasks.clear();
        epics.clear();
        prioritizedTasks.removeIf(task -> task instanceof Subtask);
    }

    //Удаление всех подзадач
    @Override
    public void deleteSubtask() {
        subtasks.keySet().forEach(historyManager::remove);
        subtasks.clear();

        epics.values().forEach(epic -> {
            updateDurationAndStart(epic);
            epic.deleteSubtaskId();
            updateEpicStatus(epic);
        });
        prioritizedTasks.removeIf(task -> task instanceof Subtask);
    }

    //Получение задач по идентификатору
    @Override
    public Task taskById(Integer id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    //Получение эпиков по идентификатору
    @Override
    public Epic epicById(Integer id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    //Получение подзадач по идентификатору
    @Override
    public Subtask subtaskById(Integer id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    //Создание задач
    @Override
    public void addTask(Task task) {
        if (task == null) {
            throw new TaskValidationException("Нельзя добавить null задачу");
        }
        if (isTaskOverlappingAny(task)) {
            throw new ManagerSaveException("Задача пересекается по времени с существующей");
        }
        task.setId(counterId());
        tasks.put(task.getId(), task);
        addPrioritizedTasks(task);
    }

    //Создание эпиков
    @Override
    public void addEpic(Epic epic) {
        epic.setId(counterId());
        epics.put(epic.getId(), epic);
    }

    //Создание подзадач
    @Override
    public void addSubtask(Subtask subtask) {
        if (isTaskOverlappingAny(subtask)) {
            throw new ManagerSaveException("Подзадача пересекается по времени с существующей");
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtask.setId(counterId());
            epic.addSubtaskId(subtask.getId());
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epic);
            updateDurationAndStart(epic);

        }
        addPrioritizedTasks(subtask);
    }

    //Обновление задач
    @Override
    public void updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getId())) {
            Task oldTask = tasks.get(newTask.getId());
            prioritizedTasks.remove(oldTask);
            if (isTaskOverlappingAny(newTask)) {
                prioritizedTasks.add(oldTask);
                throw new ManagerSaveException("Обновленная задача пересекается по времени с существующей");
            }
            tasks.put(newTask.getId(), newTask);
            addPrioritizedTasks(newTask);

        }
    }

    //Обновление эпиков
    @Override
    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getId())) {
            epics.get(newEpic.getId()).setName(newEpic.getName());
            epics.get(newEpic.getId()).setDescription(newEpic.getDescription());
        }

    }

    //Обновление подзадач
    @Override
    public void updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.getId())
                && newSubtask.getEpicId().equals(subtasks.get(newSubtask.getId()).getEpicId())) {
            Subtask oldSubtask = subtasks.get(newSubtask.getId());
            prioritizedTasks.remove(oldSubtask);
            if (isTaskOverlappingAny(newSubtask)) {
                prioritizedTasks.add(oldSubtask);
                throw new ManagerSaveException("Обновленная подзадача пересекается по времени с существующей");
            }
            subtasks.put(newSubtask.getId(), newSubtask);
            addPrioritizedTasks(newSubtask);
            Epic epic = epics.get(newSubtask.getEpicId());
            updateEpicStatus(epic);
            updateDurationAndStart(epic);
        }
    }

    //Удаление задач по идентификатору
    @Override
    public void deleteByIdTask(Integer id) {
        Task task = tasks.get(id);
        if (task != null) {
            prioritizedTasks.remove(task);
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    //Удаление эпиков по идентификатору
    @Override
    public void deleteByIdEpic(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskId()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    prioritizedTasks.remove(subtask);
                }
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    //Удаление подзадач по идентификатору
    @Override
    public void deleteByIdSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            prioritizedTasks.remove(subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.deleteSubtaskId(id);
            subtasks.remove(id);
            historyManager.remove(id);
            updateEpicStatus(epic);
            updateDurationAndStart(epic);
        }
    }

    //Получение списка всех подзадач определённого эпика


    @Override
    public List<Subtask> getEpicSubtask(Integer epicId) {
        return Optional.ofNullable(epics.get(epicId))
                .map(Epic::getSubtaskId)
                .stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    //Статусы


    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtaskId().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = getEpicSubtask(epic.getId()).stream()
                .allMatch(subtask -> subtask.getStatus() == Status.NEW);

        boolean allDone = getEpicSubtask(epic.getId()).stream()
                .allMatch(subtask -> subtask.getStatus() == Status.DONE);

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRES);
        }
    }

    protected void directTaskPut(Task task) {
        tasks.put(task.getId(), task);
    }

    protected void directEpicPut(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    protected void directSubtaskPut(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
        }
    }

    protected void updateIdCounter(int loadId) {
        if (loadId >= this.id) {
            this.id = loadId + 1;
        }
    }

    private void addPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    private boolean isTasksOverlap(Task task1, Task task2) {
        if (task1.getStartTime() == null || task1.getEndTime() == null ||
                task2.getStartTime() == null || task2.getEndTime() == null) {
            return false;
        }

        return !task1.getEndTime().isBefore(task2.getStartTime()) &&
                !task1.getStartTime().isAfter(task2.getEndTime());
    }

    private boolean isTaskOverlappingAny(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null) {
            return false;
        }

        return prioritizedTasks.stream()
                .filter(t -> !t.getId().equals(task.getId()))
                .anyMatch(t -> isTasksOverlap(task, t));
    }

    public void updateDurationAndStart(Epic epic) {
        List<Subtask> subtasks = getEpicSubtask(epic.getId());

        if (subtasks.isEmpty()) {
            epic.setDuration(Duration.ZERO);
            epic.setStartTime(null);
            epic.setEndTime(null);
            return;
        }

        Duration totalDuration = Duration.ZERO;
        LocalDateTime earliestStart = null;
        LocalDateTime latestEnd = null;

        for (Subtask subtask : subtasks) {
            totalDuration = totalDuration.plus(subtask.getDuration());

            if (subtask.getStartTime() != null) {
                if (earliestStart == null || subtask.getStartTime().isBefore(earliestStart)) {
                    earliestStart = subtask.getStartTime();
                }

                LocalDateTime subtaskEnd = subtask.getEndTime();
                if (latestEnd == null || subtaskEnd.isAfter(latestEnd)) {
                    latestEnd = subtaskEnd;
                }
            }
        }

        epic.setDuration(totalDuration);
        epic.setStartTime(earliestStart);
        epic.setEndTime(latestEnd);
    }
}
