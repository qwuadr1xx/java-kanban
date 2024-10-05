public class Task {
    private String name;
    private String description;
    private TaskCondition taskCondition;
    private long id;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, long id) {
        this.name = name;
        this.description = description;
        this.id = id;
        taskCondition = TaskCondition.NEW;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskCondition getTaskCondition() {
        return taskCondition;
    }

    public long getId() {
        return id;
    }

    public void setTaskCondition(TaskCondition taskCondition) {
        this.taskCondition = taskCondition;
    }

    public void changeTaskCondition(TaskCondition taskCondition) {
        this.taskCondition = taskCondition;
    }
}
