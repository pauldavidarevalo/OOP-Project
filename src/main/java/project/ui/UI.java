package project.ui;

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
        System.out.println("6 - ???");
        System.out.println("7 - ???");
        System.out.println("0 - Exit:");
        System.out.println("Enter choice: ");
    }

    private Scanner scanner = new Scanner(System.in);

    public int readInt() {
        return scanner.nextInt();
    }

    //generic output method:
    public <T> void displaySingle(T result) {
        System.out.println(result);
    }
    //generic output method:
    public <K, V> void displayPairs(Map<K, V> results ) {
        for (Map.Entry<K, V> entry : results.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // etc.
}

