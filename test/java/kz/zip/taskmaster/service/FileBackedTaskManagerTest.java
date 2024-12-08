package kz.zip.taskmaster.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import kz.zip.taskmaster.exception.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    File file;

    @BeforeEach
    public void setFile() {
        {
            try {
                file = File.createTempFile("test", ".csv");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        taskManager = new FileBackedTaskManager(file);
        super.setUp();
    }

    @Test
    void checkIfNullSaveFileIsNull() {
        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubtasks();
        assertTrue(taskManager.getListOfTasks().isEmpty());
        assertTrue(taskManager.getListOfEpics().isEmpty());
        assertTrue(taskManager.getListOfSubtasks().isEmpty());

        try {
            taskManager.save();
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
        assertEquals(2, taskManager.getListOfTasks().size());

        FileBackedTaskManager loadedManager;

        try {
            loadedManager = FileBackedTaskManager.loadFromFile(file);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        assertEquals(2, loadedManager.getListOfTasks().size());
        assertEquals(taskManager.getTaskById(1), loadedManager.getTaskById(1));
        assertEquals(taskManager.getTaskById(2), loadedManager.getTaskById(2));
    }

    @Test
    void checkIfSavesEpicsAndSubtasksAndLoadThem() {
        assertEquals(2, taskManager.getListOfEpics().size());
        assertEquals(4, taskManager.getListOfSubtasks().size());

        FileBackedTaskManager loadedManager;
        try {
            loadedManager = FileBackedTaskManager.loadFromFile(file);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        assertEquals(2, loadedManager.getListOfEpics().size());
        assertEquals(4, loadedManager.getListOfSubtasks().size());
    }

    @Test
    void checkIsConnectionBetweenEpicsAndSubtasksStay() {
        assertEquals(taskManager.getEpicById(3), taskManager.getEpicById(taskManager.getSubtaskById(5).getEpicId()));
        assertEquals(taskManager.getEpicById(3), taskManager.getEpicById(taskManager.getSubtaskById(6).getEpicId()));
        assertEquals(taskManager.getEpicById(4), taskManager.getEpicById(taskManager.getSubtaskById(7).getEpicId()));
        assertEquals(taskManager.getEpicById(4), taskManager.getEpicById(taskManager.getSubtaskById(8).getEpicId()));

        FileBackedTaskManager loadedManager;
        try {
            loadedManager = FileBackedTaskManager.loadFromFile(file);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        assertEquals(loadedManager.getEpicById(3), loadedManager.getEpicById(loadedManager.getSubtaskById(5).getEpicId()));
        assertEquals(loadedManager.getEpicById(3), loadedManager.getEpicById(loadedManager.getSubtaskById(6).getEpicId()));
        assertEquals(loadedManager.getEpicById(4), loadedManager.getEpicById(loadedManager.getSubtaskById(7).getEpicId()));
        assertEquals(loadedManager.getEpicById(4), loadedManager.getEpicById(loadedManager.getSubtaskById(8).getEpicId()));
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

