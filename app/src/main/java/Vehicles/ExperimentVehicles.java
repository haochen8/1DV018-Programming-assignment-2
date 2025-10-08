package Vehicles;

import Hashing.HashTable;
import Vehicles.Vehicle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Experiment for Task 5: analyze hashfunction
 *
 * CLI:
 * args[0] = total vehicles (default 10000)
 * args[1] = csv-file (valfritt).
 *
 * Example:
 * ./gradlew run -PmainClass=Vehicles.ExperimentVehicles --args="20000
 * buckets.csv"
 */
public class ExperimentVehicles {
  private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final Random RNG = new Random(42); // deterministisk

  public static void main(String[] args) throws Exception {
    int n = args.length > 0 ? Integer.parseInt(args[0]) : 10_000;
    String csv = (args.length > 1 && !args[1].equals("0")) ? args[1] : null;

    // Small table to force hash
    HashTable<Vehicle, Integer> table = new HashTable<>(256, 0.75);

    // Unique reg numbers
    int inserted = 0;
    while (inserted < n) {
      String plate = (RNG.nextBoolean()) ? genABC123() : genABC12A();
      Vehicle v = new Vehicle(plate);
      Integer old = table.put(v, inserted);
      if (old == null) {
        inserted++;
      }
    }

    // Statistics
    int[] buckets = table.bucketSizes();
    int maxChain = 0;
    long nonEmpty = 0;
    long sum = 0;
    for (int b : buckets) {
      sum += b;
      if (b > 0) {
        nonEmpty++;
      }
      if (b > maxChain)
        maxChain = b;
    }
    double avg = sum / (double) buckets.length;
    double std = stdDev(buckets, avg);

    System.out.println("=== Assignment 5 – Hash function quality ===");
    System.out.println("Vehicles (unique): " + table.size());
    System.out.println("Capacity (buckets): " + buckets.length);
    System.out.println("Load factor:        " + table.currentLoadFactor());
    System.out.println("Collisions (put):   " + table.collisions());
    System.out.println("Non-empty buckets:  " + nonEmpty + " / " + buckets.length);
    System.out.println("Max chain length:   " + maxChain);
    System.out.println("Avg chain length:   " + String.format("%.3f", avg));
    System.out.println("Stddev chain len:   " + String.format("%.3f", std));

    if (csv != null) {
      writeCsv(csv, buckets);
      System.out.println("Wrote bucket distribution to: " + csv);
    }

    // Sanity check
    for (int i = 0; i < 5; i++) {
      String p = genABC123();
      System.out.println("Probe get(" + p + "): " + table.get(new Vehicle(p)));
    }
  }

  // Generate Swedish License Plates
  // ABC123
  private static String genABC123() {
    char a = LETTERS.charAt(RNG.nextInt(LETTERS.length()));
    char b = LETTERS.charAt(RNG.nextInt(LETTERS.length()));
    char c = LETTERS.charAt(RNG.nextInt(LETTERS.length()));
    int num = RNG.nextInt(1000); // 000..999
    return "" + a + b + c + String.format("%03d", num);
  }

  // ABC12A (New format with last letter instead of number)
  private static String genABC12A() {
    char a = LETTERS.charAt(RNG.nextInt(LETTERS.length()));
    char b = LETTERS.charAt(RNG.nextInt(LETTERS.length()));
    char c = LETTERS.charAt(RNG.nextInt(LETTERS.length()));
    int d = RNG.nextInt(10);
    int e = RNG.nextInt(10);
    char f = RNG.nextBoolean() ? LETTERS.charAt(RNG.nextInt(LETTERS.length()))
        : (char) ('0' + RNG.nextInt(10));
    return "" + a + b + c + d + e + f;
  }

  private static double stdDev(int[] arr, double mean) {
    double ss = 0;
    for (int x : arr) {
      double d = x - mean;
      ss += d * d;
    }
    return Math.sqrt(ss / arr.length);
  }

  private static void writeCsv(String path, int[] buckets) throws IOException {
    try (BufferedWriter w = new BufferedWriter(new FileWriter(path))) {
      w.write("bucket,size\n");
      for (int i = 0; i < buckets.length; i++) {
        w.write(i + "," + buckets[i] + "\n");
      }
    }
  }
}
