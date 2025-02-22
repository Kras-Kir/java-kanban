package model;
import status.Status;
import java.util.Objects;
public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String name, String description, Status status, Integer epicId) {
        super(name, description,  status);
        this.epicId = epicId;
    }
    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
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
        Subtask subtask = (Subtask) o;
        return id == subtask.id && Objects.equals(name, subtask.name) && Objects.equals(description,subtask.description) &&
                Objects.equals(status,subtask.status);
    }

}
