package kz.zip.taskmaster.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    @Test
    public void checkNotNull() {
        Assertions.assertNotNull(Manager.getDefault());
    }

}