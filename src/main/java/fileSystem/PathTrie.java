package fileSystem;

import java.util.ArrayList;
import java.util.List;

class PathTrie {
    private final TrieNode root = new TrieNode();

    /**
     * Inserts an Entry's full path into the Trie.
     */
    public void insert(Entry entry) {
        String[] segments = splitPath(entry.getFullPath());
        TrieNode current = root;
        for (String segment : segments) {
            current.getChildren().computeIfAbsent(segment, k -> new TrieNode());
            current = current.getChildren().get(segment);
        }
        current.setEndOfPath(true);
        current.setFileSystemEntry(entry);
    }

    /**
     * Searches for paths matching a pattern that may contain wildcards ('*').
     */
    public List<Entry> search(String pattern) {
        List<Entry> results = new ArrayList<>();
        String[] segments = splitPath(pattern);
        searchRecursive(root, segments, 0, results);
        return results;
    }

    private void searchRecursive(TrieNode current, String[] segments, int index, List<Entry> results) {
        if (index == segments.length) {
            if (current.isEndOfPath() && current.getFileSystemEntry() != null) {
                results.add(current.getFileSystemEntry());
            }
            return;
        }

        String segment = segments[index];

        if (segment.equals("*")) {
            // Wildcard matches all child branches at this specific depth
            for (TrieNode child : current.getChildren().values()) {
                searchRecursive(child, segments, index + 1, results);
            }
        } else {
            // Standard exact match
            if (current.getChildren().containsKey(segment)) {
                searchRecursive(current.getChildren().get(segment), segments, index + 1, results);
            }
        }
    }

    private String[] splitPath(String path) {
        if (path == null || path.equals("/") || path.isBlank()) {
            return new String[0];
        }
        // Remove leading slash and split
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path.split("/");
    }
}
