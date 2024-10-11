package kz.zip.taskmaster.service;

public class Manager {
    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
