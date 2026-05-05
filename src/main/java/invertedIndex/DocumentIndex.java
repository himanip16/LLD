package invertedIndex;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocumentIndex {
    private final Map<String, Set<String>> map = new HashMap<>();

    public void add(String docId, Set<String> words) {
        map.put(docId, words);
    }

    public Set<String> getWords(String docId) {
        return map.getOrDefault(docId, Collections.emptySet());
    }

    public void remove(String docId) {
        map.remove(docId);
    }
}
