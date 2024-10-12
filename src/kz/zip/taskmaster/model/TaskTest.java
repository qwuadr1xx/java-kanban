package kz.zip.taskmaster.model;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.service.InMemoryTaskManager;
import kz.zip.taskmaster.service.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class TaskTest {

    InMemoryTaskManager taskManager;
    Task task;

    @BeforeEach
    public void createTasks() {
        taskManager = Manager.getDefault();
    }

    @Test
    public void checkIfReturnedTaskIsSame() {
        Task task = new Task("task1", "desc1");
        taskManager.addTask(task);
        Assertions.assertEquals(taskManager.getTaskById(1), taskManager.getTaskById(1));
    }

    @Test
    public void checkTaskConditionAfterUpdating() {
        task.changeTaskCondition(TaskCondition.DONE);
        Assertions.assertEquals(TaskCondition.DONE, task.getTaskCondition());
    }
}