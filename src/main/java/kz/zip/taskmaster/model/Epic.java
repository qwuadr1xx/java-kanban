package kz.zip.taskmaster.model;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.enums.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private static final TaskType taskType = TaskType.EPIC;
    private final List<Long> subtaskIdList;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIdList = new ArrayList<>();
    }

    public Epic(String name, String description, long id) {
        super(name, description, id);
        subtaskIdList = new ArrayList<>();
    }

    public Epic(String name, String description, TaskCondition taskCondition, long id) {
        super(name, description, taskCondition, id);
        subtaskIdList = new ArrayList<>();
    }

    public void addIdToList(long id) {
        subtaskIdList.add(id);
    }

    public void removeIdFromList(long id) {
        subtaskIdList.remove(id);
    }

    public List<Long> getIdList() {
        return new ArrayList<>(subtaskIdList);
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public String toString() {
        return "EPIC" + ',' + getName() + ',' + getDescription() + ',' + getTaskCondition() + ',' + getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic epic)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(subtaskIdList, epic.subtaskIdList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIdList);
    }
}
