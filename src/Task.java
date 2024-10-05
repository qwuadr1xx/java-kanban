public class Task {
    protected String name;
    protected String description;
    protected TaskCondition taskCondition;
    protected long id;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void changeTaskCondition(TaskCondition taskCondition) {
        this.taskCondition = taskCondition;
    }
}
