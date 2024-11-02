package main.java.kz.zip.taskmaster.service;

public class Node<E> {

    Node<E> prev;
    Node<E> next;
    E element;

    Node(E element) {
        this.element = element;
    }

}
