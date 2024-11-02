package kz.zip.taskmaster.service;

import kz.zip.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager<Task> {
    private final List<Task> histroyList = new ArrayList<>(11);

    @Override
    public List<Task> getHistory() {
        return histroyList;
    }

    @Override
    public void addToHistoryList(Task task) {
        histroyList.addFirst(task);
        histroyList.remove(10);
    }

}
