package project.common;

public class ParkingViolation {
    private final String ticketNumber;
    private final String issueDate;
    private final String violationCode;
    private final double fine;
    private final String zipCode;

    public ParkingViolation(String ticketNumber, String issueDate,
                            String violationCode, double fine, String zipCode) {
        this.ticketNumber = ticketNumber;
        this.issueDate = issueDate;
        this.violationCode = violationCode;
        this.fine = fine;
        this.zipCode = zipCode;
    }

    public String getTicketNumber() { return ticketNumber;}
    public String getIssueDate() { return issueDate;}
    public String getViolationCode() { return violationCode;}
    public double getFine() { return fine; }
    public String getZipCode() { return zipCode; }

    @Override
    public String toString() {
        return "ParkingViolation{" +
                "ticketNumber='" + ticketNumber + '\'' +
                ", date='" + issueDate + '\'' +
                ", violationCode='" + violationCode + '\'' +
                ", fineAmount=" + fine +
                ", zip='" + zipCode + '\'' +
                '}';
    }

}
