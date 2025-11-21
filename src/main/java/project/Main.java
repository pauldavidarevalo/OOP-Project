package project;

import project.common.ProjectData;
import project.common.ParkingViolation;
import project.common.PropertyValue;
import project.data.TextReader;
import project.data.JsonReader;
import project.data.CsvReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

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
                System.out.println("6 - ???");
                System.out.println("7 - ???");
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
                        break;
                    case 7:
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
}


