package main.java.kz.zip.taskmaster.service;

import main.java.kz.zip.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager<Task> {
    private final List<Task> histroyList = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return histroyList;
    }

    @Override
    public void addToHistoryList(Task task) {
        if (histroyList.size() == 10) {
            histroyList.remove(9);
        }
        histroyList.addFirst(task);
    }

}
