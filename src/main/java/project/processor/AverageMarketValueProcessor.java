package project.processor;

import project.common.*;
import java.util.List;

public class AverageMarketValueProcessor {
    ProjectData projectData;

    public AverageMarketValueProcessor(ProjectData pd) {
        this.projectData = pd;
    }

    public double run() {
        double totalMarketValue = 0.0;
        int count = 0;

        List<PropertyValue> propertyValues = projectData.getPropertyValues();
        // Check for null list
        if(propertyValues == null){
            System.err.println("No property values available.");
            return 0;
        }

        for (PropertyValue pv : propertyValues) {
            try {
                if(pv == null) continue;                 // guard null entries
                double marketValue = pv.getMarketValue();
                // Consider only positive market values
                if (marketValue > 0.0) {
                    totalMarketValue += marketValue;
                    count++;
                }
            } catch (Exception e) {
                // skip malformed entries and continue
                continue;
            }
            
        }

        if (count == 0) {
            return 0.0; // avoid division by zero
        }

        return totalMarketValue / count;
    }
}