package project.processor;

import project.common.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class FinesPerCapitaProcessor {
    ProjectData projectData;

    public FinesPerCapitaProcessor(ProjectData pd) {
        this.projectData = pd;
    }
    public TreeMap<String, Double> run() {
        TreeMap<String, Double> finesPerCapita = new TreeMap<>();

        // Step 1: accumulate total fines per ZIP
        Map<String, Integer> zipPopulation = projectData.getZipPopulation();
        Map<String, Double> zipFines = new HashMap<>();

        for (ParkingViolation pv : projectData.getParkingViolations()) {
            String zip = pv.getZipCode();
            String state = pv.getState();

            // ignore bad records
            if (zip == null || zip.isEmpty()) continue;
            if (!"PA".equals(state)) continue;

            // accumulate fines
            Double fine = pv.getFine();
            zipFines.put(zip, zipFines.getOrDefault(zip, 0.0000) + fine);
        }

        // Step 2: compute fines / population
        for (String zip : zipFines.keySet()) {
            Double totalFines = zipFines.get(zip);
            int population = zipPopulation.getOrDefault(zip, 0);

            if (totalFines == 0) continue;      // skip requirement
            if (population == 0) continue;      // skip requirement


            double raw = (double) totalFines / population;

            double fpc = new BigDecimal(raw)
                    .setScale(4, RoundingMode.HALF_UP)
                    .doubleValue();

            finesPerCapita.put(zip, fpc);
        }

        return finesPerCapita;   // <-- automatically sorted!
    }



}
