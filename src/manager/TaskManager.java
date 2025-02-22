package manager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getHistory();

    //Получение списка задач
    ArrayList<Task> getTasks();

    //Получение списка эпиков
    ArrayList<Epic> getEpics();

    //Получение списка подзадач
    ArrayList<Subtask> getSubtasks();

    //Удаление всех задач
    void deleteTask();

    //Удаление всех эпиков
    void deleteEpic();

    //Удаление всех подзадач
    void deleteSubtask();

    //Получение задач по идентификатору
    Task taskById(Integer id);

    //Получение эпиков по идентификатору
    Epic epicById(Integer id);

    //Получение подзадач по идентификатору
    Subtask subtaskById(Integer id);

    //Создание задач
    void addTask(Task task);

    //Создание эпиков
    void addEpic(Epic epic);

    //Создание подзадач
    void addSubtask(Subtask subtask);

    //Обновление задач
    void updateTask(Task newTask);

    //Обновление эпиков
    void updateEpic(Epic newEpic);

    //Обновление подзадач
    void updateSubtask(Subtask newSubtask);

    //Удаление задач по идентификатору
    void deleteByIdTask(Integer id);

    //Удаление эпиков по идентификатору
    void deleteByIdEpic(Integer id);

    //Удаление подзадач по идентификатору
    void deleteByIdSubtask(Integer id);

    //Получение списка всех подзадач определённого эпика
    ArrayList<Subtask> getEpicSubtask(Integer id);


}
