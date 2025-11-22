package project.data;

import project.common.PropertyValue;
import project.common.ParkingViolation;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvReader {
    /** method to read parking violations in csv
     *
     * method to read property values in csv which figures out the correct
     * columns to read.
     *
     * make the methods static so no instance is needed and with filename as parameter
     */
    public static List<ParkingViolation> readParkingViolations(String path) {
        List<ParkingViolation> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) continue;
                ParkingViolation pv = new ParkingViolation(
                        parts[0].trim(), //issueDate
                        Double.parseDouble(parts[1].trim()), //fine
                        parts[2].trim(), //description
                        parts[3].trim(), //vehicleId
                        parts[4].trim(), //state
                        parts[5].trim(), //violationId
                        parts[6].length() >= 5 ? parts[6].substring(0, 5) : parts[6] //zipCode
                );
                result.add(pv);
            }
        } catch (Exception e) {
            System.err.println("Error reading parking violations CSV: " + e.getMessage());
        }
        return result;
    }

     public static List<PropertyValue> readPropertyValues(String filename){
        List<PropertyValue> propertyValues = new ArrayList<>();
        String headerLine = null;
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            headerLine = br.readLine();
            if (headerLine == null) {
                return propertyValues; // empty file
            }
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                rows.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Identify column indices
        int market_value_idx = -1;
        int total_livable_area_idx = -1;
        int zip_code_idx = -1;
        String[] headers = headerLine.split(",");
        for (int i = 0; i < headers.length; i++) {
            if(headers[i].equals("market_value")){
                market_value_idx = i;
            } else if (headers[i].equals("total_livable_area")){
                total_livable_area_idx = i;
            } else if (headers[i].equals("zip_code")){
                zip_code_idx = i;
            }
        }

        // Make sure all required columns are found
        if(market_value_idx == -1 || total_livable_area_idx == -1 || zip_code_idx == -1){
            throw new IllegalArgumentException("Required columns are missing in the CSV file");
        }

        // Parse rows and create PropertyValue objects
        for (String[] row : rows) {
            if(row == null || row.length == 0) {
                continue; // Skip empty rows
            }

            try {
                
                // Defualt market_value to 0.0 and will be skipped later
                // Don't parse if empty
                double market_value = 0.0;
                String mvStr = row[market_value_idx];
                if(mvStr != null && !mvStr.isEmpty()) {
                    try{
                        market_value = Double.parseDouble(mvStr);
                    } catch (Exception e) {}// Do nothing  
                }
                
                // Defualt total_livable_area to 0.0 and will be skipped later
                // Don't parse if empty
                double total_livable_area = 0.0;
                String tlaStr = row[total_livable_area_idx];
                if(tlaStr != null && !tlaStr.isEmpty()) {
                    try{
                        total_livable_area = Double.parseDouble(tlaStr);
                    } catch (Exception e) {} // Do nothing
                }

                // Don't substring if empty or too short
                String zip_code = row[zip_code_idx];
                if(zip_code != null && !zip_code.isEmpty() && zip_code.length() > 5)
                    zip_code = zip_code.substring(0, 5); // first 5 characters

                // Create PropertyValue object and add to list
                PropertyValue pv = new PropertyValue(market_value, total_livable_area, zip_code);
                propertyValues.add(pv);

            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                // Handle parsing errors or missing data
                e.printStackTrace();
            }
        }


        return propertyValues;
     }

    /**
     * Temporary main for quick local testing. Usage:
     *   java project.data.CsvReader <path-to-csv>
     * This prints the parsed PropertyValue objects to stdout.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java project.data.CsvReader <path-to-csv>");
            return;
        }

        String filename = args[0];
        try {
            List<PropertyValue> values = readPropertyValues(filename);
            List<PropertyValue> head = values.subList(0, Math.min(5, values.size()));
            System.out.println("Read " + values.size() + " property values from: " + filename);
            // System.out.println("First 5 entries:");
            for (PropertyValue pv : values) {
                System.out.printf("Market Value: %.2f, Total Livable Area: %.2f, Zip Code: %s%n",
                    pv.getMarketValue(), pv.getTotalLivableArea(), pv.getZipCode());
            }
        } catch (Exception e) {
            System.err.println("Error reading CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
