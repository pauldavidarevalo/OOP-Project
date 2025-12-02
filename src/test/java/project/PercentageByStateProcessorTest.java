package project;

import project.common.*;
import org.junit.Test;

import project.processor.PercentageByStateProcessor;

import java.util.*;
import static org.junit.Assert.*;

public class PercentageByStateProcessorTest {

    @Test
    public void projectDataIsNull(){
        ProjectData pd = new ProjectData(
                null,
                null,
                null
        );
        PercentageByStateProcessor processor = new PercentageByStateProcessor(pd);
        Map<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
    }

    @Test
    public void projectDataIsEmpty(){
        ProjectData pd = new ProjectData(
                new ArrayList<>(),
                new ArrayList<>(),
                new HashMap<>()
        );
        PercentageByStateProcessor processor = new PercentageByStateProcessor(pd);
        Map<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
    }

    @Test
    public void parkingViolationsListIsNull(){
        ProjectData pd = new ProjectData(
                null,
                null,
                null
        );
        PercentageByStateProcessor processor = new PercentageByStateProcessor(pd);
        Map<String, Double> result = processor.run();
        assertTrue(result.isEmpty());   
    }

    @Test
    public void parkingViolationsListIsEmpty(){
        ProjectData pd = new ProjectData(
                new ArrayList<>(),
                null,
                null
        );
        PercentageByStateProcessor processor = new PercentageByStateProcessor(pd);
        Map<String, Double> result = processor.run();
        assertTrue(result.isEmpty());
    }

    @Test
    public void parkingViolationIsNull(){
        List<ParkingViolation> violations = new ArrayList<>();
        violations.add(null);
        ProjectData pd = new ProjectData(
                violations,
                null,
                null
        );
        PercentageByStateProcessor processor = new PercentageByStateProcessor(pd);
        Map<String, Double> result = processor.run();
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
        PercentageByStateProcessor processor = new PercentageByStateProcessor(pd);
        Map<String, Double> result = processor.run();
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
        PercentageByStateProcessor processor = new PercentageByStateProcessor(pd);
        Map<String, Double> result = processor.run();   
        assertTrue(result.isEmpty());
    }

    @Test
    public void validData_1(){
        List<ParkingViolation> violations = new ArrayList<>();
        violations.add(new ParkingViolation("2023-01-01", 100.0, "Test", "ABC123", "PA", "V001", "19104"));
        violations.add(new ParkingViolation("2023-01-02", 150.0, "Test2", "DEF456", "PA", "V002", "19105"));
        violations.add(new ParkingViolation("2023-01-03", 200.0, "Test3", "GHI789", "NJ", "V003", "07001"));
        violations.add(new ParkingViolation("2023-01-04", 250.0, "Test4", "JKL012", "PA", "V004", "19106"));
        violations.add(new ParkingViolation("2023-01-05", 300.0, "Test5", "MNO345", "NY", "V005", "10001"));
        ProjectData pd = new ProjectData(
                violations,
                null,
                null
        );
        PercentageByStateProcessor processor = new PercentageByStateProcessor(pd);
        Map<String, Double> result = processor.run();   
        assertEquals(3, result.size());
        assertEquals(60.0, result.get("PA"), 0.001);
        assertEquals(20.0, result.get("NJ"), 0.001);
        assertEquals(20.0, result.get("NY"), 0.001);    
    }

    @Test
    public void validData_2(){
        List<ParkingViolation> violations = new ArrayList<>();
        violations.add(new ParkingViolation("2023-01-01", 100.0, "Test", "ABC123", "CA", "V001", "90001"));
        violations.add(new ParkingViolation("2023-01-02", 150.0, "Test2", "DEF456", "CA", "V002", "90002"));
        violations.add(new ParkingViolation("2023-01-03", 200.0, "Test3", "GHI789", "TX", "V003", "73301"));
        violations.add(new ParkingViolation("2023-01-04", 250.0, "Test4", "JKL012", "FL", "V004", "32003"));
        violations.add(new ParkingViolation("2023-01-05", 300.0, "Test5", "MNO345", "CA", "V005", "90003"));
        violations.add(new ParkingViolation("2023-01-06", 350.0, "Test6", "PQR678", "TX", "V006", "73302"));
        ProjectData pd = new ProjectData(
                violations,
                null,
                null
        );
        PercentageByStateProcessor processor = new PercentageByStateProcessor(pd);
        Map<String, Double> result = processor.run();   
        assertEquals(3, result.size());
        assertEquals(50.0, result.get("CA"), 0.001);
        assertEquals(33.33, result.get("TX"), 0.01);
        assertEquals(16.67, result.get("FL"), 0.01);
    }
}