import java.util.ArrayList;
import java.util.HashMap;
public class TaskManager {
    Integer id = 0;

    HashMap<Integer,Task> tasks = new HashMap<>();
    HashMap<Integer,Epic> epics = new HashMap<>();
    HashMap<Integer,Subtask> subtasks = new HashMap<>();

    public Integer counterId(){
        id++;
        return id;
    }

//Получение списка задач
    public ArrayList<Task> getTasks() {
        ArrayList<Task> listTask = new ArrayList<>();
        for(Task task : tasks.values()){
            listTask.add(task);
        }
        return listTask;
    }
//Получение списка эпиков
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> listEpic = new ArrayList<>();
        for(Epic epic : epics.values()){
            listEpic.add(epic);
        }
        return listEpic;
    }
//Получение списка подзадач
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        for(Subtask subtask : subtasks.values()){
            listSubtask.add(subtask);
        }
        return listSubtask;
    }
    //Удаление всех задач
    public void deleteTask(){
        tasks.clear();
    }
    //Удаление всех эпиков
    public void deleteEpic(){
        epics.clear();
    }
    //Удаление всех подзадач
    public void deleteSubtask(){
        subtasks.clear();
    }
    //Получение задач по идентификатору
    public Task taskById(Integer id){
        return tasks.get(id);
    }
    //Получение эпиков по идентификатору
    public Epic epicById(Integer id){
        return epics.get(id);
    }
    //Получение подзадач по идентификатору
    public Subtask subtaskById(Integer id){
        return subtasks.get(id);
    }
    //Создание задач
    public void addTask(Task task){
        tasks.put(task.id,task);
    }
    //Создание эпиков
    public void addEpic(Epic epic){
        epics.put(epic.id,epic);
    }
    //Создание подзадач
    public void addSubtask(Subtask subtask){
        subtasks.put(subtask.id,subtask);
        Epic epic = epics.get(subtask.epicId);
        updateEpic(epic);
    }
    //Обновление задач
    public void updateTask (Task task){
        tasks.put(task.id,task);
    }
    //Обновление эпиков
    public ArrayList<Integer> getSubtaskIdOfEpic(Epic epic){
        ArrayList<Integer> subtaskIdOfEpic = new ArrayList<>();
        for (Subtask subtask : getEpicSubtask(epic)){
            subtaskIdOfEpic.add(subtask.id);
        }
        return subtaskIdOfEpic;
    }

    public void updateEpic (Epic epic){
        epic.subtaskId = getSubtaskIdOfEpic(epic);
        epic.status = getStatusEpic(epic);
        epics.put(epic.id,epic);
    }
    //Обновление подзадач

    public void updateSubtask (Subtask subtask){
        subtasks.put(subtask.id,subtask);
        Epic epic = epics.get(subtask.epicId);
        updateEpic(epic);
    }
    //Удаление задач по идентификатору
    public void deleteByIdTask(Integer id){
        tasks.remove(id);

        }
    //Удаление эпиков по идентификатору
    public void deleteByIdEpic(Integer id) {
        for (Epic epic : epics.values()){
            if (epic.id == id) {
                for (Integer subtaskId : epic.subtaskId) {
                    subtasks.remove(subtaskId);
                }
            }
        }
        epics.remove(id);
    }
    //Удаление подзадач по идентификатору
    public void deleteByIdSubtask(Integer id){
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.epicId);
        subtasks.remove(id);
        updateEpic(epic);

    }

    //Получение списка всех подзадач определённого эпика
    public ArrayList<Subtask> getEpicSubtask(Epic epic){
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        for (Subtask subtask : subtasks.values()){
            if(epic.id == subtask.epicId){
                listSubtask.add(subtask);
            }
        }
        return listSubtask;
    }

    //Статусы
    public Status getStatusEpic(Epic epic){
        Status status;
        boolean statusNew = false;
        boolean statusInProgress = false;
        boolean statusDone = false;
        for(Subtask subtask : getEpicSubtask(epic)){
            if (subtask.status.equals(Status.NEW)){
                statusNew = true;
            }
            if (subtask.status.equals(Status.IN_PROGRES)){
                statusInProgress = true;
            }
            if (subtask.status.equals(Status.DONE)){
                statusDone = true;
            }
        }

        if (statusNew == true && statusInProgress == false && statusDone == false){
            status = Status.NEW;
        } else if (statusNew == false && statusDone == true && statusInProgress == false) {
            status = Status.DONE;
        }
        else {
            status = Status.IN_PROGRES;
        }
        return status;
    }
}
