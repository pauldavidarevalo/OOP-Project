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
    @Test
    public void parkingViolationsListIsNull(){
        ProjectData pd = new ProjectData(
                null,
                null,
                new HashMap<>()
        );
        FinesPerCapitaProcessor processor = new FinesPerCapitaProcessor(pd);
        TreeMap<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
    }
    @Test
    public void parkingViolationsListIsEmpty(){
        ProjectData pd = new ProjectData(
                new ArrayList<>(),
                null,
                null
        );
        FinesPerCapitaProcessor processor = new FinesPerCapitaProcessor(pd);
        TreeMap<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
    }
    @Test
    public void parkingViolationIsNull(){
        List<ParkingViolation> violations = new ArrayList<>();
        violations.add(null);
        ProjectData pd = new ProjectData(
                violations,
                null,
                new HashMap<>()
        );
        FinesPerCapitaProcessor processor = new FinesPerCapitaProcessor(pd);
        TreeMap<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
    }
    @Test
    public void zipPopulationIsNull(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                return null;
            }
        };
        FinesPerCapitaProcessor processor = new FinesPerCapitaProcessor(mockData);
        TreeMap<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
    }

    @Test
    public void zipPopulationIsEmpty(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                return new HashMap<>();
            }
        };
        FinesPerCapitaProcessor processor = new FinesPerCapitaProcessor(mockData);
        TreeMap<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
    }
    @Test
    public void zipCodeIsNull(){
        List<ParkingViolation> violations = new ArrayList<>();
        violations.add(new ParkingViolation("2023-01-01",
                100.0,
                "Test",
                "ABC123",
                "PA",
                "V001",
                null));
        ProjectData pd = new ProjectData(violations, null, new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                HashMap<String, Integer> pop = new HashMap<>();
                pop.put("19104", 1000);
                return pop;
            }
        };
        FinesPerCapitaProcessor processor = new FinesPerCapitaProcessor(pd);
        TreeMap<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
    }
    @Test
    public void zipCodeIsEmpty(){
        List<ParkingViolation> violations = new ArrayList<>();
        violations.add(new ParkingViolation("2023-01-01",
                100.0,
                "Test",
                "ABC123",
                "PA",
                "V001",
                ""));
        ProjectData pd = new ProjectData(violations, null, null) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                HashMap<String, Integer> pop = new HashMap<>();
                pop.put("19104", 1000);
                return pop;
            }
        };
        FinesPerCapitaProcessor processor = new FinesPerCapitaProcessor(pd);
        TreeMap<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
    }
    @Test
    public void zipNotFound(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = new PropertyValue(125000.0,
                        1750.0, "19103");
                PropertyValue pv2 = new PropertyValue(450000.0,
                        1800.0, "19105");
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                return propertyValues;
            }
        };
        FinesPerCapitaProcessor processor = new FinesPerCapitaProcessor(mockData);
        TreeMap<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
    }
    @Test
    public void stateIsNull(){
        List<ParkingViolation> violations = new ArrayList<>();
        violations.add(new ParkingViolation("2023-01-01",
                100.0,
                "Test",
                "ABC123",
                null,
                "V001",
                "19104"));
        ProjectData pd = new ProjectData(
                violations,
                null,
                null
        );
        FinesPerCapitaProcessor processor = new FinesPerCapitaProcessor(pd);
        TreeMap<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
    }
    @Test
    public void stateIsEmpty(){
        List<ParkingViolation> violations = new ArrayList<>();
        violations.add(new ParkingViolation("2023-01-01",
                100.0,
                "Test",
                "ABC123",
                "",
                "V001",
                "19104"));
        ProjectData pd = new ProjectData(
                violations,
                null,
                null
        );
        FinesPerCapitaProcessor processor = new FinesPerCapitaProcessor(pd);
        TreeMap<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
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

    //Skip zero population
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
