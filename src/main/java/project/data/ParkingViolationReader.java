package project.data;
import java.util.List;
import project.common.ParkingViolation;
public interface ParkingViolationReader {
    List<ParkingViolation>  readParkingViolations(String path);
}
