package kz.zip.taskmaster.model;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.enums.TaskType;

import java.util.Comparator;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Epic extends Task {
    private static final TaskType taskType = TaskType.EPIC;
    private final List<Subtask> subtaskList;

    public Epic(String name, String description) {
        super(name, description, Duration.ZERO, null);
        subtaskList = new ArrayList<>();
    }

    public Epic(String name, String description, long id) {
        super(name, description, id, Duration.ZERO, null);
        subtaskList = new ArrayList<>();
    }

    public Epic(String name, String description, TaskCondition taskCondition, long id, Duration duration,
                LocalDateTime startTime) {
        super(name, description, taskCondition, id, duration, startTime);
        subtaskList = new ArrayList<>();
    }

    public Epic(String name, String description, long id, ArrayList<Subtask> subtaskList) {
        super(name, description, id, Duration.ZERO, null);
        this.subtaskList = subtaskList;
        setDuration(getEpicDuration());
        setStartTime(getEpicEarliestStartTime());
    }

    public void addToList(Subtask subtask) {
        subtaskList.add(subtask);
        setDuration(getEpicDuration());
        setStartTime(getEpicEarliestStartTime());
    }

    public void removeFromList(Subtask subtask) {
        subtaskList.remove(subtask);
        if (subtaskList.isEmpty()) {
            setDuration(Duration.ZERO);
            setStartTime(null);
        }
        setDuration(getEpicDuration());
        setStartTime(getEpicEarliestStartTime());

    }

    public List<Long> getIdList() {
        return subtaskList.stream()
                .map(Task::getId)
                .collect(Collectors.toList());
    }

    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskList);
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtaskList.isEmpty()) {
            return null;
        }

        Subtask localSubtask = subtaskList.stream()
                .max(Comparator.comparing(Task::getEndTime))
                .get();
        return localSubtask.getStartTime().plus(localSubtask.getDuration());
    }

    private Duration getEpicDuration() {
        if (subtaskList.isEmpty()) {
            return Duration.ZERO;
        }

        return subtaskList.stream()
                .map(Task::getDuration)
                .reduce(Duration::plus).get();
    }

    private LocalDateTime getEpicEarliestStartTime() {
        if (subtaskList.isEmpty()) {
            return null;
        }

        return subtaskList.stream()
                .map(Subtask::getStartTime)
                .min(Comparator.naturalOrder()).get();
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
        return Objects.equals(getIdList(), epic.getIdList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIdList());
    }
}
