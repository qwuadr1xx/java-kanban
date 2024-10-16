package main.java.kz.zip.taskmaster.model;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "main.java.kz.zip.taskmaster.model.Subtask{" +
                "name='" + getName() + '\'' +
                ", description.length='" + getDescription().length() + '\'' +
                ", taskCondition=" + getTaskCondition() +
                ", id=" + getId() +
                ", epicId=" + epicId +
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
