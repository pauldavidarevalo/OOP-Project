package project.common;

public class ParkingViolation {
    private final String issueDate;
    private final double fine;
    private final String description;
    private final String vehicleId;
    private final String state;
    private final String violationId;
    private final String zipCode;

    public ParkingViolation(String issueDate, double fine, String description,
                            String vehicleId, String state,
                            String violationId, String zipCode) {
        this.issueDate = issueDate;
        this.fine = fine;
        this.description = description;
        this.vehicleId = vehicleId;
        this.state = state;
        this.violationId = violationId;
        this.zipCode = zipCode;

    }

    public String getIssueDate() { return issueDate;}
    public double getFine() { return fine; }
    public String getDescription() { return description;}
    public String getVehicleId() { return vehicleId; }
    public String getState() { return state; }
    public String getViolationId() { return violationId; }
    public String getZipCode() { return zipCode; }


    @Override
    public String toString() {
        return "ParkingViolation{" +
                "issueDate='" + issueDate + '\'' +
                ", fine=" + fine +
                ", description='" + description + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", state='" + state + '\'' +
                ", violationId='" + violationId + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';

    }

}
