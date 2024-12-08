package kz.zip.taskmaster.service;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.model.Epic;
import kz.zip.taskmaster.model.Subtask;
import kz.zip.taskmaster.utils.DateTimeUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {

    InMemoryTaskManager taskManager;
    Epic epic;

    @BeforeEach
    public void createEpics() {
        taskManager = Manager.getDefault();
        epic = new Epic("epic1", "desc1");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "desc2", 1,
                Duration.ofMinutes(30), LocalDateTime.of(2020, 1, 1, 12, 0));
        taskManager.addSubtask(subtask);
    }

    @Test
    public void checkIfReturnedEpicIsSame() {
        Assertions.assertEquals("epic1", taskManager.getEpicById(1).getName());
        Assertions.assertEquals("desc1", taskManager.getEpicById(1).getDescription());
        Assertions.assertEquals(30, taskManager.getEpicById(1).getDuration().toMinutes());
        Assertions.assertEquals("12:00 01.01.2020", taskManager.getEpicById(1).getStartTime().format(DateTimeUtils.formatterDT));
        Assertions.assertEquals("12:30 01.01.2020", taskManager.getEpicById(1).getEndTime().format(DateTimeUtils.formatterDT));
    }

    @Test
    public void checkSubtaskIdListAfterAddingSubtask() {
        Assertions.assertNotNull(epic.getIdList());
    }

    @Test
    public void checkTaskConditions() {
        Subtask subtask1 = new Subtask("subtask2", "desc3", 1,
                Duration.ofMinutes(60), LocalDateTime.of(2020, 1, 1, 13, 0));
        taskManager.addSubtask(subtask1);
        Assertions.assertEquals("NEW", taskManager.getEpicById(1).getTaskCondition().toString());

        Subtask subtask = taskManager.getSubtaskById(2);
        subtask1 = taskManager.getSubtaskById(3);

        subtask.setTaskCondition(TaskCondition.DONE);
        taskManager.updateSubtask(subtask);

        Assertions.assertEquals("NEW", taskManager.getEpicById(1).getTaskCondition().toString());

        subtask1.setTaskCondition(TaskCondition.DONE);
        taskManager.updateSubtask(subtask1);

        Assertions.assertEquals("DONE", taskManager.getEpicById(1).getTaskCondition().toString());

        subtask.setTaskCondition(TaskCondition.IN_PROGRESS);
        subtask1.setTaskCondition(TaskCondition.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        taskManager.updateSubtask(subtask1);

        Assertions.assertEquals("IN_PROGRESS", taskManager.getEpicById(1).getTaskCondition().toString());
    }
}