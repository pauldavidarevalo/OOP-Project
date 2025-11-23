package project;

import project.common.ProjectData;
import project.common.ParkingViolation;
import project.common.PropertyValue;
import project.data.TextReader;
import project.data.JsonReader;
import project.data.CsvReader;
import project.processor.*;

import java.io.File;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
                        TotalPopulationProcessor totalProcessor = new TotalPopulationProcessor(pd);
                        int totalPopulation = totalProcessor.run();
                        ui.displaySingle(totalPopulation);
                        break;
                    case 2:
                        FinesPerCapitaProcessor finesProcessor = new FinesPerCapitaProcessor(pd);
                        Map<String, Double> finesPerCapita = finesProcessor.run();
                        ui.displayPairs(finesPerCapita);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        System.out.print("Enter ZIP Code: ");
                        sc.nextLine();
                        String zip = sc.nextLine().trim();
                        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(pd);
                        int value = processor.run(zip);
                        ui.displaySingle(value);
                        break;
                    case 6:
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
                        TopNZipCodeByFinesProcessor processor = new TopNZipCodeByFinesProcessor(pd);
                        List<Map.Entry<String, Double>> result = processor.run(N);
                        ui.displayPairs(result);
                        break;
                    case 7:
                        PercentageByStateProcessor processor = new PercentageByStateProcessor(pd);
                        Map<String, Double> result = processor.run();
                        ui.displayPairs(result);
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

}


