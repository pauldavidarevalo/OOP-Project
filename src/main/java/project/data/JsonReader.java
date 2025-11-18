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

                ParkingViolation pv = new ParkingViolation(
                        String.valueOf(ticket.get("ticket_number")),
                        (String) ticket.get("date"),
                        (String) ticket.get("violation"),
                        Double.parseDouble(ticket.get("fine").toString()),
                        ticket.get("zip_code") != null
                            ? ticket.get("zip_code").toString().substring(0, Math.min(5, ticket.get("zip_code").toString().length()))
                                : ""
                );
                result.add(pv);
            }
        } catch (Exception e) {
            System.err.println("error reading JSON file: " + e.getMessage());
        }
        return result;
    }
}