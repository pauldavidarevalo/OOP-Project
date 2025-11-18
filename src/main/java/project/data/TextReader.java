package project.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class TextReader {
    /** reader to parse population.txt into a HashMap mapping Zip -> Population
     * return this HashMap
     * Method is static so no instance is needed to call it with file name as
     * parameter.
     */
    public static HashMap <String, Integer > readZipPopulation(String filename){

        HashMap<String, Integer> map = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+"); // split on whitespace
                if (parts.length == 2) {
                    String zip = parts[0];          // ZIP stays a string
                    int population = Integer.parseInt(parts[1]);
                    map.put(zip, population);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
