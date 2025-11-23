package project.processor;

import project.common.ProjectData;
import project.common.PropertyValue;

public class MarketValuePerCapitaProcessor {
    private final ProjectData pd;

    public MarketValuePerCapitaProcessor(ProjectData pd) {
        this.pd = pd;
    }

    public int run(String zip) {
        //compute total market value for this zip
        double totalMarketValue = 0.0;
        for (PropertyValue pv : pd.getPropertyValues()) {
            if (zip.equals(pv.getZipCode()) && pv.getMarketValue() > 0) {
                totalMarketValue += pv.getMarketValue();
            }
        }

        //get population for this ZIP
        int population = pd.getZipPopulation().getOrDefault(zip, 0);

        //compute market value per capita
        if (population == 0 || totalMarketValue == 0) {
            return 0;
        }

        return (int) Math.round(totalMarketValue / population);
    }
}