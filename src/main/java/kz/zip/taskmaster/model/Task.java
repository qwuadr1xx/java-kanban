package kz.zip.taskmaster.model;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private static final TaskType taskType = TaskType.TASK;
    private final String name;
    private final String description;
    private TaskCondition taskCondition;
    private long id;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, Duration duration,
                LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        taskCondition = TaskCondition.NEW;
    }

    public Task(String name, String description, long id, Duration duration,
                LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        taskCondition = TaskCondition.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, TaskCondition taskCondition, long id, Duration duration,
                LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.taskCondition = taskCondition;
        this.duration = duration;
        this.startTime = startTime;
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

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public void setTaskCondition(TaskCondition taskCondition) {
        this.taskCondition = taskCondition;
    }

    public void changeTaskCondition(TaskCondition taskCondition) {
        this.taskCondition = taskCondition;
    }

    protected void setDuration(Duration duration) {
        this.duration = duration;
    }

    protected void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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
