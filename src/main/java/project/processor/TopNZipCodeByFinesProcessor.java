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
        //aggregate total fines by ZIP for PA plates
        HashMap<String, Double> zipTotals = new HashMap<>();

        for (ParkingViolation pv : pd.getParkingViolations()) {
            if (pv.getState() == null || pv.getZipCode() == null) continue;
            if (!pv.getState().equalsIgnoreCase("PA")) continue;
            if (pv.getZipCode().isEmpty()) continue;

            zipTotals.put(
                    pv.getZipCode(),
                    zipTotals.getOrDefault(pv.getZipCode(), 0.0) + pv.getFine()
            );
        }

        //Sort descending by total fines
        List<Map.Entry<String, Double>> sorted = new ArrayList<>(zipTotals.entrySet());

        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        //return only top N
        return sorted.subList(0, Math.min(N, sorted.size()));
    }
}