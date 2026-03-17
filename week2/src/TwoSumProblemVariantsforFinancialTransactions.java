import java.util.*;

public class TwoSumProblemVariantsforFinancialTransactions {

    // Transaction class
    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        long time; // in minutes

        Transaction(int id, int amount, String merchant, String account, long time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }
    }

    // CLASSIC TWO-SUM
    public static void findTwoSum(List<Transaction> transactions, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        System.out.println("\nTwo Sum Results:");

        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction t2 = map.get(complement);
                System.out.println("(" + t2.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    // TWO-SUM WITH TIME WINDOW (1 hour = 60 min)
    public static void findTwoSumWithTime(List<Transaction> transactions, int target) {

        System.out.println("\nTwo Sum (Within 1 Hour):");

        for (int i = 0; i < transactions.size(); i++) {
            for (int j = i + 1; j < transactions.size(); j++) {

                Transaction t1 = transactions.get(i);
                Transaction t2 = transactions.get(j);

                if (Math.abs(t1.time - t2.time) <= 60 &&
                        t1.amount + t2.amount == target) {

                    System.out.println("(" + t1.id + ", " + t2.id + ")");
                }
            }
        }
    }

    // DUPLICATE DETECTION
    public static void detectDuplicates(List<Transaction> transactions) {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        System.out.println("\nDuplicate Transactions:");

        for (Transaction t : transactions) {
            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {
            List<Transaction> list = map.get(key);

            if (list.size() > 1) {
                System.out.print("Duplicate → Amount & Merchant: " + key + " Accounts: ");

                for (Transaction t : list) {
                    System.out.print(t.account + " ");
                }
                System.out.println();
            }
        }
    }

    // K-SUM (recursive)
    public static void findKSum(List<Transaction> transactions, int k, int target) {
        System.out.println("\nK-Sum Results:");
        kSumHelper(transactions, k, target, 0, new ArrayList<>());
    }

    private static void kSumHelper(List<Transaction> transactions, int k, int target,
                                   int index, List<Integer> current) {

        if (k == 0 && target == 0) {
            System.out.println(current);
            return;
        }

        if (k <= 0 || index >= transactions.size()) return;

        for (int i = index; i < transactions.size(); i++) {

            Transaction t = transactions.get(i);

            current.add(t.id);
            kSumHelper(transactions, k - 1, target - t.amount, i + 1, current);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, 500, "StoreA", "acc1", 600));
        transactions.add(new Transaction(2, 300, "StoreB", "acc2", 615));
        transactions.add(new Transaction(3, 200, "StoreC", "acc3", 630));
        transactions.add(new Transaction(4, 500, "StoreA", "acc4", 640)); // duplicate

        // Run all features
        findTwoSum(transactions, 500);
        findTwoSumWithTime(transactions, 500);
        detectDuplicates(transactions);
        findKSum(transactions, 3, 1000);
    }
}