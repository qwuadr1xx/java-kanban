package kz.zip.taskmaster.service;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.model.Epic;
import kz.zip.taskmaster.model.Subtask;
import kz.zip.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    private Map<Long, Task> tasks;
    private Map<Long, Epic> epics;
    private Map<Long, Subtask> subtasks;
    InMemoryHistoryManager inMemoryHistoryManager;
    private long idCounter;
    Set<Task> PrioritizedTasks;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        inMemoryHistoryManager = new InMemoryHistoryManager();
        idCounter = 1;
        PrioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    @Override
    public List<Task> getListOfTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getListOfEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getListOfSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getTaskById(long id) {
        inMemoryHistoryManager.addToHistoryList(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(long id) {
        inMemoryHistoryManager.addToHistoryList(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(long id) {
        inMemoryHistoryManager.addToHistoryList(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public boolean addTask(Task task) {
        long id = generateId();
        Task taskToAdd = new Task(task.getName(), task.getDescription(), id, task.getDuration(), task.getStartTime());
        tasks.put(id, taskToAdd);
        boolean isIntersected = false;
        if (!PrioritizedTasks.isEmpty()) {
            isIntersected = PrioritizedTasks.stream().anyMatch(task1 -> isIntersection(taskToAdd, task1));
        }
        addToPrioritizedTasks(taskToAdd);
        return isIntersected;
    }

    @Override
    public void addEpic(Epic epic) {
        long id = generateId();
        Epic epicToAdd = new Epic(epic.getName(), epic.getDescription(), id);
        epics.put(id, epicToAdd);
    }

    @Override
    public boolean addSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return false;
        }
        long id = generateId();
        Subtask subtaskToAdd = new Subtask(subtask.getName(), subtask.getDescription(), id, subtask.getEpicId(),
                subtask.getDuration(), subtask.getStartTime());
        subtasks.put(id, subtaskToAdd);
        boolean isIntersected = false;
        if (!PrioritizedTasks.isEmpty()) {
            isIntersected = PrioritizedTasks.stream().anyMatch(task1 -> isIntersection(subtaskToAdd, task1));
        }
        addToPrioritizedTasks(subtaskToAdd);
        epics.get(subtaskToAdd.getEpicId()).addToList(subtaskToAdd);
        updateEpicCondition(subtaskToAdd.getEpicId());
        return isIntersected;
    }

    @Override
    public boolean updateTask(Task task) {
        PrioritizedTasks.remove(tasks.get(task.getId()));
        Task taskToAdd = new Task(task.getName(), task.getDescription(), task.getId(), task.getDuration(), task.getStartTime());
        boolean isIntersected = PrioritizedTasks.stream().anyMatch(task1 -> isIntersection(taskToAdd, task1));
        addToPrioritizedTasks(taskToAdd);
        tasks.put(taskToAdd.getId(), taskToAdd);
        return isIntersected;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        PrioritizedTasks.remove(epics.get(epic.getId()));
        Epic epicToAdd = new Epic(epic.getName(), epic.getDescription(), epic.getId(), new ArrayList<>(epic.getSubtaskList()));
        boolean isIntersected = PrioritizedTasks.stream().anyMatch(epic1 -> isIntersection(epicToAdd, epic1));
        addToPrioritizedTasks(epicToAdd);
        epics.put(epicToAdd.getId(), epicToAdd);
        return isIntersected;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        PrioritizedTasks.remove(subtasks.get(subtask.getId()));
        Subtask subtaskToAdd = new Subtask(subtask.getName(), subtask.getDescription(), subtask.getId(), subtask.getEpicId(),
                subtask.getDuration(), subtask.getStartTime());
        boolean isIntersected = PrioritizedTasks.stream().anyMatch(subtask1 -> isIntersection(subtaskToAdd, subtask1));
        addToPrioritizedTasks(subtaskToAdd);
        subtasks.put(subtaskToAdd.getId(), subtaskToAdd);
        updateEpicCondition(subtaskToAdd.getEpicId());
        return isIntersected;
    }

    @Override
    public void removeTask(long id) {
        PrioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void removeEpic(long id) {
        if (!epics.containsKey(id)) {
            return;
        }
        Epic epic = epics.get(id);
        epic.getSubtaskList()
                .forEach(PrioritizedTasks::remove);
        epic.getIdList()
                .forEach(subtasks::remove);
        epics.remove(id);
    }

    @Override
    public void removeSubtask(long id) {
        if (!subtasks.containsKey(id)) {
            return;
        }
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeFromList(subtask);
        subtasks.remove(id);
        updateEpicCondition(subtask.getEpicId());
        PrioritizedTasks.remove(subtasks.get(id));
    }

    public void updateEpicCondition(long epicId) {
        Epic epic = epics.get(epicId);
        if (epic.getSubtaskList().isEmpty()) {
            return;
        }
        List<TaskCondition> taskConditionList = epic.getSubtaskList().stream()
                .map(Subtask::getTaskCondition)
                .toList();
        if (taskConditionList.contains(TaskCondition.IN_PROGRESS)) {
            epic.setTaskCondition(TaskCondition.IN_PROGRESS);
        } else if (!taskConditionList.contains(TaskCondition.IN_PROGRESS) && !taskConditionList.contains(TaskCondition.NEW)) {
            epic.setTaskCondition(TaskCondition.DONE);
        } else {
            epic.setTaskCondition(TaskCondition.NEW);
        }
    }

    public Set<Task> getPrioritizedTasks() {
        return PrioritizedTasks;
    }

    public void setTasks(Map<Long, Task> map) {
        tasks = map;
        PrioritizedTasks.addAll(tasks.values());
    }

    public void setEpics(Map<Long, Epic> map) {
        epics = map;
        PrioritizedTasks.addAll(epics.values());
    }

    public void setSubtasks(Map<Long, Subtask> map) {
        subtasks = map;
        PrioritizedTasks.addAll(subtasks.values());
    }

    public void setIdCounter(long idCounter) {
        this.idCounter = idCounter;
    }

    private long generateId() {
        return idCounter++;
    }

    private void addToPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            PrioritizedTasks.add(task);
        }
    }

    private boolean isIntersection(Task task1, Task task2) {
        return !task1.getEndTime().isBefore(task2.getStartTime()) && !task1.getStartTime().isAfter(task2.getEndTime())
                || !task2.getEndTime().isBefore(task1.getStartTime()) && !task2.getStartTime().isAfter(task1.getEndTime());
    }
}
