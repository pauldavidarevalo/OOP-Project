package project;

import project.common.*;
import org.junit.Test;
import project.processor.FinesPerCapitaProcessor;

import java.util.*;
import static org.junit.Assert.*;

public class FinesPerCapitaProcessorTest {
    //helper for ParkingViolation creation
    private ParkingViolation pv(double fine, String state, String zip) {
        return new ParkingViolation(
                "2024-01-01",
                fine,
                "DESC",
                "VEH1",
                state,
                "VID1",
                zip
        );
    }
    // Basic valid calculation
    @Test
    public void testBasicCalculation() {

        //anonymous mock ProjectData
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<ParkingViolation> getParkingViolations() {
                List<ParkingViolation> list = new ArrayList<>();
                list.add(pv(50.0, "PA", "19104"));
                list.add(pv(150.0, "PA", "19104"));
                return list;
            }
            @Override
            public HashMap<String, Integer> getZipPopulation() {
               HashMap<String, Integer> pop = new HashMap<>();
               pop.put("19104", 1000);
               return pop;
            }
            @Override
            public List<PropertyValue> getPropertyValues() {
                return new ArrayList<>();
            }
        };

        FinesPerCapitaProcessor proc = new FinesPerCapitaProcessor(mockData);
        TreeMap<String, Double> result = proc.run();
        assertEquals(1, result.size());
        assertEquals(0.2000, result.get("19104"), 0.00001);
    }
    // skip non-PA
    @Test
    public void testSkipNonPA() {
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                HashMap<String, Integer> pop = new HashMap<>();
                pop.put("19104", 1000);
                return pop;
            }
            @Override
            public List<ParkingViolation> getParkingViolations() {
                return Arrays.asList(pv(500.0, "NY", "19104"));
            }
        };

        FinesPerCapitaProcessor proc = new FinesPerCapitaProcessor(mockData);
        TreeMap<String, Double> result = proc.run();
        assertTrue(result.isEmpty());
    }

    //skip empty zip
    @Test
    public void testSkipEmptyZip() {
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                return new HashMap<>();
            }
            @Override
            public List<ParkingViolation> getParkingViolations() {
                return Arrays.asList(pv(100.0, "PA", ""));
            }
        };

        FinesPerCapitaProcessor proc = new FinesPerCapitaProcessor(mockData);
        TreeMap<String, Double> result = proc.run();

        assertTrue(result.isEmpty());
    }

    //Skip zero populaiton
    @Test
    public void testSkipZeroPopulation() {
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                HashMap<String, Integer> pop = new HashMap<>();
                pop.put("19104", 0);
                return pop;
            }
            @Override
            public List<ParkingViolation> getParkingViolations() {
                return Arrays.asList(pv(100.0, "PA", "19104"));
            }
        };
        FinesPerCapitaProcessor proc = new FinesPerCapitaProcessor(mockData);
        TreeMap<String, Double> result = proc.run();

        assertTrue(result.isEmpty());
    }

    //Skip zero fines
    @Test
    public void testSkipZeroFines() {
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                HashMap<String, Integer> pop = new HashMap<>();
                pop.put("19104", 1000);
                return pop;
            }
            @Override
            public List<ParkingViolation> getParkingViolations() {
                return Arrays.asList(pv(0.0, "PA", "19104"));
            }
        };

        FinesPerCapitaProcessor proc = new FinesPerCapitaProcessor(mockData);
        TreeMap<String, Double> result = proc.run();

        assertTrue(result.isEmpty());
    }
    //treemap sorting
    @Test
    public void testSortedOutput() {
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                HashMap<String, Integer> pop = new HashMap<>();
                pop.put("19104", 1000);
                pop.put("20001", 2000);
                return pop;
            }

            @Override
            public List<ParkingViolation> getParkingViolations() {
                return Arrays.asList(
                        pv(200.0, "PA", "20001"),
                        pv(100.0, "PA", "19104")
                );
            }
        };

        FinesPerCapitaProcessor proc = new FinesPerCapitaProcessor(mockData);
        TreeMap<String, Double> result = proc.run();

        List<String> expected = Arrays.asList("19104", "20001");
        assertEquals(expected, new ArrayList<>(result.keySet()));
    }

}
