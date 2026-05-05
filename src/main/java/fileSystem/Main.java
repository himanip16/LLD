package fileSystem;


import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileSystem fs = new FileSystem();

        // 1. Build directory structure
        fs.createDirectory("/", "user");
        fs.createDirectory("/user", "alice");
        fs.createDirectory("/user", "bob");

        // 2. Add files in different subdirectories
        fs.createFile("/user/alice", "resume.pdf");
        fs.createFile("/user/alice", "notes.txt");
        fs.createFile("/user/bob", "todo.txt");
        fs.createFile("/user/bob", "script.sh");

        System.out.println("File System successfully populated.");

        // 3. Test Search with Exact Match
        System.out.println("\n--- 1. Exact Match Search ---");
        List<Entry> exactMatch = fs.findByPattern("/user/alice/resume.pdf");
        exactMatch.forEach(e -> System.out.println("Found exact: " + e.getFullPath()));

        // 4. Test Search with Wildcards
        System.out.println("\n--- 2. Wildcard Search: '/user/*/todo.txt' ---");
        List<Entry> wildcardMatch1 = fs.findByPattern("/user/*/todo.txt");
        wildcardMatch1.forEach(e -> System.out.println("Match: " + e.getFullPath()));

        System.out.println("\n--- 3. Wildcard Search: '/user/alice/*' ---");
        List<Entry> wildcardMatch2 = fs.findByPattern("/user/alice/*");
        wildcardMatch2.forEach(e -> System.out.println("Match: " + e.getFullPath()));
    }
}