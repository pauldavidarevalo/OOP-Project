package project.data;
import project.common.ParkingViolation;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.util.*;

public class JsonReader {
    /** make the method static so no instance is needed and with filename as parameter

     */
    public static List<ParkingViolation> readParkingViolations(String path) {
        List<ParkingViolation> result = new ArrayList<>();
        JSONParser parser  = new JSONParser();

        try (FileReader reader = new FileReader(path)) {
            JSONArray arr = (JSONArray) parser.parse(reader);

            for (Object o : arr){
                JSONObject ticket = (JSONObject) o;

                String issueDate = (String) ticket.get("date");
                double fine = ((Number) ticket.get("fine")).doubleValue();
                String description = (String) ticket.get("violation");
                String vehicleId = (String) ticket.get("plate_id");
                String state = (String) ticket.get("state");
                String violationId = String.valueOf(ticket.get("ticket_number"));
                String rawZip = ticket.get("zip_code") != null
                        ? ticket.get("zip_code").toString().trim()
                        : "";
                String zip = rawZip.length() >= 5 ? rawZip.substring(0, 5) : rawZip;

                ParkingViolation pv = new ParkingViolation(
                        issueDate,
                        fine,
                        description,
                        vehicleId,
                        state,
                        violationId,
                        zip
                );
                result.add(pv);
            }
        } catch (Exception e) {
            System.err.println("error reading JSON file: " + e.getMessage());
        }
        return result;
    }
}