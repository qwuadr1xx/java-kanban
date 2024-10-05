import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Task> epics = new HashMap<>();
    private Map<Integer, Task> subtasks = new HashMap<>();

    //короче думаю, что нет смысла создавать 3 разных мапа, а просто при выводе получать класс каждого таска
    public List<String> getListOfTasks() {
        if (!tasks.isEmpty()) {
            List<String> listOfTasks = new ArrayList<>();
            for (Object task : tasks.values()) {
                listOfTasks.add(String.valueOf(task));
            }
            return listOfTasks;
        } else {
            return null;
        }
    }

    public List<String> getListOfEpics() {
        if (!epics.isEmpty()) {
            List<String> listOfEpics = new ArrayList<>();
            for (Object epic : epics.values()) {
                listOfEpics.add(String.valueOf(epic));
            }
            return listOfEpics;
        } else {
            return null;
        }
    }

    public List<String> getListOfSubtasks() {
        if (!subtasks.isEmpty()) {
            List<String> listOfSubtasks = new ArrayList<>();
            for (Object subtask : subtasks.values()) {
                listOfSubtasks.add(String.valueOf(subtask));
            }
            return listOfSubtasks;
        } else {
            return null;
        }
    }

    public void clearTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(int id) {
        if (!tasks.isEmpty()) {
            if (tasks.containsKey(id)) { //я так думаю, что containsKey тут не нужен
                for (Integer taskId : tasks.keySet()) {
                    if (taskId == id) {
                        return tasks.get(id);
                    }
                }
            }
        }
        if (!epics.isEmpty()) {
            if (epics.containsKey(id)) {
                for (Integer taskId : epics.keySet()) {
                    if (taskId == id) {
                        return epics.get(id);
                    }
                }
            }
        }
        if (!subtasks.isEmpty()) {
            if (subtasks.containsKey(id)) {
                for (Integer taskId : subtasks.keySet()) {
                    if (taskId == id) {
                        return subtasks.get(id);
                    }
                }
            }
        }
        return null;
    }

/*    public boolean addTask(Task task) {
        int id = generateId();
//        task.setId(generateId());
        task.CONDITION = TaskCondition.NEW;
        tasks.put(id, task);
        return true;
    }

    public boolean addEpic(Epic epic) {
        int id = generateId();
//        epic.setId(generateId());
        epic.CONDITION = TaskCondition.NEW;

        return true;
    }

    public boolean addSubtask(Subtask subtask) {
        int id = generateId();
//        subtask.setId(generateId());
        subtask.CONDITION = TaskCondition.NEW;
        return true;
    } */

    public void addTask(Task task) {
        switch (task.getClass().toString()) {
            case "Task" -> {
                task.CONDITION = TaskCondition.NEW;
                tasks.put(generateId(task), task);
                return;
            }
            case "Epic" -> {
                task.CONDITION = TaskCondition.NEW;
                epics.put(generateId(task), task);
                return;
            }
            case "Subtask" -> {
                task.CONDITION = TaskCondition.NEW;
                subtasks.put(generateId(task), task);
                return;
            }
            default -> {
                return;
            }
        }
    }

    public void updateTask(Task task) {

    }

    private int generateId(Task task) {
        return hashCode(task);
    }

    public int hashCode(Task task) {
        int hash = 17;
        if (task.getName() != null) {
            hash = task.getName().hashCode();
        }
        if (task.getDescription() != null) {
            hash = hash + task.getDescription().hashCode();
        }
        return hash;
    }

}
