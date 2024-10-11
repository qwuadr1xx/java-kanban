package kz.zip.taskmaster.model;

import kz.zip.taskmaster.service.InMemoryTaskManager;
import kz.zip.taskmaster.service.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SubtaskTest {

    InMemoryTaskManager taskManager;

    @BeforeEach
    public void createSubtasks() {
        taskManager = Manager.getDefault();
        Epic epic = new Epic("epic1", "desc1");
        taskManager.addEpic(epic);
    }

    @Test
    public void checkIfSameTaskId() {
        Subtask subtask = new Subtask("subtask1", "desc1", 1);
        taskManager.addTask(subtask);
        Assertions.assertEquals(taskManager.getSubtaskById(2), taskManager.getSubtaskById(2));
    }

    @Test
    public void checkIfReturnedSubtaskIsSame() {
        Subtask subtask = new Subtask("subtask1", "desc1", 1);
        taskManager.addTask(subtask);
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(2));
    }
}