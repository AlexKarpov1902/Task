package karpov.example;


import java.util.Arrays;
import java.util.HashMap;

/**
 * freq - массив с частотами запросов страниц
 * При удалении из кэша выбирается страница с минимальным количеством запросов,
 * которая выбирается путем перебора элементов списка, начиная с головы, первая найденная.
 *
 * @param <K>
 * @param <V>
 */

public class LfuCache<K, V> implements ICache<K, V> {

    private HashMap<K, Node<K, V>> map;
    private int capacity;
    private Node<K, V> head, tail;
    private int[] freq;

    public LfuCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
        freq = new int[capacity];
    }

    @Override
    public void putValue(K key, V value) {
        if (map.containsKey(key)) {
            Node<K, V> node = map.get(key);
            node.value = value;
            removeNode(node);
            nodeToTail(node);
            freqRerange(node);
        } else {
            if (isFull()) {
                deleteNext();
            }
            Node<K, V> node = new Node<>(key, value);
            freq[node.count]++;
            nodeToTail(node);
            map.put(key, node);
        }

    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = map.get(key);
        removeNode(node);
        nodeToTail(node);
        freqRerange(node);
        return node != null ? node.value : null;
    }

    /**
     * метод удаления редко запрашиваемого нода
     */
    private void deleteNext() {
        Node<K, V> node = head;
        int min = getMinFreq();
        while (node.count != min) {
            node = node.next;
        }
        freq[node.count]--;
        map.remove(node.key);
        removeNode(node);
    }

    private boolean isFull() {
        return map.size() >= capacity;
    }

    /**
     * Удаление node из списка
     *
     * @param node
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
     *
     * @param node
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

    /**
     * Модификация массива частот обращения к страницам
     *
     * @param node
     */
    private void freqRerange(Node<K, V> node) {
        if (node == null)
            return;
        freq[node.count]--;
        if (node.count < capacity - 1) {
            node.count++;
        }
        freq[node.count]++;
    }

    /**
     * Возвращает количество запросов самой редко запрашиваемой страницы
     *
     * @return
     */
    private int getMinFreq() {
        int n = 0;
        while (freq[n] == 0) {
            n++;
        }
        return n;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private int count;
        private Node<K, V> next;
        private Node<K, V> prev;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.count = 0;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    @Override
    public void printCache() {
        Node<K, V> tmp = head;
        while (tmp != null) {
            System.out.print(tmp.count + " " + tmp.value + ", ");
            tmp = tmp.next;
        }
        System.out.print(" массив частот: ");

        Arrays.stream(freq).forEach(n -> System.out.print(n + " "));
        System.out.println();
    }

}
