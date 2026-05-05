package invertedIndex;

import java.util.Set;

public class InvertedIndex {
    private final WordIndex wordIndex = new WordIndex();
    private final DocumentIndex docIndex = new DocumentIndex();

    public void insert(Document doc) {
        String docId = doc.getId();
        Set<String> words = doc.getWords();
        for (String word : words) wordIndex.add(word, docId);
        docIndex.add(docId, words);
    }

    public void delete(String docId) {
        Set<String> words = docIndex.getWords(docId);
        for (String word : words) wordIndex.remove(word, docId);
        docIndex.remove(docId);
    }

    // This is the "Read" API for the Processor
    public Set<String> getDocsForWord(String word) {
        return wordIndex.getDocs(word.toLowerCase());
    }
}