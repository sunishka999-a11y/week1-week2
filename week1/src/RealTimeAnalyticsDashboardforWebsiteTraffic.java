import java.util.*;

public class RealTimeAnalyticsDashboardforWebsiteTraffic {

    // pageUrl -> visit count
    static HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique users
    static HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    static HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process incoming event
    public static void processEvent(String url, String userId, String source) {

        // Count page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Count traffic sources
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // Display dashboard
    public static void getDashboard() {

        System.out.println("\n===== REAL TIME ANALYTICS DASHBOARD =====\n");

        // Top pages
        System.out.println("Top Pages:");

        List<Map.Entry<String, Integer>> list = new ArrayList<>(pageViews.entrySet());

        // Sort by visits descending
        list.sort((a, b) -> b.getValue() - a.getValue());

        int count = 0;
        for (Map.Entry<String, Integer> entry : list) {

            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println((count + 1) + ". " + page +
                    " - " + views + " views (" + unique + " unique)");

            count++;
            if (count == 10) break;
        }

        // Traffic sources
        System.out.println("\nTraffic Sources:");
        for (String source : trafficSources.keySet()) {
            System.out.println(source + " : " + trafficSources.get(source));
        }
    }

    public static void main(String[] args) {

        // Simulating real-time events
        processEvent("/article/breaking-news", "user_123", "google");
        processEvent("/article/breaking-news", "user_456", "facebook");
        processEvent("/sports/championship", "user_222", "google");
        processEvent("/sports/championship", "user_333", "direct");
        processEvent("/sports/championship", "user_222", "google");
        processEvent("/tech/ai-future", "user_777", "twitter");

        // Display dashboard
        getDashboard();
    }
}