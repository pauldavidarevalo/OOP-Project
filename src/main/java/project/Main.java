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

        } catch (Exception e) {
            System.err.println("ERROR: Failed to load data: " + e.getMessage());
        }
    }


    private static boolean fileExists(String filename) {
        File f = new File(filename);
        return f.exists() && f.canRead();
    }
}


