package project.processor;

import java.util.List;

import project.common.ProjectData;
import project.common.PropertyValue;

// Only needed for testing purposes
// ----------------------------------------------------------
// import java.util.ArrayList;
// import java.util.HashMap;
// ----------------------------------------------------------

public class AverageLivableAreaProcessor {
    ProjectData projectData;

    public AverageLivableAreaProcessor(ProjectData pd) {
        this.projectData = pd;
    }

    public int run(String zip) {
        double totalLivableArea = 0.0;
        int count = 0;

        List<PropertyValue> propertyValues = projectData.getPropertyValues();
        // Check for null list
        if(propertyValues == null){
            System.err.println("No property values available.");
            return 0;
        }

        for (PropertyValue pv : propertyValues) {
            if(pv == null) continue;                 // guard null entries
            double livableArea = pv.getTotalLivableArea();
            String pvZip = pv.getZipCode();
            // Check zip code match
            if (pvZip == null || !pvZip.equals(zip)) {
                continue;
            }
            // Consider only positive livable areas
            if (livableArea > 0.0) {
                totalLivableArea += livableArea;
                count++;
            }
        }

        if (count == 0) {
            return 0; // avoid division by zero
        }

        return (int) Math.round(totalLivableArea / count);
    }

    // /**
    //  * Temporary main for quick testing. Prints expected vs actual averages for given ZIPs.
    //  */
    // public static void main(String[] args) {
    //     List<PropertyValue> pvList = new ArrayList<>();

    //     // Add several entries for ZIP 19104 (some zeros/negatives should be ignored)
    //     pvList.add(new PropertyValue(250000.0, 1200.0, "19104"));
    //     pvList.add(new PropertyValue(150000.0, 1500.0, "19104"));
    //     pvList.add(new PropertyValue(90000.0, 0.0, "19104"));       // zero livable -> ignored
    //     pvList.add(new PropertyValue(-100.0, -800.0, "19104"));    // negative -> ignored
    //     pvList.add(new PropertyValue(200000.0, 1400.0, "19104"));

    //     // Entries for other ZIPs
    //     pvList.add(new PropertyValue(100000.0, 1000.0, "19105"));
    //     pvList.add(new PropertyValue(120000.0, 1100.0, "19106"));

    //     // Null and malformed entries
    //     pvList.add(null);
    //     pvList.add(new PropertyValue(130000.0, 0.0, null)); // null zip -> skipped

    //     project.common.ProjectData pd = new project.common.ProjectData(null, pvList, new HashMap<>());
    //     AverageLivableArea proc = new AverageLivableArea(pd);

    //     // For ZIP 19104, included livable areas: 1200, 1500, 1400 -> sum=4100 count=3 -> avg=1366.666.. -> rounded 1367
    //     int expected19104 = 1367;
    //     int actual19104 = proc.run("19104");
    //     System.out.println("Expected average livable area for 19104: " + expected19104);
    //     System.out.println("Computed average livable area for 19104: " + actual19104);

    //     // For a ZIP with no matches
    //     int expectedNoMatch = 0;
    //     int actualNoMatch = proc.run("99999");
    //     System.out.println("Expected average livable area for 99999: " + expectedNoMatch);
    //     System.out.println("Computed average livable area for 99999: " + actualNoMatch);
    // }
}