package project.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class UI {

    private static UI instance;

    private UI() {

    }

    public static UI getInstance() {
        if (instance == null) {
            instance = new UI();
        }
        return instance;
    }

    public void displayMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1 - Total population");
        System.out.println("2 - Fines per capita");
        System.out.println("3 - Average residential market value");
        System.out.println("4 - Average residential total livable area");
        System.out.println("5 - Residential market value per capita");
        System.out.println("6 - Top N zip codes by fines");
        System.out.println("7 - Percentage of fines per state");
        System.out.println("0 - Exit:");
        System.out.println("Enter choice: ");
    }

    private Scanner scanner = new Scanner(System.in);

    public int readInt() {
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter an integer:");
            }
        }
    }

    public String getSingleZip() {
        System.out.print("Enter ZIP Code: ");
        return scanner.nextLine().trim();
    }

    public String[] getMultipleZips() {
        System.out.print("Enter one or more ZIP Codes separated by commas: ");
        String input = scanner.nextLine().trim();

        // Split and trim each ZIP
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

    public int promptForN() {
        while (true) {
            System.out.print("Enter N (max 50): ");

            int N;
            String input = scanner.nextLine().trim();

            try {
                N = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
                continue; // goes back to start of while
            }

            if (N <= 0) {
                System.out.println("N must be a positive integer.");
                continue; // loop again
            }

            if (N > 50) {
                System.out.println("N too large - limiting to 50.");
                N = 50;
            }

            return N;   // <-- exits the loop AND the method
        }
    }


    //generic output method:
    public <T> void displaySingle(String message, T result) {
        System.out.println(message);
        System.out.println(result);
    }
    //generic output method:
    public <K, V> void displayPairs(String message, Map<K, V> results ) {
        System.out.println(message);
        for (Map.Entry<K, V> entry : results.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    public <K, V> void displayPairs(String message, List<Map.Entry<K, V>> entries) {
        System.out.println(message);
        for (Map.Entry<K, V> entry : entries) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }


}

