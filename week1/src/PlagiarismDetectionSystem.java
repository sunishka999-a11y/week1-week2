import java.util.*;

public class PlagiarismDetectionSystem {

    // HashMap to store n-gram -> documents
    static HashMap<String, Set<String>> ngramIndex = new HashMap<>();

    static int N = 5; // size of n-gram

    // Function to create n-grams
    public static List<String> generateNgrams(String text) {
        List<String> ngrams = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }
            ngrams.add(sb.toString().trim());
        }

        return ngrams;
    }

    // Add document to database
    public static void addDocument(String docId, String text) {

        List<String> ngrams = generateNgrams(text);

        for (String ngram : ngrams) {
            ngramIndex.putIfAbsent(ngram, new HashSet<>());
            ngramIndex.get(ngram).add(docId);
        }
    }

    // Analyze new document
    public static void analyzeDocument(String docId, String text) {

        List<String> ngrams = generateNgrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String ngram : ngrams) {
            if (ngramIndex.containsKey(ngram)) {

                for (String doc : ngramIndex.get(ngram)) {
                    matchCount.put(doc, matchCount.getOrDefault(doc, 0) + 1);
                }

            }
        }

        System.out.println("Total n-grams: " + ngrams.size());

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Matched with: " + doc);
            System.out.println("Matching n-grams: " + matches);
            System.out.println("Similarity: " + String.format("%.2f", similarity) + "%");

            if (similarity > 60) {
                System.out.println("⚠ PLAGIARISM DETECTED");
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {

        // Sample database essays
        String essay1 = "Artificial intelligence is transforming education systems and improving learning experiences";
        String essay2 = "Machine learning and artificial intelligence are changing modern technology and education";

        addDocument("essay_089.txt", essay1);
        addDocument("essay_092.txt", essay2);

        // New essay to check
        String newEssay = "Artificial intelligence is transforming education systems and improving learning";

        analyzeDocument("essay_123.txt", newEssay);
    }
}