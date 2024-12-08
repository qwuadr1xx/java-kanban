package kz.zip.taskmaster.service;

import kz.zip.taskmaster.enums.TaskCondition;
import kz.zip.taskmaster.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "Description 1", TaskCondition.NEW, 1, Duration.ofMinutes(30), LocalDateTime.of(2020, 1, 1, 12, 0));
        task2 = new Task("Task 2", "Description 2", TaskCondition.NEW, 2, Duration.ofMinutes(60), LocalDateTime.of(2020, 1, 1, 13, 0));
        task3 = new Task("Task 3", "Description 3", TaskCondition.NEW, 3, Duration.ofMinutes(90), LocalDateTime.of(2020, 1, 1, 14, 0));
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
        historyManager.addToHistoryList(task3);
        historyManager.addToHistoryList(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(2));
        assertEquals(task3, history.get(1));
    }

    @Test
    void checkIfTaskIsRemovedFromHistory() {
        historyManager.addToHistoryList(task1);
        historyManager.addToHistoryList(task2);
        historyManager.addToHistoryList(task3);
        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task3, history.get(1));

        historyManager.addToHistoryList(task1);
        historyManager.addToHistoryList(task2);
        historyManager.addToHistoryList(task3);
        historyManager.remove(task2.getId());

        history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));

        historyManager.addToHistoryList(task1);
        historyManager.addToHistoryList(task2);
        historyManager.addToHistoryList(task3);
        historyManager.remove(task3.getId());

        history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void checkThatNonExistingTaskShouldNotAffectHistory() {
        historyManager.addToHistoryList(task1);
        historyManager.addToHistoryList(task2);
        historyManager.addToHistoryList(task3);
        historyManager.remove(999);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertTrue(history.contains(task1));
        assertTrue(history.contains(task2));
        assertTrue(history.contains(task3));
    }

    @Test
    void checkIfEmptyHistoryListShouldReturnEmpty() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }
}
