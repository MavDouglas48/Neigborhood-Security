package FinalProject;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Tip extends Post {
    String message;
    public Tip(String location, String message) {
        super(location);
        this.message = message;
    }
    @Override
    public void display() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        System.out.println("\n[TIP]");
        System.out.println("Location: " + getLocation());
        System.out.println("Tip: " + message);
        System.out.println("Created: " + getCreatedAt().format(formatter));
    }
    @Override
    public String serialize() {
        return "TIP|" + getLocation() + "|" + message + "|" + getCreatedAt();
    }
    public static Tip deserialize(String[] data) {
        if (data.length < 4) {
            System.out.println("Invalid tip data: " + Arrays.toString(data));
            return null;
        }

        Tip tip = new Tip(data[1], data[2]);
        tip.setCreatedAt(LocalDateTime.parse(data[3]));
        return tip;
    }
}
