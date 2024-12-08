package kz.zip.taskmaster.service;

import kz.zip.taskmaster.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    public void setFile() {
        taskManager = new InMemoryTaskManager();
        super.setUp();
    }

    @Test
    public void checkThatIntersectionsAreDetectedTrue() {
        Task task1 = new Task("Task 3", "Description 9",
                Duration.ofMinutes(90), LocalDateTime.of(2020, 1, 1, 11, 0));
        assertTrue(taskManager.addTask(task1));

        Task task2 = new Task("Task 4", "Description 10",
                Duration.ofMinutes(90), LocalDateTime.of(2021, 1, 1, 11, 0));
        assertFalse(taskManager.addTask(task2));
    }

    @Test
    public void checkThatPrioritizedListIsNotEmpty() {
        assertNotNull(taskManager.getPrioritizedTasks());
    }
}