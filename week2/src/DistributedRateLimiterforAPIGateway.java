import java.util.*;

public class DistributedRateLimiterforAPIGateway {

    // TokenBucket class
    static class TokenBucket {
        int tokens;
        int maxTokens;
        double refillRate; // tokens per second
        long lastRefillTime;

        TokenBucket(int maxTokens, int refillPerHour) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.refillRate = refillPerHour / 3600.0; // convert to tokens per second
            this.lastRefillTime = System.currentTimeMillis();
        }

        // Refill tokens based on time passed
        void refill() {
            long now = System.currentTimeMillis();
            double seconds = (now - lastRefillTime) / 1000.0;

            int tokensToAdd = (int) (seconds * refillRate);

            if (tokensToAdd > 0) {
                tokens = Math.min(maxTokens, tokens + tokensToAdd);
                lastRefillTime = now;
            }
        }

        // Check if request allowed
        synchronized boolean allowRequest() {
            refill();

            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        int remainingTokens() {
            return tokens;
        }
    }

    // clientId -> TokenBucket
    static HashMap<String, TokenBucket> clientBuckets = new HashMap<>();

    static int LIMIT = 1000; // requests per hour

    // Rate limit check
    public static void checkRateLimit(String clientId) {

        clientBuckets.putIfAbsent(clientId, new TokenBucket(LIMIT, LIMIT));

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.allowRequest()) {
            System.out.println("Allowed (" + bucket.remainingTokens() + " requests remaining)");
        } else {
            System.out.println("Denied (0 requests remaining, retry later)");
        }
    }

    // Show status
    public static void getRateLimitStatus(String clientId) {

        if (!clientBuckets.containsKey(clientId)) {
            System.out.println("Client not found.");
            return;
        }

        TokenBucket bucket = clientBuckets.get(clientId);

        int used = LIMIT - bucket.remainingTokens();

        System.out.println("{used: " + used +
                ", limit: " + LIMIT +
                ", remaining: " + bucket.remainingTokens() + "}");
    }

    public static void main(String[] args) {

        String client = "abc123";

        // Simulate requests
        for (int i = 0; i < 5; i++) {
            checkRateLimit(client);
        }

        // Show status
        getRateLimitStatus(client);
    }
}