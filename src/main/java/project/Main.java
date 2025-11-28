package project;

import project.common.ProjectData;
import project.common.ParkingViolation;
import project.common.PropertyValue;
import project.data.*;
import project.processor.*;
import project.ui.UI;

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
            ParkingViolationReader reader;
            if (format.equals("csv")) {
                reader = new CsvReader();
            } else {
                reader = new JsonReader();
            }
            List<ParkingViolation> parkingViolations = reader.readParkingViolations(parkingFile);

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
            //create the processor once for option 5; for memoization...
            MarketValuePerCapitaProcessor marketValuePerCapitaProcessor = new MarketValuePerCapitaProcessor(pd);

            //create Singleton UI class
            UI ui = UI.getInstance();

            /*  Menu Functionality Begins

             **/
            while (true) {
                ui.displayMenu();
                int choice = ui.readInt();

                switch (choice) {
                    case 1:
                        TotalPopulationProcessor totalProcessor = new TotalPopulationProcessor(pd);
                        int totalPopulation = totalProcessor.run();
                        ui.displaySingle("\nTotal Population for all zip codes is:",
                                totalPopulation);
                        break;
                    case 2:
                        FinesPerCapitaProcessor finesProcessor = new FinesPerCapitaProcessor(pd);
                        Map<String, Double> finesPerCapita = finesProcessor.run();
                        ui.displayPairs("\nFines Per Capita for each zip code:",
                                finesPerCapita);
                        break;
                    case 3:
                        AverageMarketValueProcessor avgMarketProcessor = new AverageMarketValueProcessor(pd);
                        String[] zipArray = ui.getMultipleZips();
                        int avgMarketValue = avgMarketProcessor.run(zipArray);
                        ui.displaySingle("\nAverage Residential Market Value for this area is:",
                                avgMarketValue);
                        break;
                    case 4:
                        AverageLivableAreaProcessor avgLivableProcessor = new AverageLivableAreaProcessor(pd);
                        String zipLivable = ui.getSingleZip();
                        int avgLivableArea = avgLivableProcessor.run(zipLivable);
                        ui.displaySingle("\nAverage Residential Livable Area for this zip code is:",
                                avgLivableArea);
                        break;
                    case 5:
                        String zip = ui.getSingleZip();
                        int valuePerCap = marketValuePerCapitaProcessor.run(zip);
                        ui.displaySingle("\nMarket Value Per Capita for this zip code is:",
                                valuePerCap);
                        break;
                    case 6:
                        TopNZipCodeByFinesProcessor topNZipProcessor = new TopNZipCodeByFinesProcessor(pd);
                        int N = ui.promptForN();
                        List<Map.Entry<String, Double>> topZips = topNZipProcessor.run(N);
                        ui.displayPairs("\nTop Zip Codes by Fines:",
                                topZips);
                        break;
                    case 7:
                        PercentageByStateProcessor byStateProcessor = new PercentageByStateProcessor(pd);
                        Map<String, Double> resultByState = byStateProcessor.run();
                        ui.displayPairs("\nPercentage of Fines per state:",
                                resultByState);
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


