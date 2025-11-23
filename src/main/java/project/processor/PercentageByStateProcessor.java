package project.processor;

import project.common.ParkingViolation;
import project.common.ProjectData;

import java.util.*;

public class PercentageByStateProcessor {
    private final ProjectData pd;

    public PercentageByStateProcessor(ProjectData pd) {
        this.pd = pd;
    }

    public Map<String, Double> run() {
        Map<String, Integer> counts = new HashMap<>();

        for (ParkingViolation pv : pd.getParkingViolations()) {
            String state = pv.getState();
            if (state == null || state.trim().isEmpty()) continue;

            state = state.trim().toUpperCase();
            counts.put(state, counts.getOrDefault(state, 0) + 1);
        }

        int total = pd.getParkingViolations().size();
        if (total == 0) {
            return Collections.emptyMap();
        }

        //Sort states by descending count
        Map<String, Double> percentages = new HashMap<>();
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            double pct = (100.0 * e.getValue()) / total;
            pct = Math.round(pct * 100.0) / 100.0;
            percentages.put(e.getKey(), pct);
        }

        //Sort states by descending count
        List<Map.Entry<String, Double>> sortedList = new ArrayList<>(percentages.entrySet());
        sortedList.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        //use linkedHashMap to preserve order
        Map<String, Double> sortedPercentages = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry: sortedList) {
            sortedPercentages.put(entry.getKey(), entry.getValue());
        }

        return sortedPercentages;
    }
}