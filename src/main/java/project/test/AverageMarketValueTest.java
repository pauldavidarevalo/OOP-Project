package project.test;

import project.common.PropertyValue;
import project.common.ProjectData;
import project.processor.AverageMarketValueProcessor;
import java.util.*;

public class AverageMarketValueTest {
    public static void main(String[] args) {
        List<PropertyValue> pvList = new ArrayList<>();

        // Build test data with easy-to-calculate numbers (multiples of 5/10, < 1000)
        // ZIP 19104 entries
        pvList.add(new PropertyValue(100.0, 1500.0, "19104")); // include
        pvList.add(new PropertyValue(200.0, 1600.0, "19104")); // include
        pvList.add(new PropertyValue(0.0, 1200.0, "19104"));   // zero -> ignored
        pvList.add(new PropertyValue(300.0, 1700.0, "19104")); // include
        pvList.add(new PropertyValue(-50.0, 1300.0, "19104")); // negative -> ignored
        pvList.add(new PropertyValue(150.0, 1400.0, "19104")); // include

        // ZIP 19105 entries
        pvList.add(new PropertyValue(50.0, 1000.0, "19105"));  // include
        pvList.add(new PropertyValue(100.0, 1100.0, "19105")); // include
        pvList.add(new PropertyValue(0.0, 900.0, "19105"));    // zero -> ignored

        // ZIP 19106 entries (for multi-ZIP test)
        pvList.add(new PropertyValue(250.0, 1800.0, "19106")); // include
        pvList.add(new PropertyValue(200.0, 1700.0, "19106")); // include

        // Other entries (should be skipped)
        pvList.add(null);                                        // null entry -> skipped
        pvList.add(new PropertyValue(500.0, 1000.0, null));     // null zip -> skipped
        pvList.add(new PropertyValue(400.0, 1200.0, "99999")); // other zip -> ignored

        ProjectData pd = new ProjectData(null, pvList, null);
        AverageMarketValueProcessor proc = new AverageMarketValueProcessor(pd);

        System.out.println("===== SINGLE ZIP TEST =====");
        // Test single ZIP: 19104
        // Included market values: 100, 200, 300, 150
        // Sum = 750 ; count = 4 ; expected average = 750 / 4 = 187.5 -> rounds to 188
        int expectedSingle = 188;
        int actualSingle = proc.run("19104");
        System.out.println("Expected average for ZIP 19104: " + expectedSingle);
        System.out.println("Computed average for ZIP 19104: " + actualSingle);
        System.out.println();

        System.out.println("===== MULTI-ZIP TEST =====");
        // Test multiple ZIPs: 19104 and 19106
        // 19104 included: 100, 200, 300, 150 (sum = 750)
        // 19106 included: 250, 200 (sum = 450)
        // Total sum = 1200 ; total count = 6 ; expected average = 1200 / 6 = 200
        int expectedMulti = 200;
        int actualMulti = proc.run("19104", "19106");
        System.out.println("Expected average for ZIPs 19104, 19106: " + expectedMulti);
        System.out.println("Computed average for ZIPs 19104, 19106: " + actualMulti);
        System.out.println();

        System.out.println("===== NO-MATCH TEST =====");
        // Test ZIP with no matches
        int expectedNoMatch = 0;
        int actualNoMatch = proc.run("77777");
        System.out.println("Expected average for ZIP 77777 (no matches): " + expectedNoMatch);
        System.out.println("Computed average for ZIP 77777: " + actualNoMatch);
    }
}
