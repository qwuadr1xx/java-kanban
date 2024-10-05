public class Task {
    protected String name;
    protected String description;
    protected TaskCondition CONDITION;

    Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
