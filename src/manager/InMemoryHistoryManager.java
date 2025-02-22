package manager;
import model.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

private final static int LIST_SIZE = 10;
private final ArrayList<Task> listHistory = new ArrayList<>();
    @Override
    public void add(Task task) {
    if (listHistory.size() == LIST_SIZE){
        listHistory.remove(0);
    }
    if (task != null){
        listHistory.add(task);
    }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return listHistory;
    }


}
