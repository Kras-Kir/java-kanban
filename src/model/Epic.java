package model;

import status.Status;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskId;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.subtaskId = new ArrayList<>();

    }


    public void deleteSubtaskId(Integer id) {
        if (subtaskId.contains(id)) {
            subtaskId.remove(id);
        }
    }

    public void deleteSubtaskId() {
        subtaskId.clear();
    }

    public void addSubtaskId(Integer id) {
        subtaskId.add(id);
    }

    public ArrayList<Integer> getSubtaskId() {
        return new ArrayList<>(subtaskId);

    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskId=" + subtaskId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return id == epic.id && Objects.equals(name, epic.name) && Objects.equals(description, epic.description) &&
                Objects.equals(status, epic.status);
    }


}
