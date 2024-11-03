package kz.zip.taskmaster.service;

import kz.zip.taskmaster.model.Epic;
import kz.zip.taskmaster.model.Subtask;
import kz.zip.taskmaster.model.Task;

import java.util.List;

public interface TaskManager{
    List<Task> getListOfTasks();
    List<Epic> getListOfEpics();
    List<Subtask> getListOfSubtasks();

    void clearTasks();
    void clearEpics();
    void clearSubtasks();

    Task getTaskById(long id);
    Epic getEpicById(long id);
    Subtask getSubtaskById(long id);

    void addTask(Task task);
    void addEpic(Epic epic);
    boolean addSubtask(Subtask subtask);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void removeTask(long id);
    void removeEpic(long id);
    void removeSubtask(long id);
}
