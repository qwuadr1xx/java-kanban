package main.java.kz.zip.taskmaster.model;

import main.java.kz.zip.taskmaster.enums.TaskCondition;

import java.util.Objects;

public class Task {
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

    @Override
    public String toString() {
        return "main.java.kz.zip.taskmaster.model.Task{" +
                "name='" + name + '\'' +
                ", description.length='" + description.length() + '\'' +
                ", taskCondition=" + taskCondition +
                ", id=" + id +
                '}';
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
