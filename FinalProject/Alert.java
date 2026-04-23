package FinalProject;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Alert extends Post {
    protected String description;
    protected LocalDateTime endTime;
    protected AlertSeverity severity;
    boolean resolved = false;
    public Alert(String location, String description, int duration, AlertSeverity severity) {
        super(location);
        this.description = description;
        this.endTime = LocalDateTime.now().plusHours(duration);
        this.severity = severity;
    }
    public void markAsResolved() {
        resolved = true;
    }
    @Override
    public void display() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        System.out.println("\n[ALERT" + (resolved ? " - RESOLVED" : "") + "]");
        System.out.println("Location: " + getLocation());
        System.out.println("Description: " + description);
        System.out.println("Severity: " + severity);
        System.out.println("Ends: " + endTime.format(formatter));
        System.out.println("Created: " + getCreatedAt().format(formatter));
    }
    @Override
    public String serialize() {
        return "ALERT|" + getLocation() + "|" + description + "|" + endTime + "|" + severity + "|" + resolved + "|" + getCreatedAt();
    }
    public static Alert deserialize(String[] data) {
        if (data.length < 7) {
            System.out.println("Invalid alert data: " + Arrays.toString(data));
            return null;
        }

        Alert a = new Alert(data[1], data[2], 1, AlertSeverity.valueOf(data[4]));
        a.endTime = LocalDateTime.parse(data[3]);
        a.resolved = Boolean.parseBoolean(data[5]);
        a.setCreatedAt(LocalDateTime.parse(data[6]));
        return a;
    }
}
