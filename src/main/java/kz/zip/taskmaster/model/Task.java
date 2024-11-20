package kz.zip.taskmaster.model;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.enums.TaskType;

import java.util.Objects;

public class Task {
    private static final TaskType taskType = TaskType.TASK;
    private final String name;
    private final String description;
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

    public Task(String name, String description, TaskCondition taskCondition, long id) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.taskCondition = taskCondition;
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

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskCondition(TaskCondition taskCondition) {
        this.taskCondition = taskCondition;
    }

    public void changeTaskCondition(TaskCondition taskCondition) {
        this.taskCondition = taskCondition;
    }

    @Override
    public String toString() {
        return "TASK" + ',' + name + ',' + description + ',' + taskCondition + ',' + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && taskCondition == task.taskCondition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, taskCondition, id);
    }
}
