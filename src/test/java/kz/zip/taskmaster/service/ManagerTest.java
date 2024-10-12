package test.java.kz.zip.taskmaster.service;

import main.java.kz.zip.taskmaster.model.Task;
import main.java.kz.zip.taskmaster.service.InMemoryHistoryManager;
import main.java.kz.zip.taskmaster.service.InMemoryTaskManager;
import main.java.kz.zip.taskmaster.service.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagerTest {

    @Test
    public void checkNotNull() {
        Assertions.assertNotNull(Manager.getDefault());
    }

    @Test
    public void checkThaTaskCanBeAdd() {
        Task task = new Task("task1", "desc1");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.getTaskById(task.getId());
        Assertions.assertNotNull(inMemoryHistoryManager.getHistory());
    }
}