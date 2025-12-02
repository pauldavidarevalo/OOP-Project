package project;

import project.common.*;
import org.junit.Test;
import project.processor.AverageMarketValueProcessor;

import java.util.*;
import static org.junit.Assert.*;

public class AverageMarketValueProcessorTest {

    @Test
    public void propertyValuesListIsNull(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                return null;
            }
        };

        AverageMarketValueProcessor processor = new AverageMarketValueProcessor(mockData);
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
        AverageMarketValueProcessor processor = new AverageMarketValueProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(125000, result);
    }

    // If market value is zero by default if its missing from the input file.
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
        };
        AverageMarketValueProcessor processor = new AverageMarketValueProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(0, result);
    }

    @Test
    public void propertyValueZipIsNull(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = new PropertyValue(280000.0,
                        1750.0, null);
                PropertyValue pv2 = new PropertyValue(340000.0,
                        1800.0, "19144");
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                return propertyValues;
            }
        };
        AverageMarketValueProcessor processor = new AverageMarketValueProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(340000, result);
    }

    @Test
    public void propertyValueZipNotInParameter(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = new PropertyValue(180000.0,
                        1750.0, "19144");
                PropertyValue pv2 = new PropertyValue(250000.0,
                        1800.0, "19150");
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                return propertyValues;
            }
        };
        AverageMarketValueProcessor processor = new AverageMarketValueProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(180000, result);
    }

    @Test
    public void multipleZipCodes(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = new PropertyValue(450000.0,
                        1750.0, "19144");
                PropertyValue pv2 = new PropertyValue(750000.0,
                        1800.0, "19144");
                PropertyValue pv3 = new PropertyValue(480000.0,
                        1750.0, "19140");
                PropertyValue pv4 = new PropertyValue(640000.0,
                        1800.0, "19148");
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                propertyValues.add(pv3);
                propertyValues.add(pv4);
                return propertyValues;
            }
        };
        AverageMarketValueProcessor processor = new AverageMarketValueProcessor(mockData);
        int result = processor.run("19144", "19140");
        assertEquals(560000, result);
    }

    @Test
    public void exceptionThrown(){

    }
}
