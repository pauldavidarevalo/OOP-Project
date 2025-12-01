package project.processor;

import project.common.*;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

// Only needed for testing purposes
// ----------------------------------------------------------
// import java.util.ArrayList;
// import java.util.HashMap;
// ----------------------------------------------------------

public class AverageMarketValueProcessor {
    ProjectData projectData;

    public AverageMarketValueProcessor(ProjectData pd) {
        this.projectData = pd;
    }

    // Could make this just run() and have zip as constructor arg, but this is more flexible for testing
    public int run(String... zips) {
        double totalMarketValue = 0.0;
        int count = 0;

        List<PropertyValue> propertyValues = projectData.getPropertyValues();
        // Check for null list
        if(propertyValues == null){
            System.err.println("No property values available.");
            return 0;
        }

        Set<String> zipSet = new HashSet<>(Arrays.asList(zips));

        for (PropertyValue pv : propertyValues) {
            if(pv == null) continue;                 // guard null entries
            double marketValue = pv.getMarketValue();
            String pvZip = pv.getZipCode();
            // Check zip code match
            if (pvZip == null || !zipSet.contains(pvZip)) {
                continue;
            }
            // Consider only positive market values
            if (marketValue > 0.0) {
                totalMarketValue += marketValue;
                count++;
            }
        }

        if (count == 0) {
            return 0; // avoid division by zero
        }

        return (int) Math.round(totalMarketValue / count);
    }


    //  /**
    //  * Temporary main for quick testing. Constructs sample property values and
    //  * prints expected vs actual average market value for a ZIP.
    //  */
    // public static void main(String[] args) {
    //     List<PropertyValue> pvList = new ArrayList<>();
    //     pvList.add(new PropertyValue(250000.0, 1800.0, "19104")); // include
    //     pvList.add(new PropertyValue(0.0, 1200.0, "19104"));      // zero -> ignored
    //     pvList.add(new PropertyValue(150000.0, 1000.0, "19104")); // include
    //     pvList.add(new PropertyValue(200000.0, 1400.0, "19104")); // include
    //     pvList.add(new PropertyValue(100000.0, 900.0, "19105"));  // other zip
    //     pvList.add(null);                                           // null entry -> skipped
    //     pvList.add(new PropertyValue(175000.0, 1300.0, "19104")); // include
    //     pvList.add(new PropertyValue(220000.0, 1100.0, "19104")); // include
    //     pvList.add(new PropertyValue(300000.0, 1600.0, "191047777")); // long zip -> no match
    //     pvList.add(new PropertyValue(-50000.0, 1200.0, "19104")); // negative -> ignored

    //     ProjectData pd = new ProjectData(null, pvList, new HashMap<>());
    //     AverageMarketValueProcessor proc = new AverageMarketValueProcessor(pd);

    //     // Included market values for 19104: 250000,150000,200000,175000,220000 => sum=995000 count=5 => avg=199000
    //     int expected = 199000;
    //     int actual = proc.run("19104");
    //     System.out.println("Expected average market value for 19104: " + expected);
    //     System.out.println("Computed average market value for 19104: " + actual);

    //     // No-match case
    //     System.out.println("Average for ZIP 99999 (no matches): " + proc.run("99999"));
    // }

}