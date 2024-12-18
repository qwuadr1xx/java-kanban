package kz.zip.taskmaster.model;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private static final TaskType taskType = TaskType.SUBTASK;
    private final long epicId;

    public Subtask(String name, String description, long epicId, Duration duration,
                   LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, long id, long epicId, Duration duration,
                   LocalDateTime startTime) {
        super(name, description, id, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskCondition taskCondition, long id, long epicId, Duration duration,
                   LocalDateTime startTime) {
        super(name, description, taskCondition, id, duration, startTime);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", taskCondition=" + this.getTaskCondition() +
                ", id=" + this.getId() +
                ", duration=" + this.getDuration() +
                ", startTime=" + this.getStartTime() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask subtask)) return false;
        if (!super.equals(o)) return false;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
