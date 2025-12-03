package project;

import project.common.*;
import org.junit.Test;
import project.processor.FinesPerCapitaProcessor;
import project.processor.TotalPopulationProcessor;

import java.util.*;
import static org.junit.Assert.*;


public class TotalPopulationProcessorTest {

    //helper to create a ProjectData with given zip populations
    private ProjectData createProjectDate(HashMap<String, Integer> zipPopulations) {
        return new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                return zipPopulations;
            }
        };
    }

    @Test
    public void zipPopulationIsNull(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                return null;
            }
        };
        TotalPopulationProcessor processor = new TotalPopulationProcessor(mockData);
        int result = processor.run();
        assertEquals(0, result);
    }

    @Test
    public void zipPopulationIsEmpty(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                return new HashMap<>();
            }
        };
        TotalPopulationProcessor processor = new TotalPopulationProcessor(mockData);
        int result = processor.run();
        assertEquals(0, result);
    }
    @Test
    public void  zipPopulationValueIsNull() {
        HashMap<String, Integer> zipPop = new HashMap<>();
        zipPop.put("10001", null);
        zipPop.put("10003", 200);
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                return zipPop;
            }
        };
        TotalPopulationProcessor processor = new TotalPopulationProcessor(mockData);
        int result = processor.run();
        assertEquals(200, result);

    }

    @Test
    public void testRunWithMultipleZips() {
        HashMap<String, Integer> zipPop = new HashMap<>();
        zipPop.put("10001", 5000);
        zipPop.put("10002", 3000);
        zipPop.put("10003", 2000);

        ProjectData pd = createProjectDate(zipPop);
        TotalPopulationProcessor processor = new TotalPopulationProcessor(pd);

        int total = processor.run();
        assertEquals(10000, total);
    }

    @Test
    public void testRunWithEmptyMap() {
        ProjectData pd = createProjectDate(new HashMap<>());
        TotalPopulationProcessor processor = new TotalPopulationProcessor(pd);

        int total = processor.run();
        assertEquals(0, total);

    }

    @Test
    public void testRunWithSingleZip() {
        HashMap<String, Integer> zipPop = new HashMap<>();
        zipPop.put("12345", 7500);

        ProjectData pd = createProjectDate(zipPop);
        TotalPopulationProcessor processor = new TotalPopulationProcessor(pd);

        int total = processor.run();
        assertEquals(7500, total);
    }
}
