package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList<Task> customList = new CustomLinkedList<>();
    private HashMap<Integer, Node> mapNode = new HashMap<>();


    public class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;

        void linkLast(Node node) {
            final Node<T> oldTail = tail;
            node.prev = oldTail;
            tail = node;
            if (oldTail == null) {
                head = node;
            } else {
                oldTail.next = node;
            }
        }

        List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node node = head;
            while (node != null) {
                tasks.add((Task) node.data);
                node = node.next;
            }
            return tasks;

        }

        void removeNode(Node node) {
            if (node == null) return;
            Node nodeNext = node.next;
            Node nodePrev = node.prev;

            if (nodeNext != null && nodePrev == null) {
                head = null;
                tail = null;
            }
            if (nodeNext != null) {
                head = nodeNext;
                nodeNext.prev = null;
            }
            if (nodePrev != null) {
                tail = nodePrev;
                nodePrev.next = null;
            }
            if (nodeNext != null && nodePrev != null) {
                nodePrev.next = nodeNext;
                nodeNext.prev = nodePrev;
            }
        }
    }


    @Override
    public void add(Task task) {
        if (task != null) {
            Node<Task> node = new Node<>(task, null, null);
            if (mapNode.containsKey(node.data.getId())) {
                remove(node.data.getId());
            }
            customList.linkLast(node);
            mapNode.put(task.getId(), node);
        }
    }

    @Override
    public void remove(int id) {
        customList.removeNode(mapNode.get(id));
        mapNode.remove(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return (ArrayList<Task>) customList.getTasks();
    }


}
