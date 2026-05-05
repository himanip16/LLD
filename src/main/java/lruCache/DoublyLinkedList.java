package lruCache;

/**
 * Doubly Linked List specifically optimized for LRU operations.
 * Uses sentinel head and tail nodes to prevent null-pointer checks during edge cases.
 */
class DoublyLinkedList<K, V> {
    private final CacheNode<K, V> head;
    private final CacheNode<K, V> tail;

    public DoublyLinkedList() {
        head = new CacheNode<>(null, null);
        tail = new CacheNode<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    /**
     * Appends a node immediately after the sentinel head (Most Recently Used).
     */
    public void addAtHead(CacheNode<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    /**
     * Unlinks an existing node from the list.
     */
    public void removeNode(CacheNode<K, V> node) {
        CacheNode<K, V> prevNode = node.prev;
        CacheNode<K, V> nextNode = node.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }

    /**
     * Moves an existing node to the most recently used position (Head).
     */
    public void moveToHead(CacheNode<K, V> node) {
        removeNode(node);
        addAtHead(node);
    }

    /**
     * Removes the node right before the sentinel tail (Least Recently Used).
     */
    public CacheNode<K, V> removeTail() {
        if (tail.prev == head) {
            return null; // List is empty
        }
        CacheNode<K, V> lruNode = tail.prev;
        removeNode(lruNode);
        return lruNode;
    }
}
