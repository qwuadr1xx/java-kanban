package kz.zip.taskmaster.model;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.service.InMemoryTaskManager;
import kz.zip.taskmaster.service.Manager;
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
        Assertions.assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    public void checkEpicConditionAfterUpdating() {
        Subtask subtask = new Subtask("subtask1", "desc2", 1);
        subtask.changeTaskCondition(TaskCondition.DONE);
        Assertions.assertEquals(TaskCondition.DONE, epic.getTaskCondition());
    }

    @Test
    public void checkSubtaskIdListAfterAddingSubtask() {
        Subtask subtask = new Subtask("subtask1", "desc2", 1);
        Assertions.assertEquals(subtask.getId(), epic.getIdList().getFirst());
        taskManager.removeSubtask(subtask.getId());
        Assertions.assertNull(epic.getIdList());
    }


}