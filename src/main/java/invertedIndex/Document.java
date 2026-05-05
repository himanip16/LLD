package invertedIndex;

import java.util.HashSet;
import java.util.Set;

public class Document {
    private final String id;
    private final String content;

    public Document(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public Set<String> getWords() {
        Set<String> words = new HashSet<>();
        String[] tokens = content.toLowerCase().split("\\W+");

        for (String token : tokens) {
            if (!token.isEmpty()) {
                words.add(token);
            }
        }
        return words;
    }
}