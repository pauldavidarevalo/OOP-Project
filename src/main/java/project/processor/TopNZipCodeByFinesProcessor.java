package project.processor;

import project.common.ParkingViolation;
import project.common.ProjectData;

import java.util.*;

public class TopNZipCodeByFinesProcessor {
    private final ProjectData pd;

    public TopNZipCodeByFinesProcessor(ProjectData pd) {
        this.pd = pd;
    }

    public List<Map.Entry<String, Double>> run(int N) {
        if (N <= 0) return Collections.emptyList();
        //aggregate total fines by ZIP for PA plates
        List<ParkingViolation> violations = pd.getParkingViolations();
        if (violations == null || violations.isEmpty()) {
            return Collections.emptyList();
        }
        HashMap<String, Double> zipTotals = new HashMap<>();

        for (ParkingViolation pv : violations) {
            if (pv == null) continue;

            String state = pv.getState();
            String zip = pv.getZipCode();
            if (state == null || zip == null) continue;
            if (!state.equalsIgnoreCase("PA")) continue;
            if (zip.isEmpty()) continue;

            double fine = pv.getFine();
            zipTotals.put(zip, zipTotals.getOrDefault(zip, 0.0) + fine);
        }

        //Sort descending by total fines
        List<Map.Entry<String, Double>> sorted = new ArrayList<>(zipTotals.entrySet());

        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        //return only top N
        return sorted.subList(0, Math.min(N, sorted.size()));
    }
}