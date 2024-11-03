package kz.zip.taskmaster.service;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.model.Task;
import kz.zip.taskmaster.service.InMemoryTaskManager;
import kz.zip.taskmaster.service.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskTest {

    InMemoryTaskManager taskManager;
    Task task;

    @BeforeEach
    public void createTasks() {
        taskManager = Manager.getDefault();
        task = new Task("task1", "desc1");
        taskManager.addTask(task);
    }

    @Test
    public void checkIfReturnedTaskIsSame() {
        Assertions.assertEquals("task1", taskManager.getTaskById(1).getName());
        Assertions.assertEquals("desc1", taskManager.getTaskById(1).getDescription());
    }

    @Test
    public void checkTaskConditionAfterUpdating() {
        task.changeTaskCondition(TaskCondition.DONE);
        Assertions.assertEquals(TaskCondition.DONE, task.getTaskCondition());
    }
}