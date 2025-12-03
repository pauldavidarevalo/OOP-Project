package project.processor;

import project.common.ProjectData;
import project.common.PropertyValue;

import java.util.HashMap;
import java.util.Map;

public class MarketValuePerCapitaProcessor {
    private final ProjectData pd;
    private final Map<String, Integer> cache;

    public MarketValuePerCapitaProcessor(ProjectData pd) {
        this.pd = pd;
        this.cache = new HashMap<>();
    }

    public int run(String zip) {
        if (zip == null || zip.isEmpty()) {
            return 0;
        }

        if (cache.containsKey(zip)) {
            return cache.get(zip);
        }

        if (pd.getPropertyValues() == null) {
            cache.put(zip, 0);
            return 0;
        }
        //compute total market value for this zip
        double totalMarketValue = 0.0;
        for (PropertyValue pv : pd.getPropertyValues()) {
            if (pv == null) continue;

            String pvZip = pv.getZipCode();
            if(pvZip == null) continue;

            if (zip.equals(pvZip) &&
                    pv.getMarketValue() > 0) {
                totalMarketValue += pv.getMarketValue();
            }
        }

        //get population for this ZIP
        int population = 0;
        if (pd.getZipPopulation() != null) {
            population = pd.getZipPopulation().getOrDefault(zip, 0);
        }

        //compute market value per capita
        int result;
        if (population == 0 || totalMarketValue == 0) {
            result = 0;
        } else {
            result = (int) Math.round(totalMarketValue / population);
        }
        cache.put(zip, result);
        return result;
    }
}