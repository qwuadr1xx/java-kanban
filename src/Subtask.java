public class Subtask extends Task {
    private long epicId;

    public Subtask(String name, String description, long epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, long id, long epicId) {
        super(name, description, id);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }
}
