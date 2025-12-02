package project;

import project.common.*;
import org.junit.Test;
import project.processor.MarketValuePerCapitaProcessor;

import java.util.*;
import static org.junit.Assert.*;

public class MarketValuePerCapitaProcessorTest {

    @Test
    public void cacheHit() {
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = new PropertyValue(200000.0, 1750.0, "19144");
                PropertyValue pv2 = new PropertyValue(300000.0, 1800.0, "19144");
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                return propertyValues;
            }
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                HashMap<String, Integer> zipPop = new HashMap<>();
                zipPop.put("19144", 1000);
                return zipPop;
            }
        };
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        
        // First call: computes and caches
        int result1 = processor.run("19144");
        assertEquals(500, result1);
        
        // Second call: hits cache (should print "Cache hit for ZIP: 19144result 500")
        int result2 = processor.run("19144");
        assertEquals(500, result2);
    }

    @Test
    public void projectDataIsNull(){
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(null);
        int result = processor.run("19144");
        assertEquals(0, result);
    }

    @Test
    public void projectDataIsEmpty(){
        ProjectData emptyData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>());
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(emptyData);
        int result = processor.run("19144");
        assertEquals(0, result);
    }

    @Test
    public void propertyValuesListIsNull(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                return null;
            }
        };

        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(0, result);
    }

    @Test
    public void propertyValuesListIsEmpty(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                return new ArrayList<>();
            }
        };

        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(0, result);
    }

    @Test
    public void propertyValueIsNull(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = null;
                PropertyValue pv2 = new PropertyValue(125000.0,
                        1800.0, "19144");
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                return propertyValues;
            }
        };
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(0, result); // population is zero by default
    }

    @Test
    public void marketValueIsZero(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = new PropertyValue(0.0,
                        1750.0, "19144");
                PropertyValue pv2 = new PropertyValue(0.0,
                        1800.0, "19144");
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                return propertyValues;
            }
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                HashMap<String, Integer> zipPop = new HashMap<>();
                zipPop.put("19144", 1000);
                return zipPop;
            }
        };
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(0, result); // market value is zero
    }

    @Test
    public void marketValueIsNegative(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = new PropertyValue(-50000.0,
                        1750.0, "19144");
                PropertyValue pv2 = new PropertyValue(-120000.0,
                        1800.0, "19144");
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                return propertyValues;
            }
        };
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(0, result); // market value is negative
    }

    @Test
    public void zipCodeIsNull(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = new PropertyValue(125000.0,
                        1750.0, null);
                PropertyValue pv2 = new PropertyValue(450000.0,
                        1800.0, null);
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                return propertyValues;
            }
        };
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(0, result); // ZIP is null
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
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(0, result); // ZIP not found
    }

    @Test
    public void zipPopulationIsNull(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                return null;
            }
        };
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        int result = processor.run("19144");
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
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(0, result);
    }

    @Test
    public void populationIsZero(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = new PropertyValue(125000.0,
                        1750.0, "19144");
                PropertyValue pv2 = new PropertyValue(450000.0,
                        1800.0, "19144");
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                return propertyValues;
            }
        };
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(0, result); // population is zero by default
    }

    @Test
    public void validData_1(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = new PropertyValue(200000.0,
                        1750.0, "19144");
                PropertyValue pv2 = new PropertyValue(300000.0,
                        1800.0, "19144");
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                return propertyValues;
            }
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                HashMap<String, Integer> zipPop = new HashMap<>();
                zipPop.put("19144", 1000);
                return zipPop;
            }
        };
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(500, result); // (200000 + 300000) / 1000 = 500
    }

    @Test
    public void validData_2(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = new PropertyValue(150000.0,
                        1750.0, "19144");
                PropertyValue pv2 = new PropertyValue(250000.0,
                        1800.0, "19144");
                PropertyValue pv3 = new PropertyValue(100000.0,
                        1600.0, "19144");
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                propertyValues.add(pv3);
                return propertyValues; 
            }
            @Override
            public HashMap<String, Integer> getZipPopulation() {
                HashMap<String, Integer> zipPop = new HashMap<>();
                zipPop.put("19144", 800);
                return zipPop; 
            }  
        };
        MarketValuePerCapitaProcessor processor = new MarketValuePerCapitaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(625, result); // (150000 + 250000 + 100000) / 800 = 625
    }

}