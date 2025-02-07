public class Subtask extends Task{
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


}
