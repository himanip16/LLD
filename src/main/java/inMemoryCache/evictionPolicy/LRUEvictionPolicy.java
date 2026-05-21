package inMemoryCache.evictionPolicy;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU (Least Recently Used) Implementation using an internal Doubly Linked List.
 * This class is NOT internally thread-safe; thread safety is managed by the parent Cache container.
 */
class LRUEvictionPolicy<K> implements EvictionPolicy<K> {

    private static class DoublyLinkedListNode<T> {
        T key;
        DoublyLinkedListNode<T> prev;
        DoublyLinkedListNode<T> next;

        DoublyLinkedListNode(T key) {
            this.key = key;
        }
    }

    private final Map<K, DoublyLinkedListNode<K>> nodeMap = new HashMap<>();
    private final DoublyLinkedListNode<K> head;
    private final DoublyLinkedListNode<K> tail;

    public LRUEvictionPolicy() {
        head = new DoublyLinkedListNode<>(null);
        tail = new DoublyLinkedListNode<>(null);
        head.next = tail;
        tail.prev = head;
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
        if (tail.prev == head) return null; // Cache is empty
        DoublyLinkedListNode<K> leastRecentlyUsed = tail.prev;
        removeNode(leastRecentlyUsed);
        nodeMap.remove(leastRecentlyUsed.key);
        return leastRecentlyUsed.key;
    }
}
