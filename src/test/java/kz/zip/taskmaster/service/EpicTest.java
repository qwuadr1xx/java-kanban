package test.java.kz.zip.taskmaster.service;

import main.java.kz.zip.taskmaster.model.Epic;
import main.java.kz.zip.taskmaster.model.Subtask;
import main.java.kz.zip.taskmaster.service.InMemoryTaskManager;
import main.java.kz.zip.taskmaster.service.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {

    InMemoryTaskManager taskManager;
    Epic epic;

    @BeforeEach
    public void createEpics() {
        taskManager = Manager.getDefault();
        epic = new Epic("epic1", "desc1");
        taskManager.addEpic(epic);
    }

    @Test
    public void checkIfReturnedEpicIsSame() {
        Assertions.assertEquals("epic1", taskManager.getEpicById(1).getName());
        Assertions.assertEquals("desc1", taskManager.getEpicById(1).getDescription());
    }

    @Test
    public void checkSubtaskIdListAfterAddingSubtask() {
        Subtask subtask = new Subtask("subtask1", "desc2", epic.getId());
        taskManager.addSubtask(subtask);
        Assertions.assertNotNull(epic.getIdList());
    }


}