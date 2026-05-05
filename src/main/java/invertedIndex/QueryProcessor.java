package invertedIndex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryProcessor {
    private final InvertedIndex index;

    public QueryProcessor(InvertedIndex index) {
        this.index = index;
    }

    public List<String> search(String term) {
        Set<String> docs = index.getDocsForWord(term.toLowerCase());
        return new ArrayList<>(docs);
    }

    public List<String> andQuery(String term1, String term2) {
        Set<String> set1 = index.getDocsForWord(term1);
        Set<String> set2 = index.getDocsForWord(term2);

        // Intersection Optimization
        if (set1.size() > set2.size()) {
            Set<String> temp = set1;
            set1 = set2;
            set2 = temp;
        }

        Set<String> result = new HashSet<>();
        for (String doc : set1) {
            if (set2.contains(doc)) result.add(doc);
        }
        return new ArrayList<>(result);
    }

    public List<String> orQuery(String term1, String term2) {
        Set<String> result = new HashSet<>(index.getDocsForWord(term1));
        result.addAll(index.getDocsForWord(term2));
        return new ArrayList<>(result);
    }
}