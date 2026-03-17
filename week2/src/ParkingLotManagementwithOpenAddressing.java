import java.util.*;

public class ParkingLotManagementwithOpenAddressing {

    static final int SIZE = 500;

    // Parking spot structure
    static class ParkingSpot {
        String licensePlate;
        long entryTime;
        boolean isOccupied;

        ParkingSpot() {
            this.licensePlate = null;
            this.entryTime = 0;
            this.isOccupied = false;
        }
    }

    static ParkingSpot[] table = new ParkingSpot[SIZE];

    static int totalVehicles = 0;
    static int totalProbes = 0;

    static {
        for (int i = 0; i < SIZE; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    public static int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % SIZE;
    }

    // Park vehicle using linear probing
    public static void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].isOccupied) {
            index = (index + 1) % SIZE;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].isOccupied = true;

        totalVehicles++;
        totalProbes += probes;

        System.out.println("parkVehicle(\"" + licensePlate + "\") → Assigned spot #"
                + index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public static void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].isOccupied) {

            if (licensePlate.equals(table[index].licensePlate)) {

                long exitTime = System.currentTimeMillis();
                long durationMillis = exitTime - table[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);
                double fee = Math.ceil(hours * 5); // $5 per hour

                table[index].isOccupied = false;
                table[index].licensePlate = null;

                System.out.println("exitVehicle(\"" + licensePlate + "\") → Spot #"
                        + index + " freed, Duration: "
                        + String.format("%.2f", hours) + "h, Fee: $" + fee);

                return;
            }

            index = (index + 1) % SIZE;
            probes++;
        }

        System.out.println("Vehicle not found.");
    }

    // Find nearest available spot
    public static int findNearestSpot() {
        for (int i = 0; i < SIZE; i++) {
            if (!table[i].isOccupied) {
                return i;
            }
        }
        return -1;
    }

    // Statistics
    public static void getStatistics() {

        int occupied = 0;

        for (ParkingSpot spot : table) {
            if (spot.isOccupied) occupied++;
        }

        double occupancy = (occupied * 100.0) / SIZE;
        double avgProbes = totalVehicles == 0 ? 0 : (double) totalProbes / totalVehicles;

        System.out.println("\n===== PARKING STATISTICS =====");
        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Average Probes: " + String.format("%.2f", avgProbes));
        System.out.println("Nearest Free Spot: #" + findNearestSpot());
    }

    public static void main(String[] args) {

        parkVehicle("ABC-1234");
        parkVehicle("ABC-1235");
        parkVehicle("XYZ-9999");

        try { Thread.sleep(2000); } catch (Exception e) {}

        exitVehicle("ABC-1234");

        getStatistics();
    }
}