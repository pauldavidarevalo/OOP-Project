package project;

import project.common.*;
import org.junit.Test;

import project.processor.TopNZipCodeByFinesProcessor;

import java.util.*;
import static org.junit.Assert.*;

public class TopNZipCodeByFinesProcessorTest {

    @Test
    public void projectDataIsNull(){
        ProjectData pd = new ProjectData(
                null,
                null,
                null
        );
        TopNZipCodeByFinesProcessor processor = new TopNZipCodeByFinesProcessor(pd);
        List<Map.Entry<String, Double>> result = processor.run(5);
        assertTrue(result.isEmpty());
    }

    @Test
    public void projectDataIsEmpty(){
        ProjectData pd = new ProjectData(
                new ArrayList<>(),
                new ArrayList<>(),
                new HashMap<>()
        );
        TopNZipCodeByFinesProcessor processor = new TopNZipCodeByFinesProcessor(pd);
        List<Map.Entry<String, Double>> result = processor.run(5);
        assertTrue(result.isEmpty());
    }

    @Test
    public void parkingViolationsListIsNull(){
        ProjectData pd = new ProjectData(
                null,
                null,
                null
        );
        TopNZipCodeByFinesProcessor processor = new TopNZipCodeByFinesProcessor(pd);
        List<Map.Entry<String, Double>> result = processor.run(5);
        assertTrue(result.isEmpty());
    }

    @Test
    public void parkingViolationsListIsEmpty(){
        ProjectData pd = new ProjectData(
                new ArrayList<>(),
                null,
                null
        );
        TopNZipCodeByFinesProcessor processor = new TopNZipCodeByFinesProcessor(pd);
        List<Map.Entry<String, Double>> result = processor.run(5);
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
        TopNZipCodeByFinesProcessor processor = new TopNZipCodeByFinesProcessor(pd);
        List<Map.Entry<String, Double>> result = processor.run(5);
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
        TopNZipCodeByFinesProcessor processor = new TopNZipCodeByFinesProcessor(pd);
        List<Map.Entry<String, Double>> result = processor.run(5);
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
        TopNZipCodeByFinesProcessor processor = new TopNZipCodeByFinesProcessor(pd);
        List<Map.Entry<String, Double>> result = processor.run(5);
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
        ProjectData pd = new ProjectData(
                violations,
                null,
                null
        );
        TopNZipCodeByFinesProcessor processor = new TopNZipCodeByFinesProcessor(pd);
        List<Map.Entry<String, Double>> result = processor.run(5);
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
        ProjectData pd = new ProjectData(
                violations,
                null,
                null
        );
        TopNZipCodeByFinesProcessor processor = new TopNZipCodeByFinesProcessor(pd);
        List<Map.Entry<String, Double>> result = processor.run(5);
        assertTrue(result.isEmpty());
    }

    @Test
    public void validData_1(){
        List<ParkingViolation> violations = new ArrayList<>();
        violations.add(new ParkingViolation("2023-01-01", 
                                            100.0, 
                                            "Test1", 
                                            "ABC123", 
                                            "PA", 
                                            "V001", 
                                            "19104"));
        violations.add(new ParkingViolation("2023-01-02", 
                                            200.0, 
                                            "Test2", 
                                            "DEF456", 
                                            "PA", 
                                            "V002", 
                                            "19104"));
        violations.add(new ParkingViolation("2023-01-03", 
                                            150.0, 
                                            "Test3", 
                                            "GHI789", 
                                            "PA", 
                                            "V003", 
                                            "19105"));
        violations.add(new ParkingViolation("2023-01-04", 
                                            50.0, 
                                            "Test4", 
                                            "JKL012", 
                                            "NY", 
                                            "V004", 
                                            "19104")); // Different state, should be ignored
        ProjectData pd = new ProjectData(
                violations,
                null,
                null
        );
        TopNZipCodeByFinesProcessor processor = new TopNZipCodeByFinesProcessor(pd);
        List<Map.Entry<String, Double>> result = processor.run(2);
        assertEquals(2, result.size());
        assertEquals("19104", result.get(0).getKey());
        assertEquals(300.0, result.get(0).getValue(), 0.001);
        assertEquals("19105", result.get(1).getKey());
        assertEquals(150.0, result.get(1).getValue(), 0.001);
    }

    @Test
    public void validData_2(){
        List<ParkingViolation> violations = new ArrayList<>();
        violations.add(new ParkingViolation("2023-01-01", 
                                            120.0, 
                                            "Test1", 
                                            "ABC123", 
                                            "PA", 
                                            "V001", 
                                            "19106"));
        violations.add(new ParkingViolation("2023-01-02", 
                                            80.0, 
                                            "Test2", 
                                            "DEF456", 
                                            "PA", 
                                            "V002", 
                                            "19107"));
        violations.add(new ParkingViolation("2023-01-03", 
                                            200.0, 
                                            "Test3", 
                                            "GHI789", 
                                            "PA", 
                                            "V003", 
                                            "19106"));
        ProjectData pd = new ProjectData(
                violations,
                null,
                null
        );
        TopNZipCodeByFinesProcessor processor = new TopNZipCodeByFinesProcessor(pd);
        List<Map.Entry<String, Double>> result = processor.run(1);
        assertEquals(1, result.size());
        assertEquals("19106", result.get(0).getKey());
        assertEquals(320.0, result.get(0).getValue(), 0.001);
    }
}