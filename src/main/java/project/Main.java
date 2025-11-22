package project;

import project.common.ProjectData;
import project.common.ParkingViolation;
import project.common.PropertyValue;
import project.data.TextReader;
import project.data.JsonReader;
import project.data.CsvReader;

import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        // Validate argument count
        if (args.length != 4) {
            System.err.println("ERROR: Expected 4 arguments: <csv|json> <parkingFile> <propertyFile> <populationFile>");
            return;
        }

        String format = args[0];
        String parkingFile = args[1];
        String propertyFile = args[2];
        String populationFile = args[3];

        // Validate format
        if (!format.equals("csv") && !format.equals("json")) {
            System.err.println("ERROR: First argument must be \"csv\" or \"json\".");
            return;
        }

        // Validate file existence
        if (!fileExists(parkingFile) || !fileExists(propertyFile) || !fileExists(populationFile)) {
            System.err.println("ERROR: One or more input files do not exist or cannot be opened.");
            return;
        }

        try {
            // Load parking violations
            List<ParkingViolation> parkingViolations;
            if (format.equals("csv")) {
                parkingViolations = CsvReader.readParkingViolations(parkingFile);
            } else {
                parkingViolations = JsonReader.readParkingViolations(parkingFile);
            }

            // Load property values
            List<PropertyValue> propertyValues =
                    CsvReader.readPropertyValues(propertyFile);

            // Load ZIP population map
            HashMap<String, Integer> zipPopulation =
                    TextReader.readZipPopulation(populationFile);

            // Build central data object
            ProjectData pd = new ProjectData(
                    parkingViolations,
                    propertyValues,
                    zipPopulation
            );

            /**  Menu Functionality Begins
            ProcessorA.run(pd);
            ProcessorB.run(pd);
            */
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("\nMain Menu:");
                System.out.println("1 - Total population");
                System.out.println("2 - Fines per capita");
                System.out.println("3 - Average residential market value");
                System.out.println("4 - Average residential total livable area");
                System.out.println("5 - Residential market value per capita");
                System.out.println("6 - Top N ZIP codes by total fines (PA only)");
                System.out.println("7 - Percentage of violations by state");
                System.out.println("0 - Exit:");
                System.out.println("Enter choice: ");

                String input = sc.nextLine().trim();
                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input, try again.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        displayMarketValuePerCapita(pd, sc);
                        break;
                    case 6:
                        displayTopNZipCodeByFines(pd, sc);
                        break;
                    case 7:
                        displayPercentageByState(pd);
                        break;
                    case 0:
                        System.out.println("Exiting.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }

            }

        } catch (Exception e) {
            System.err.println("ERROR: Failed to load data: " + e.getMessage());
        }
    }


    private static boolean fileExists(String filename) {
        File f = new File(filename);
        return f.exists() && f.canRead();
    }
    //Option 5 method
    private static void displayMarketValuePerCapita(ProjectData pd, java.util.Scanner sc) {
        System.out.print("Enter ZIP Code: ");
        String zip = sc.nextLine().trim();

        //compute total market value for this zip
        double totalMarketValue = 0.0;
        for (PropertyValue pv : pd.getPropertyValues()) {
            if (zip.equals(pv.getZipCode()) && pv.getMarketValue() > 0) {
                totalMarketValue += pv.getMarketValue();
            }
        }

        //get population for this ZIP
        int population = pd.getZipPopulation().getOrDefault(zip, 0);

        //compute market value per capita
        int valuePerCapita = 0;
        if (totalMarketValue > 0 && population > 0) {
            valuePerCapita = (int) Math.round(totalMarketValue / population);
        }

        System.out.println("Residential market value per capita for ZIP " + zip + ": " + valuePerCapita);
    }

    //Option 6 method
    private static void displayTopNZipCodeByFines(ProjectData pd, Scanner sc) {
        System.out.print("Enter N (max 50):");
        int N;
        try {
            N = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return;
        }

        if (N <= 0) {
            System.out.println("N must be a positive integer.");
            return;
        }
        if (N > 50) {
            System.out.println("N too large - limiting to 50.");
            N = 50;
        }

        //aggregate total fines by ZIP for PA plates
        HashMap<String, Double> zipTotals = new HashMap<>();

        for (ParkingViolation pv : pd.getParkingViolations()) {
            if (pv.getState() == null || pv.getZipCode() == null) continue;
            if (!pv.getState().equalsIgnoreCase("PA")) continue;
            if (pv.getZipCode().isEmpty()) continue;

            zipTotals.put(
                    pv.getZipCode(),
                    zipTotals.getOrDefault(pv.getZipCode(), 0.0) + pv.getFine()
            );
        }

        //Sort descending by total fines
        List<HashMap.Entry<String, Double>> sorted = new java.util.ArrayList<>(zipTotals.entrySet());

        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        System.out.println("\nTop " + N + " ZIP Codes by Total Fines:");
        for (int i = 0; i < Math.min(N, sorted.size()); i++) {
            var entry = sorted.get(i);
            System.out.printf("%s $%.2f%n", entry.getKey(), entry.getValue());
        }
    }

    //Option 7 method
    private static void displayPercentageByState(ProjectData pd) {
        HashMap<String, Integer> counts = new HashMap<>();

        for (ParkingViolation pv : pd.getParkingViolations()) {
            String state = pv.getState();
            if (state == null || state.trim().isEmpty()) continue;

            state = state.trim();
            counts.put(state, counts.getOrDefault(state, 0) + 1);
        }

        int total = pd.getParkingViolations().size();
        if (total == 0) {
            System.out.println("No violation data loaded.");
            return;
        }

        //Sort states by descending count
        List<Map.Entry<String, Integer>> list = new ArrayList<>(counts.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        System.out.println("\nPercentage of Violations by State:");
        for (Map.Entry<String, Integer> e : list) {
            double pct = (100.0 * e.getValue()) / total;
            System.out.printf("%-5s : %.2f%%%n", e.getKey(), pct);
        }
    }
}


