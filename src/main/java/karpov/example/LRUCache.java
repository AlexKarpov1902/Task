package karpov.example;

import java.util.HashMap;

/**
 * Реализация LRU Cache
 * Создается двусвязный список. Каждая добавляемая и запрашиваемая страница перемещается в конец списка.
 * При заполнении кэша удаляется страница из головы списка.
 *
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> implements ICache<K, V>{

    private final HashMap<K, Node<K, V>> map;
    private final int capacity;
    private Node<K, V> head, tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
    }

    @Override
    public void putValue(K key, V value) {
        if (map.containsKey(key)) {
            Node<K, V> node = map.get(key);
            node.value = value;
            removeNode(node);
            nodeToTail(node);
        } else {
            if (isFull()) {
   //             System.out.println("Заполнен");
                map.remove(head.key);
                removeNode(head);
            }
            Node<K, V> node = new Node<>(key, value);
            nodeToTail(node);
            map.put(key, node);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = map.get(key);
        removeNode(node);
        nodeToTail(node);
        return node != null ? node.value : null;
    }

    private boolean isFull() {
        return map.size() >= capacity;
    }


    /**
     * Удаление node из списка
     * @param node нода
     */
    private void removeNode(Node<K, V> node) {
        if (node == null)
            return;
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    /**
     * Перенесение node в конец списка
     * @param node нода
     */
    private void nodeToTail(Node<K, V> node) {
        if (node == null)
            return;
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            node.next = null;
            tail = node;
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;
        private Node<K, V> prev;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
        @Override
        public String toString() {
            return value.toString();
        }
    }

    /**
     * Печать содержимого кэша
     */
    @Override
    public void printCache() {
        Node<K, V> temp = head;
        while (temp != null) {
            System.out.print(temp.value + ", ");
            temp = temp.next;
        }
        System.out.println();
    }

}
