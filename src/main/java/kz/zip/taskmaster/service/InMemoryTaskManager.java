package kz.zip.taskmaster.service;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.model.Epic;
import kz.zip.taskmaster.model.Subtask;
import kz.zip.taskmaster.model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, Epic> epics = new HashMap<>();
    private final Map<Long, Subtask> subtasks = new HashMap<>();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private long idCounter = 1;

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
    public void addTask(Task task) {
        long id = generateId();
        tasks.put(id, new Task(task.getName(), task.getDescription(), id));
    }

    @Override
    public void addEpic(Epic epic) {
        long id = generateId();
        epics.put(id, new Epic(epic.getName(), epic.getDescription(), id));
    }

    @Override
    public boolean addSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return false;
        } else {
            long id = generateId();
            subtasks.put(id, new Subtask(subtask.getName(), subtask.getDescription(), id, subtask.getEpicId()));
            epics.get(subtask.getEpicId()).addIdToList(id);
            updateEpicCondition(subtask.getEpicId());
            return true;
        }
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), new Task(task.getName(), task.getDescription(), task.getId()));
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), new Epic(epic.getName(), epic.getDescription(), epic.getId()));
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), new Subtask(subtask.getName(), subtask.getDescription(), subtask.getId()));
        updateEpicCondition(subtask.getEpicId());
    }

    @Override
    public void removeTask(long id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpic(long id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Long subtaskId : epic.getIdList()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    @Override
    public void removeSubtask(long id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeIdFromList(id);
            subtasks.remove(id);
            updateEpicCondition(subtask.getEpicId());
        }
    }

    public void updateEpicCondition(long epicId) {
        Epic epic = epics.get(epicId);
        List<TaskCondition> taskConditionList = new ArrayList<>();
        for (long id : epic.getIdList()) {
            taskConditionList.add(getSubtaskById(id).getTaskCondition());
        }
        if (taskConditionList.contains(TaskCondition.IN_PROGRESS)) {
            epic.setTaskCondition(TaskCondition.IN_PROGRESS);
        } else if (!taskConditionList.contains(TaskCondition.IN_PROGRESS) && !taskConditionList.contains(TaskCondition.NEW)) {
            epic.setTaskCondition(TaskCondition.DONE);
        } else {
            epic.setTaskCondition(TaskCondition.NEW);
        }
    }

    private long generateId() {
        return idCounter++;
    }
}
