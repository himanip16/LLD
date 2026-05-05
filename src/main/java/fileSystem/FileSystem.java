package fileSystem;

import java.util.List;

public class FileSystem {
    private final Directory root;
    private final PathTrie searchIndex;

    public FileSystem() {
        this.root = new Directory("/", null);
        this.searchIndex = new PathTrie();
    }

    public Directory getRoot() {
        return root;
    }

    public FileEntry createFile(String path, String name) {
        Directory dir = traverseToDirectory(path);
        FileEntry newFile = new FileEntry(name, dir);
        dir.addEntry(newFile);

        // Add to search index
        searchIndex.insert(newFile);
        return newFile;
    }

    public Directory createDirectory(String path, String name) {
        Directory dir = traverseToDirectory(path);
        Directory newDir = new Directory(name, dir);
        dir.addEntry(newDir);
        return newDir;
    }

    public List<Entry> findByPattern(String pattern) {
        return searchIndex.search(pattern);
    }

    private Directory traverseToDirectory(String path) {
        if (path.equals("/")) return root;

        String[] segments = path.startsWith("/") ? path.substring(1).split("/") : path.split("/");
        Directory current = root;

        for (String segment : segments) {
            if (segment.isBlank()) continue;
            Entry entry = current.getChildren().get(segment);
            if (entry == null || !entry.isDirectory()) {
                throw new IllegalArgumentException("Invalid directory path: " + path);
            }
            current = (Directory) entry;
        }
        return current;
    }
}
