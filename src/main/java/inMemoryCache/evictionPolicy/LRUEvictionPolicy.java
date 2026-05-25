package inMemoryCache.evictionPolicy;

import java.util.HashMap;
import java.util.Map;

public class LRUEvictionPolicy<K> implements EvictionPolicy<K> {

    private static class DoublyLinkedListNode<T> {
        private final T key;
        private DoublyLinkedListNode<T> prev;
        private DoublyLinkedListNode<T> next;

        DoublyLinkedListNode(T key) {
            this.key = key;
        }
    }

    private final Map<K, DoublyLinkedListNode<K>> nodeMap = new HashMap<>();
    private final DoublyLinkedListNode<K> head;
    private final DoublyLinkedListNode<K> tail;

    public LRUEvictionPolicy() {
        this.head = new DoublyLinkedListNode<>(null);
        this.tail = new DoublyLinkedListNode<>(null);
        this.head.next = tail;
        this.tail.prev = head;
    }

    private void removeNode(DoublyLinkedListNode<K> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToHead(DoublyLinkedListNode<K> node) {
        node.next = head.next;
        node.next.prev = node;
        head.next = node;
        node.prev = head;
    }

    @Override
    public void keyAccessed(K key) {
        DoublyLinkedListNode<K> node = nodeMap.get(key);
        if (node != null) {
            removeNode(node);
            moveToHead(node);
        }
    }

    @Override
    public void keyInserted(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        // If the key already exists, treat it as an access/update
        // to avoid duplicate nodes in the linked list.
        if (nodeMap.containsKey(key)) {
            keyAccessed(key);
            return;
        }

        DoublyLinkedListNode<K> newNode = new DoublyLinkedListNode<>(key);
        nodeMap.put(key, newNode);
        moveToHead(newNode);
    }

    @Override
    public void keyRemoved(K key) {
        DoublyLinkedListNode<K> node = nodeMap.remove(key);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public K evictKey() {
        if (head.next == tail) return null;

        DoublyLinkedListNode<K> leastRecentlyUsed = tail.prev;
        removeNode(leastRecentlyUsed);
        nodeMap.remove(leastRecentlyUsed.key);
        return leastRecentlyUsed.key;
    }
}