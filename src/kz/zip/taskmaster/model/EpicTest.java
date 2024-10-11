package kz.zip.taskmaster.model;

import kz.zip.taskmaster.service.InMemoryTaskManager;
import kz.zip.taskmaster.service.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager taskManager;

    @BeforeEach
    public void createEpics() {
        taskManager = Manager.getDefault();
    }

    @Test
    public void checkIfSameEpicId() {
        Epic epic = new Epic("epic1", "desc1");
        taskManager.addEpic(epic);
        Assertions.assertEquals(taskManager.getEpicById(1), taskManager.getEpicById(1));
    }

    @Test
    public void checkIfReturnedEpicIsSame() {
        Epic epic = new Epic("epic1", "desc1");
        taskManager.addEpic(epic);
        Assertions.assertEquals(epic, taskManager.getEpicById(1));
    }
}