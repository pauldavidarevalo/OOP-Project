package project;

import project.common.*;
import org.junit.Test;
import project.processor.AverageLivableAreaProcessor;

import java.util.*;
import static org.junit.Assert.*;

public class AverageLivableAreaProcessorTest {
    @Test
    public void propertyValuesListIsNull(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                return null;
            }
        };

        AverageLivableAreaProcessor processor = new AverageLivableAreaProcessor(mockData);
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
        AverageLivableAreaProcessor processor = new AverageLivableAreaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(1800, result);
    }

    // If market value is zero by default if it's missing from the input file.
    @Test
    public void livableAreaIsZero(){
        ProjectData mockData = new ProjectData(new ArrayList<>(), new ArrayList<>(), new HashMap<>()) {
            @Override
            public List<PropertyValue> getPropertyValues() {
                List<PropertyValue> propertyValues = new ArrayList<>();
                PropertyValue pv1 = new PropertyValue(125000.0,
                        0.0, "19144");
                PropertyValue pv2 = new PropertyValue(450000.0,
                        0.0, "19144");
                propertyValues.add(pv1);
                propertyValues.add(pv2);
                return propertyValues;
            }
        };
        AverageLivableAreaProcessor processor = new AverageLivableAreaProcessor(mockData);
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
        AverageLivableAreaProcessor processor = new AverageLivableAreaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(1800, result);
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
        AverageLivableAreaProcessor processor = new AverageLivableAreaProcessor(mockData);
        int result = processor.run("19144");
        assertEquals(1750, result);
    }

}
