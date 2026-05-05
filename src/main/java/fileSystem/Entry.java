package fileSystem;

abstract class Entry {
    protected String name;
    protected Directory parent;

    public Entry(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    public abstract boolean isDirectory();

    public String getName() {
        return name;
    }

    public String getFullPath() {
        if (parent == null) {
            return "/";
        }
        String parentPath = parent.getFullPath();
        if (parentPath.equals("/")) {
            return "/" + name;
        }
        return parentPath + "/" + name;
    }
}
