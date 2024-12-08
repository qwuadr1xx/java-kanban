 package kz.zip.taskmaster.service;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.model.*;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;

    public void setUp() {
        Task task1 = new Task("Task 1", "Description 1",
                Duration.ofMinutes(30), LocalDateTime.of(2020, 1, 1, 12, 0));
        Task task2 = new Task("Task 2", "Description 2",
                Duration.ofMinutes(60), LocalDateTime.of(2020, 1, 1, 13, 0));
        Epic epic1 = new Epic("Epic 1", "Description 3");
        Epic epic2 = new Epic("Epic 2", "Description 4");
        Subtask subtask1 = new Subtask("Subtask 1", "Description 5", 3,
                Duration.ofMinutes(30), LocalDateTime.of(2020, 1, 1, 12, 0));
        Subtask subtask2 = new Subtask("Subtask 2", "Description 6", 3,
                Duration.ofMinutes(60), LocalDateTime.of(2020, 1, 1, 13, 0));
        Subtask subtask3 = new Subtask("Subtask 3", "Description 7", 4,
                Duration.ofMinutes(30), LocalDateTime.of(2020, 1, 1, 12, 0));
        Subtask subtask4 = new Subtask("Subtask 4", "Description 8", 4,
                Duration.ofMinutes(60), LocalDateTime.of(2020, 1, 1, 13, 0));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask4);
    }

    @Test
    public void checkIfAllTasksAdded() {
        assertEquals(2, taskManager.getListOfTasks().size());
        assertEquals(2, taskManager.getListOfEpics().size());
        assertEquals(4, taskManager.getListOfSubtasks().size());
    }

    @Test
    public void checkIfTaskListsAreClear() {
        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubtasks();

        assertEquals(0, taskManager.getListOfTasks().size());
        assertEquals(0, taskManager.getListOfEpics().size());
        assertEquals(0, taskManager.getListOfSubtasks().size());
    }

    @Test
    public void checkIfUpdatedTaskIsNew() {
        Task newTask = new Task("NewTask 1", "NewDescription 1", TaskCondition.NEW, 1,
                Duration.ofMinutes(30), LocalDateTime.of(2020, 1, 1, 12, 0));
        taskManager.updateTask(newTask);

        assertEquals(newTask, taskManager.getTaskById(1));

        Epic newEpic = new Epic("NewTask 1", "NewDescription 1", 3, new ArrayList<>(taskManager.getEpicById(3).getSubtaskList()));
        taskManager.updateEpic(newEpic);

        assertEquals(newEpic, taskManager.getEpicById(3));

        Subtask newSubtask = new Subtask("NewSubtask1", "NewDescription 1", TaskCondition.NEW,
                5, 3, Duration.ofMinutes(30), LocalDateTime.of(2020, 1, 1, 12, 0));
        taskManager.updateSubtask(newSubtask);

        assertEquals(newSubtask, taskManager.getSubtaskById(5));
    }

    @Test
    public void checkIfRemovedTaskIsRemoved() {
        taskManager.removeTask(1);

        assertEquals(1, taskManager.getListOfTasks().size());

        taskManager.removeEpic(3);

        assertEquals(1, taskManager.getListOfEpics().size());
        assertEquals(2, taskManager.getListOfSubtasks().size());

        taskManager.removeSubtask(7);

        assertEquals(1, taskManager.getListOfSubtasks().size());
    }
}
