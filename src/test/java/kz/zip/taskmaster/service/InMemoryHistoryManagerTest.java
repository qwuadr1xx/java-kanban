package kz.zip.taskmaster.service;

import kz.zip.taskmaster.model.Task;
import kz.zip.taskmaster.service.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "Description 1", 1);
        task2 = new Task("Task 2", "Description 2", 2);
    }

    @Test
    void checkIfAddedTaskIsInHistory() {
        historyManager.addToHistoryList(task1);
        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task1, history.getFirst());
    }

    @Test
    void checkIfTaskUpdatePosition() {
        historyManager.addToHistoryList(task1);
        historyManager.addToHistoryList(task2);
        historyManager.addToHistoryList(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));
    }

    @Test
    void checkIfTaskIsRemovedFromHistory() {
        historyManager.addToHistoryList(task1);
        historyManager.addToHistoryList(task2);
        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.getFirst());
    }

    @Test
    void checkThatNonExistingTaskShouldNotAffectHistory() {
        historyManager.addToHistoryList(task1);
        historyManager.addToHistoryList(task2);
        historyManager.remove(999);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertTrue(history.contains(task1));
        assertTrue(history.contains(task2));
    }

    @Test
    void checkIfEmptyHistoryListShouldReturnEmpty() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }
}
