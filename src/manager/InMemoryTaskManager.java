package manager;
import util.Managers;
import model.Epic;
import model.Task;
import model.Subtask;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import status.Status;

public class InMemoryTaskManager implements TaskManager {
    private Integer id = 0;

    public HistoryManager historyManager = Managers.getDefaultHistory();



    private Map<Integer,Task> tasks = new HashMap<>();
    private Map<Integer,Epic> epics = new HashMap<>();
    private Map<Integer,Subtask> subtasks = new HashMap<>();


    public List<Task> getHistory(){
        return historyManager.getHistory();
    }


    private Integer counterId(){
        id++;
        return id;
    }

    //Получение списка задач
    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> listTask = new ArrayList<>();
        for(Task task : tasks.values()){
            listTask.add(task);
        }
        return listTask;
    }
    //Получение списка эпиков
    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> listEpic = new ArrayList<>();
        for(Epic epic : epics.values()){
            listEpic.add(epic);
        }
        return listEpic;
    }
    //Получение списка подзадач
    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        for(Subtask subtask : subtasks.values()){
            listSubtask.add(subtask);
        }
        return listSubtask;
    }
    //Удаление всех задач
    @Override
    public void deleteTask(){
        tasks.clear();
    }
    //Удаление всех эпиков
    @Override
    public void deleteEpic(){
        subtasks.clear();
        epics.clear();
    }
    //Удаление всех подзадач
    @Override
    public void deleteSubtask(){
        subtasks.clear();
        for (Epic epic : epics.values()){
            epic.deleteSubtaskId();
            updateEpicStatus(epic);
        }
    }
    //Получение задач по идентификатору
    @Override
    public Task taskById(Integer id){
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }
    //Получение эпиков по идентификатору
    @Override
    public Epic epicById(Integer id){
        historyManager.add(epics.get(id));
        return epics.get(id);
    }
    //Получение подзадач по идентификатору
    @Override
    public Subtask subtaskById(Integer id){
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }
    //Создание задач
    @Override
    public void addTask(Task task){
        task.setId(counterId());
        tasks.put(task.getId(),task);
    }
    //Создание эпиков
    @Override
    public void addEpic(Epic epic){
        epic.setId(counterId());
        epics.put(epic.getId(),epic);
    }
    //Создание подзадач
    @Override
    public void addSubtask(Subtask subtask){
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null){
            subtask.setId(counterId());
            epic.addSubtaskId(subtask.getId());
            subtasks.put(subtask.getId(),subtask);
            updateEpicStatus(epic);

        }
    }
    //Обновление задач
    @Override
    public void updateTask (Task newTask){
        if (tasks.containsKey(newTask.getId())) {
            tasks.put(newTask.getId(), newTask);
        }
    }
    //Обновление эпиков
    @Override
    public void updateEpic (Epic newEpic){
        if (epics.containsKey(newEpic.getId())) {
            epics.get(newEpic.getId()).setName(newEpic.getName());
            epics.get(newEpic.getId()).setDescription(newEpic.getDescription());
        }

    }
    //Обновление подзадач
    @Override
    public void updateSubtask (Subtask newSubtask){
        if (subtasks.containsKey(newSubtask.getId())
                && newSubtask.getEpicId().equals(subtasks.get(newSubtask.getId()).getEpicId())){
            subtasks.put(newSubtask.getId(),newSubtask);
            Epic epic = epics.get(newSubtask.getEpicId());
            updateEpicStatus(epic);
        }

    }
    //Удаление задач по идентификатору
    @Override
    public void deleteByIdTask(Integer id){
        tasks.remove(id);

    }
    //Удаление эпиков по идентификатору
    @Override
    public void deleteByIdEpic(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskId()) {
                subtasks.remove(subtaskId);
            }
        }

        epics.remove(id);
    }
    //Удаление подзадач по идентификатору
    @Override
    public void deleteByIdSubtask(Integer id){
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.deleteSubtaskId(id);
            subtasks.remove(id);

            updateEpicStatus(epic);
        }

    }

    //Получение списка всех подзадач определённого эпика
    @Override
    public ArrayList<Subtask> getEpicSubtask(Integer id){
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        if (epics.containsKey(id)) {
            for (Integer idd : epics.get(id).getSubtaskId()) {
                listSubtask.add(subtasks.get(idd));
            }
        }
        return listSubtask;
    }

    //Статусы

    private void  updateEpicStatus(Epic epic){

        boolean statusNew = false;
        boolean statusInProgress = false;
        boolean statusDone = false;
        for(Subtask subtask : getEpicSubtask(epic.getId())){
            if (subtask.getStatus().equals(Status.NEW)){
                statusNew = true;
            }
            if (subtask.getStatus().equals(Status.IN_PROGRES)){
                statusInProgress = true;
            }
            if (subtask.getStatus().equals(Status.DONE)){
                statusDone = true;
            }
        }
        if (epic.getSubtaskId().isEmpty()){
            epic.setStatus(Status.NEW);
        }
        if (statusNew == true && statusInProgress == false && statusDone == false){
            epic.setStatus(Status.NEW);
        } else if (statusNew == false && statusDone == true && statusInProgress == false) {
            epic.setStatus(Status.DONE);
        }
        else {
            epic.setStatus(Status.IN_PROGRES);
        }

    }
}
