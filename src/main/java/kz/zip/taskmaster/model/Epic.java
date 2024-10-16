package main.java.kz.zip.taskmaster.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Long> subtaskIdList;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIdList = new ArrayList<>();
    }

    public Epic(String name, String description, long id) {
        super(name, description, id);
        subtaskIdList = new ArrayList<>();
    }

    public void addIdToList(long id) {
        subtaskIdList.add(id);
    }

    public void removeIdFromList(long id) {
        subtaskIdList.remove(id);
    }

    public void clearList() {
        subtaskIdList.clear();
    }

    public List<Long> getIdList() {
        List<Long> listToReturn = subtaskIdList;
        return listToReturn;
    }

    @Override
    public String toString() {
        return "main.java.kz.zip.taskmaster.model.Epic{" +
                "name='" + getName() + '\'' +
                ", description.length='" + getDescription().length() + '\'' +
                ", taskCondition=" + getTaskCondition() +
                ", id=" + getId() +
                ", subtaskIdList=" + subtaskIdList +
                '}';
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
