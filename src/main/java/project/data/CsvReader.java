package project.data;

import project.common.ParkingViolation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                        parts[5].trim(),
                        parts[0].trim(),
                        parts[2].trim(),
                        Double.parseDouble(parts[1].trim()),
                        parts[6].length() >= 5 ? parts[6].substring(0, 5) : parts[6]
                );
                result.add(pv);
            }
        } catch (Exception e) {
            System.err.println("Error reading parking violations CSV: " + e.getMessage());
        }
        return result;
    }
}
