package fileSystem;

import java.util.HashMap;
import java.util.Map;

class Directory extends Entry {
    private final Map<String, Entry> children;

    public Directory(String name, Directory parent) {
        super(name, parent);
        this.children = new HashMap<>();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    public Map<String, Entry> getChildren() {
        return children;
    }

    public void addEntry(Entry entry) {
        if (children.containsKey(entry.getName())) {
            throw new IllegalArgumentException("Entry already exists: " + entry.getName());
        }
        children.put(entry.getName(), entry);
    }
}
