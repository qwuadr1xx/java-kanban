package kz.zip.taskmaster.service;

import kz.zip.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager<Task> {
    private final Map<Long, Node<Task>> nodeIdentificators;

    Node<Task> tail;
    Node<Task> head;

    public InMemoryHistoryManager() {
        nodeIdentificators = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        List<Task> listOfTasks = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            listOfTasks.add(current.element);
            current = current.next;
        }
        return listOfTasks;
    }

    @Override
    public void addToHistoryList(Task task) {
        if (task == null) {
            return;
        }

        if (nodeIdentificators.containsKey(task.getId())) {
            remove(task.getId());
        }

        linkLast(task);
    }

    @Override
    public void remove(long id) {
        if (!nodeIdentificators.containsKey(id)) {
            return;
        }

        Node<Task> node = nodeIdentificators.get(id);

        if (node == null) {
            return;
        }

        Node<Task> prevNode = node.prev;
        Node<Task> nextNode = node.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        } else {
            head = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            tail = prevNode;
        }

        nodeIdentificators.remove(id);
        node.prev = null;
        node.next = null;
    }

    void linkLast(Task task) {
        Node<Task> node = new Node<>(task);

        if (tail == null) {
            tail = node;
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }

        nodeIdentificators.put(task.getId(), node);
    }
}

