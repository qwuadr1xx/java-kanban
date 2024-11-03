package main.java.kz.zip.taskmaster.service;

import java.util.List;

public interface HistoryManager<T> {

    List<T> getHistory();

    void addToHistoryList(T task);
}
