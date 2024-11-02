package kz.zip.taskmaster.service;

import java.util.List;

public interface TaskManager<T, E, S> {
    public List<T> getListOfTasks();
    public List<E> getListOfEpics();
    public List<S> getListOfSubtasks();

    public void clearTasks();
    public void clearEpics();
    public void clearSubtasks();

    public T getTaskById(long id);
    public E getEpicById(long id);
    public S getSubtaskById(long id);

    public void addTask(T task);
    public void addEpic(E epic);
    public boolean addSubtask(S subtask);

    public void updateTask(T task);
    public void updateEpic(E epic);
    public void updateSubtask(S subtask);

    public void removeTask(long id);
    public void removeEpic(long id);
    public void removeSubtask(long id);
}
