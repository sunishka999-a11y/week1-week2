import java.util.*;

public class MultiLevelCacheSystemwithHashTables {

    // Video Data
    static class Video {
        String id;
        String content;

        Video(String id, String content) {
            this.id = id;
            this.content = content;
        }
    }

    // LRU Cache using LinkedHashMap
    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        int capacity;

        LRUCache(int capacity) {
            super(capacity, 0.75f, true); // access-order
            this.capacity = capacity;
        }

        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    // Caches
    static LRUCache<String, Video> L1 = new LRUCache<>(10000);
    static LRUCache<String, Video> L2 = new LRUCache<>(100000);

    // L3 Database (simulated)
    static HashMap<String, Video> database = new HashMap<>();

    // Access count
    static HashMap<String, Integer> accessCount = new HashMap<>();

    // Stats
    static int L1_hits = 0, L2_hits = 0, L3_hits = 0;

    // Get video
    public static Video getVideo(String videoId) {

        long start = System.currentTimeMillis();

        // L1 check
        if (L1.containsKey(videoId)) {
            L1_hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 check
        if (L2.containsKey(videoId)) {
            L2_hits++;
            System.out.println("L2 Cache HIT (5ms)");

            Video video = L2.get(videoId);

            promoteToL1(videoId, video);
            return video;
        }

        System.out.println("L2 Cache MISS");

        // L3 Database
        if (database.containsKey(videoId)) {
            L3_hits++;
            System.out.println("L3 Database HIT (150ms)");

            Video video = database.get(videoId);

            L2.put(videoId, video);
            updateAccess(videoId);

            return video;
        }

        System.out.println("Video not found.");
        return null;
    }

    // Promote to L1
    public static void promoteToL1(String videoId, Video video) {
        L1.put(videoId, video);
        updateAccess(videoId);
        System.out.println("Promoted to L1");
    }

    // Update access count
    public static void updateAccess(String videoId) {
        accessCount.put(videoId, accessCount.getOrDefault(videoId, 0) + 1);
    }

    // Invalidate cache
    public static void invalidate(String videoId) {
        L1.remove(videoId);
        L2.remove(videoId);
        System.out.println("Cache invalidated for " + videoId);
    }

    // Statistics
    public static void getStatistics() {

        int total = L1_hits + L2_hits + L3_hits;

        System.out.println("\n===== CACHE STATISTICS =====");

        System.out.println("L1 Hit Rate: " + percent(L1_hits, total) + "%");
        System.out.println("L2 Hit Rate: " + percent(L2_hits, total) + "%");
        System.out.println("L3 Hit Rate: " + percent(L3_hits, total) + "%");

        System.out.println("Overall Hit Rate: " +
                percent(L1_hits + L2_hits, total) + "%");
    }

    public static double percent(int part, int total) {
        if (total == 0) return 0;
        return (part * 100.0) / total;
    }

    public static void main(String[] args) {

        // Add videos to DB
        database.put("video_123", new Video("video_123", "Movie A"));
        database.put("video_999", new Video("video_999", "Movie B"));

        // First request
        System.out.println("\nRequest 1:");
        getVideo("video_123");

        // Second request
        System.out.println("\nRequest 2:");
        getVideo("video_123");

        // New video
        System.out.println("\nRequest 3:");
        getVideo("video_999");

        // Stats
        getStatistics();

        // Invalidate example
        invalidate("video_123");
    }
}