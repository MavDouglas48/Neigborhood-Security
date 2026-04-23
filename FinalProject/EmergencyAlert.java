package FinalProject;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EmergencyAlert extends Alert {
    private String emergencyType;
    public EmergencyAlert(String location, String desc, int duration, AlertSeverity severity, String type) {
        super(location, desc, duration, severity);
        this.emergencyType = type;
    }
    @Override
    public void display() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        System.out.println("\n[EMERGENCY ALERT]");
        System.out.println("Location: " + getLocation());
        System.out.println("Description: " + description);
        System.out.println("Severity: " + severity);
        System.out.println("Ends: " + endTime.format(formatter));
        System.out.println("Type: " + emergencyType);
        System.out.println("Created: " + getCreatedAt().format(formatter));
    }
    @Override
    public String serialize() {
        return "EMERGENCY|" + getLocation() + "|" + description + "|" + endTime + "|" + severity + "|" + emergencyType + "|" + getCreatedAt();
    }
    public static EmergencyAlert deserialize(String[] data) {
        if (data.length < 7) {
            System.out.println("Invalid emergency alert data: " + Arrays.toString(data));
            return null;
        }

        EmergencyAlert e = new EmergencyAlert(
                data[1],
                data[2],
                1,
                AlertSeverity.valueOf(data[4]),
                data[5]
        );
        e.endTime = LocalDateTime.parse(data[3]);
        e.setCreatedAt(LocalDateTime.parse(data[6]));
        return e;
    }
}
