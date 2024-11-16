package kz.zip.taskmaster.service;

import kz.zip.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager<Task> {

    private final Map<Long, Node<Task>> nodeIdentifiers;
    private Node<Task> tail;
    private Node<Task> head;

    public InMemoryHistoryManager() {
        nodeIdentifiers = new HashMap<>();
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
        if (nodeIdentifiers.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(long id) {
        Node<Task> node = nodeIdentifiers.get(id);
        if (node == null) {
            return;
        }
        removeNode(node);
    }

    private void linkLast(Task task) {
        Node<Task> node = new Node<>(task);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        nodeIdentifiers.put(task.getId(), node);
    }

    private void removeNode(Node<Task> node) {
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
        nodeIdentifiers.remove(node.element.getId());
        node.prev = null;
        node.next = null;
    }

    static class Node<E> {

        Node<E> prev;
        Node<E> next;
        E element;

        Node(E element) {
            this.element = element;
        }

    }

}

