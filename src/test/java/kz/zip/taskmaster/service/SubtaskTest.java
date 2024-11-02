package test.java.kz.zip.taskmaster.service;

import main.java.kz.zip.taskmaster.enums.TaskCondition;
import main.java.kz.zip.taskmaster.model.Epic;
import main.java.kz.zip.taskmaster.model.Subtask;
import main.java.kz.zip.taskmaster.service.InMemoryTaskManager;
import main.java.kz.zip.taskmaster.service.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    InMemoryTaskManager taskManager;
    Subtask subtask;

    @BeforeEach
    public void createSubtasks() {
        taskManager = Manager.getDefault();
        Epic epic = new Epic("epic1", "desc1");
        taskManager.addEpic(epic);
        subtask = new Subtask("subtask1", "desc1", 1);
        taskManager.addSubtask(subtask);
    }

    @Test
    public void checkIfReturnedSubtaskIsSame() {
        Assertions.assertEquals("subtask1", taskManager.getSubtaskById(2).getName());
        Assertions.assertEquals("desc1", taskManager.getSubtaskById(2).getDescription());
    }

    @Test
    public void checkSubtaskConditionAfterUpdating() {
        subtask.changeTaskCondition(TaskCondition.DONE);
        Assertions.assertEquals(TaskCondition.DONE, subtask.getTaskCondition());
    }
}