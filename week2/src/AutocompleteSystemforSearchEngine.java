import java.util.*;

public class AutocompleteSystemforSearchEngine {

    // Trie Node
    static class TrieNode {
        HashMap<Character, TrieNode> children = new HashMap<>();
        List<String> queries = new ArrayList<>();
    }

    static TrieNode root = new TrieNode();

    // query -> frequency
    static HashMap<String, Integer> frequencyMap = new HashMap<>();

    // Insert query into Trie
    public static void insertQuery(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            if (!node.queries.contains(query)) {
                node.queries.add(query);
            }
        }
    }

    // Update frequency
    public static void updateFrequency(String query) {

        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);

        if (frequencyMap.get(query) == 1) {
            insertQuery(query);
        }
    }

    // Search suggestions
    public static void search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                System.out.println("No suggestions found.");
                return;
            }
            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>(node.queries);

        // Sort by frequency
        results.sort((a, b) -> frequencyMap.get(b) - frequencyMap.get(a));

        System.out.println("Suggestions for \"" + prefix + "\" :");

        int count = 0;
        for (String q : results) {

            System.out.println((count + 1) + ". " + q +
                    " (" + frequencyMap.get(q) + " searches)");

            count++;
            if (count == 10) break;
        }
    }

    public static void main(String[] args) {

        // Sample queries
        updateFrequency("java tutorial");
        updateFrequency("javascript");
        updateFrequency("java download");
        updateFrequency("java tutorial");
        updateFrequency("java 21 features");
        updateFrequency("java tutorial");

        // Search prefix
        search("jav");

        // Update frequency again
        updateFrequency("java 21 features");
        updateFrequency("java 21 features");

        System.out.println("\nUpdated Frequency:");
        System.out.println("java 21 features → " + frequencyMap.get("java 21 features"));
    }
}
