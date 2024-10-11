package kz.zip.taskmaster.model;

import kz.zip.taskmaster.service.InMemoryTaskManager;
import kz.zip.taskmaster.service.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class TaskTest {

    InMemoryTaskManager taskManager;

    @BeforeEach
    public void createTasks() {
        taskManager = Manager.getDefault();
    }


    @Test
    public void checkIfSameTaskId() {
        Task task = new Task("task1", "desc1");
        taskManager.addTask(task);
        Assertions.assertEquals(taskManager.getTaskById(1), taskManager.getTaskById(1));
    }

    @Test
    public void checkIfReturnedTaskIsSame() {
        Task task = new Task("task1", "desc1");
        taskManager.addTask(task);
        Assertions.assertEquals(taskManager.getTaskById(1), taskManager.getTaskById(1));
    }
}