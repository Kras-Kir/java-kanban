package model;

import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskId;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, Status.NEW, Duration.ZERO, null);
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
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtaskIds=" + subtaskId +
                ", duration=" + duration.toMinutes() + "m" +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
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
