package kz.zip.taskmaster.model;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.enums.TaskType;

import java.util.Objects;

public class Subtask extends Task {
    private static final TaskType taskType = TaskType.SUBTASK;
    private final long epicId;

    public Subtask(String name, String description, long epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, long id, long epicId) {
        super(name, description, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskCondition taskCondition, long id, long epicId) {
        super(name, description, taskCondition, id);
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
        return "SUBTASK" + ',' + getName() + ',' + getDescription() + ',' + getTaskCondition() + ',' + getId() + ',' + getEpicId();
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
