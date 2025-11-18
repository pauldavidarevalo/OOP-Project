package project.common;

import java.util.HashMap;
import java.util.List;

public class ProjectData {
    /** List of ParkingViolations
     *  List of PropertyValues
     *  HashMap mapping (String) Zip -> (Integer) Population
     *  get() methods to be used by processors
     */

    private List<ParkingViolation> parkingViolations;
    private List<PropertyValue> propertyValues;
    private HashMap<String, Integer> zipPopulation;

    public ProjectData(List<ParkingViolation> parkingViolations,
                       List<PropertyValue> propertyValues,
                       HashMap<String, Integer> zipPopulation) {

        this.parkingViolations = parkingViolations;
        this.propertyValues = propertyValues;
        this.zipPopulation = zipPopulation;
    }

    public List<ParkingViolation> getParkingViolations() {
        return parkingViolations;
    }

    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    public HashMap<String, Integer> getZipPopulation() {
        return zipPopulation;
    }

}
