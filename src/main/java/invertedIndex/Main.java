package invertedIndex;

public class Main {
    public static void main(String[] args) {

        InvertedIndex index = new InvertedIndex();

        Document doc1 = new Document("doc1", "hello world hello");
        Document doc2 = new Document("doc2", "hello java programming");
        Document doc3 = new Document("doc3", "world of java");

        index.insert(doc1);
        index.insert(doc2);
        index.insert(doc3);

        QueryProcessor processor = new QueryProcessor(index);
        System.out.println("Search 'hello': " + processor.search("hello"));
        System.out.println("Search 'world': " + processor.search("world"));
        System.out.println("Search 'java': " + processor.search("java"));

        System.out.println("AND search 'hello' & 'world': " +
                processor.andQuery("hello", "world"));

        index.delete("doc1");

        System.out.println("After deleting doc1:");

        System.out.println("Search 'hello': " + processor.search("hello"));
        System.out.println("Search 'world': " + processor.search("world"));
        System.out.println("AND search 'hello' & 'world': " +
                processor.andQuery("hello", "world"));
    }
}