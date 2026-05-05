package fileSystem;

class FileEntry extends Entry {
    private String content;

    public FileEntry(String name, Directory parent) {
        super(name, parent);
        this.content = "";
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
