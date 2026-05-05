package fileSystem;

import fileSystem.Entry;

import java.util.*;


public class TrieNode {
    // Each level maps a path component (e.g., "user", "bin", "test.txt") to its child node
    private final Map<String, TrieNode> children = new HashMap<>();
    private Entry fileSystemEntry; // References the actual file if this is a terminal leaf
    private boolean isEndOfPath = false;

    public Map<String, TrieNode> getChildren() { return children; }
    public boolean isEndOfPath() { return isEndOfPath; }
    public void setEndOfPath(boolean endOfPath) { isEndOfPath = endOfPath; }
    public Entry getFileSystemEntry() { return fileSystemEntry; }
    public void setFileSystemEntry(Entry entry) { this.fileSystemEntry = entry; }
}
