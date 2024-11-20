package kz.zip.taskmaster.service;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.enums.TaskType;
import kz.zip.taskmaster.exception.ManagerSaveException;
import kz.zip.taskmaster.model.*;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final File file; //Файл сохранения

    FileBackedTaskManager(File file) {
        this.file = file;
    }


    @Override
    public void clearTasks() {
        super.clearTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addSubtask(Subtask subtask) {
        if (super.addSubtask(subtask)) {
            try {
                save();
            } catch (ManagerSaveException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeTask(long id) {
        super.removeTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeSubtask(long id) {
        super.removeSubtask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeEpic(long id) {
        super.removeEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEpicCondition(long epicId) {
        super.updateEpicCondition(epicId);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("type,name,description,condition,id,epicId");
            writer.newLine();
            for (Task task : getListOfTasks()) {
                writer.write(toCSV(task));
                writer.newLine();
            }
            for (Epic epic : getListOfEpics()) {
                writer.write(toCSV(epic));
                writer.newLine();
            }
            for (Subtask subtask : getListOfSubtasks()) {
                writer.write(toCSV(subtask));
                writer.newLine();
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Проблема с сохранением задач", exception);
        }
    }


    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        Map<Long, Task> tasks = new HashMap<>();
        Map<Long, Epic> epics = new HashMap<>();
        Map<Long, Subtask> subtasks = new HashMap<>();
        long id = 0;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String line = fileReader.readLine();
            while ((line = fileReader.readLine()) != null) {
                Task task = fromSCV(line);
                if (task != null) {
                    id = Long.max(id, task.getId());
                }
                switch (Objects.requireNonNull(task).getTaskType().toString()) {
                    case ("TASK"):
                        tasks.put(task.getId(), task);
                        break;
                    case ("EPIC"):
                        epics.put(task.getId(), (Epic) task);
                        break;
                    case ("SUBTASK"):
                        Subtask subtask = (Subtask) task;
                        subtasks.put(task.getId(), subtask);
                        Epic parentEpic = epics.get(subtask.getEpicId());
                        if (parentEpic != null) {
                            parentEpic.addIdToList(subtask.getId());
                        }
                        break;
                    case null, default:
                        break;
                }
            }
            fileBackedTaskManager.setIdCounter(id);
            fileBackedTaskManager.setTasks(tasks);
            fileBackedTaskManager.setEpics(epics);
            fileBackedTaskManager.setSubtasks(subtasks);
        } catch (IOException exception) {
            throw new ManagerSaveException("Проблема с загрузкой задач", exception);
        }
        return fileBackedTaskManager;
    }

    private String toCSV(Task task) {
        TaskType taskType = task.getTaskType();
        String name = task.getName();
        String description = task.getDescription();
        TaskCondition taskCondition = task.getTaskCondition();
        long id = task.getId();
        long epicId = -1L;
        if (task instanceof Subtask) {
            epicId = ((Subtask) task).getEpicId();
        }
        String stringCSV;
        if (epicId == -1) {
            stringCSV = String.format("%s,%s,%s,%s,%d",
                    taskType.toString(), name, description, taskCondition, id);
        } else {
            stringCSV = String.format("%s,%s,%s,%s,%d,%d",
                    taskType.toString(), name, description, taskCondition, id, epicId);
        }
        return stringCSV;
    }

    private static Task fromSCV(String s) {
        if (s.isEmpty() || s.isBlank()) {
            return null;
        }
        String[] parts = s.split(",");
        return switch (parts[0]) {
            case ("TASK") -> new Task(parts[1], parts[2], TaskCondition.valueOf(parts[3]), Long.parseLong(parts[4]));
            case ("EPIC") -> new Epic(parts[1], parts[2], TaskCondition.valueOf(parts[3]), Long.parseLong(parts[4]));
            case ("SUBTASK") ->
                    new Subtask(parts[1], parts[2], TaskCondition.valueOf(parts[3]), Long.parseLong(parts[4]), Long.parseLong(parts[5]));
            default -> null;
        };
    }
}
