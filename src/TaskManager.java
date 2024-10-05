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
    }

    public void clearSubtasks() {
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
        task.CONDITION = TaskCondition.NEW;
        long id = generateId();
        task = new Task(id);
        tasks.put(id, task);
    }

    public void addEpic(Epic epic) {
        epic.CONDITION = TaskCondition.NEW;
        long id = generateId();
        epic = new Epic(id);
        epics.put(id, epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.CONDITION = TaskCondition.NEW;
        long id = generateId();
        subtask = new Subtask(id, epics.get(subtask.getEpicId()));
        subtasks.put(id, subtask);

    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    private long generateId() {
        return idCounter++;
    }
}
