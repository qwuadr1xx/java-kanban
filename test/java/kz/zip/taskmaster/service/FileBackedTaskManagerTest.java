package kz.zip.taskmaster.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import kz.zip.taskmaster.exception.ManagerSaveException;
import kz.zip.taskmaster.model.Epic;
import kz.zip.taskmaster.model.Subtask;
import kz.zip.taskmaster.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    File file;
    FileBackedTaskManager manager;

    @BeforeEach
    void setFile() {
        {
            try {
                file = File.createTempFile("test", ".csv");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        manager = new FileBackedTaskManager(file);
    }

    @Test
    void checkIfNullSaveFileIsNull() {
        assertTrue(manager.getListOfTasks().isEmpty());
        assertTrue(manager.getListOfEpics().isEmpty());
        assertTrue(manager.getListOfSubtasks().isEmpty());

        try {
            manager.save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        FileBackedTaskManager loadedManager;
        try {
            loadedManager = FileBackedTaskManager.loadFromFile(file);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        assertTrue(loadedManager.getListOfTasks().isEmpty());
        assertTrue(loadedManager.getListOfEpics().isEmpty());
        assertTrue(loadedManager.getListOfSubtasks().isEmpty());
    }

    @Test
    void checkIfSavesTasksAndLoadThem() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.addTask(task1);
        manager.addTask(task2);

        assertEquals(2, manager.getListOfTasks().size());

        FileBackedTaskManager loadedManager;

        try {
            loadedManager = FileBackedTaskManager.loadFromFile(file);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        assertEquals(2, loadedManager.getListOfTasks().size());
        assertEquals(manager.getTaskById(1), loadedManager.getTaskById(1));
        assertEquals(manager.getTaskById(2), loadedManager.getTaskById(2));
    }

    @Test
    void checkIfSavesEpicsAndSubtasksAndLoadThem() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description 1", 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description 2", 1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(1, manager.getListOfEpics().size());
        assertEquals(2, manager.getListOfSubtasks().size());

        FileBackedTaskManager loadedManager;
        try {
            loadedManager = FileBackedTaskManager.loadFromFile(file);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        assertEquals(1, loadedManager.getListOfEpics().size());
        assertEquals(2, loadedManager.getListOfSubtasks().size());

        assertEquals(manager.getEpicById(1), loadedManager.getEpicById(1));
        assertEquals(manager.getSubtaskById(2), loadedManager.getSubtaskById(2));
        assertEquals(manager.getSubtaskById(3), loadedManager.getSubtaskById(3));
    }

    @Test
    void checkIsConnectionBetweenEpicsAndSubtasksStay() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description 1", 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description 2", 1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(manager.getEpicById(1), manager.getEpicById(subtask1.getEpicId()));
        assertEquals(manager.getEpicById(1), manager.getEpicById(subtask2.getEpicId()));

        FileBackedTaskManager loadedManager;
        try {
            loadedManager = FileBackedTaskManager.loadFromFile(file);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        assertEquals(loadedManager.getEpicById(1), loadedManager.getEpicById(subtask1.getEpicId()));
        assertEquals(loadedManager.getEpicById(1), loadedManager.getEpicById(subtask2.getEpicId()));
        assertEquals(manager.getEpicById(1).getIdList(), loadedManager.getEpicById(1).getIdList());
    }

    @Test
    void checkIfIncorrectStringResultIsNull() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("");
            writer.newLine();
            writer.write("1 Sentence");
            writer.newLine();
            writer.write("1 Sentence, 2 Sentence");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        FileBackedTaskManager loadedManager;
        try {
            loadedManager = FileBackedTaskManager.loadFromFile(file);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        assertTrue(loadedManager.getListOfTasks().isEmpty());
        assertTrue(loadedManager.getListOfEpics().isEmpty());
        assertTrue(loadedManager.getListOfSubtasks().isEmpty());
    }
}

