import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class TaskManager {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, Epic> epics = new HashMap<>();
    private final Map<Long, Subtask> subtasks = new HashMap<>();

    private long idCounter = 1;

    public List<Task> getListOfTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getListOfEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getListOfSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void clearSubtasks() {
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(long id) {
        return tasks.get(id);
    }

    public Epic getEpicById(long id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(long id) {
        return subtasks.get(id);
    }

    public void addTask(Task task) {
        long id = generateId();
        task = new Task(task.getName(), task.getDescription(), id);
        tasks.put(id, task);
    }

    public void addEpic(Epic epic) {
        long id = generateId();
        epic = new Epic(epic.getName(), epic.getDescription(), id);
        tasks.put(id, epic);
    }

    public void addSubtask(Subtask subtask) {
        long id = generateId();
        subtask = new Subtask(subtask.getName(), subtask.getDescription(), id);
        epics.get(subtask.getEpicId()).addIdToList(id);
        tasks.put(id, subtask);
    }

    public void updateTask(Task task) {
        task = new Task(task.getName(), task.getDescription(), task.getId());
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epic = new Epic(epic.getName(), epic.getDescription(), epic.getId());
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtask = new Subtask(subtask.getName(), subtask.getDescription(), subtask.getId());
        subtasks.put(subtask.getId(), subtask);
    }

    public void removeTask(long id) {
        tasks.remove(id);
    }

    public void removeEpic(long id) {
        Epic epic = epics.get(id);
        for (Long subtaskId : epic.getIdList()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public void removeSubtask(long id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic.getIdList().size() == 1) {
            epic.clearList();
        } else {
            epic.removeIdFromList(id);
        }
        subtasks.remove(id);
    }


    private void updateEpicCondition(long epicId) {
        Epic epic = epics.get(epicId);
        if (epic.getIdList() != null) {
            for (long id : epic.getIdList()) {
                Subtask subtask = subtasks.get(id);
                if (subtask.getTaskCondition().equals(TaskCondition.NEW)) {
                    epic.setTaskCondition(TaskCondition.NEW);
                    return;
                }
            }
        } else {
            epic.setTaskCondition(TaskCondition.NEW);
        }
        for (long id : epic.getIdList()) {
            Subtask subtask = subtasks.get(id);
            if (subtask.getTaskCondition() != TaskCondition.DONE) {
                epic.setTaskCondition(TaskCondition.IN_PROGRESS);
                return;
            } else {
                epic.setTaskCondition(TaskCondition.DONE);
            }
        }
    }

    private long generateId() {
        return idCounter++;
    }
}
