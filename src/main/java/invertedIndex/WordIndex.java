package invertedIndex;

import java.util.*;

public class WordIndex {
    private final Map<String, Set<String>> map = new HashMap<>();

    public void add(String word, String docId) {
        map.computeIfAbsent(word, k -> new HashSet<>()).add(docId);
    }

    public void remove(String word, String docId) {
        Set<String> docs = map.get(word);
        if (docs == null) return;

        docs.remove(docId);

        if (docs.isEmpty()) {
            map.remove(word);
        }
    }

    public Set<String> getDocs(String word) {
        return map.getOrDefault(word, Collections.emptySet());
    }
}