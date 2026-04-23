package FinalProject;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class NeighborhoodSafetySystem {
    static Scanner scanner = new Scanner(System.in);
    static List<Post> posts = new ArrayList<>();
    static final String FILE_NAME = "FinalProject/data.txt";
    public static void main(String[] args) {
        loadData();

        while (true) {
            System.out.println("\n==== Neighborhood Safety Menu ====");
            System.out.println("1. Enter Alert");
            System.out.println("2. View Alerts");
            System.out.println("3. View Resolved Alerts");
            System.out.println("4. Edit Alert");
            System.out.println("5. Mark Alert as Resolved");
            System.out.println("6. Enter Tip");
            System.out.println("7. View Tips");
            System.out.println("8. Search Posts");
            System.out.println("9. Exit");

            int choice = getInt();

            switch (choice) {
                case 1 -> createAlert();
                case 2 -> viewAlerts();
                case 3 -> viewResolvedAlerts();
                case 4 -> editAlert();
                case 5 -> markAlertAsResolved();
                case 6 -> createTip();
                case 7 -> viewTips();
                case 8 -> searchPosts();
                case 9-> {
                    saveData();
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    // ================= MENU ACTIONS =================
    static void createAlert() {
        System.out.print("Enter location: ");
        String loc = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Duration (hours): ");
        int duration = getInt();
        System.out.println("Severity: 1=LOW, 2=MEDIUM, 3=HIGH");
        int s = getInt();
        AlertSeverity sev = AlertSeverity.values()[s - 1];
        System.out.println("Is this an emergency? (y/n)");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            System.out.print("Enter emergency type: ");
            String type = scanner.nextLine();
            posts.add(new EmergencyAlert(loc, desc, duration, sev, type));
        } else {
            posts.add(new Alert(loc, desc, duration, sev));
        }
        System.out.println("Alert created.");
    }
    static void viewAlerts() {
        // Filter the list to only include unresolved alerts
        List<Alert> alerts = new ArrayList<>();
        for (Alert alert : getAlerts()) {
            if (!alert.resolved) { // Only include unresolved alerts
                alerts.add(alert);
            }
        }

        // Sort alerts by severity (HIGH > MEDIUM > LOW)
        alerts.sort(Comparator.comparing((Alert a) -> a.severity).reversed());

        // Check if there are no unresolved alerts
        if (alerts.isEmpty()) {
            System.out.println("\nNo unresolved alerts available to view.");
            return;
        }

        // Display all unresolved alerts
        System.out.println("\n==== Unresolved Alerts ====");
        for (Post p : alerts) {
            p.display();
        }
    }
    static void viewResolvedAlerts() {
        // Filter the list to only include resolved alerts
        List<Alert> resolvedAlerts = new ArrayList<>();
        for (Alert alert : getAlerts()) {
            if (alert.resolved) { // Only include resolved alerts
                resolvedAlerts.add(alert);
            }
        }

        // Check if there are no resolved alerts
        if (resolvedAlerts.isEmpty()) {
            System.out.println("\nNo resolved alerts available to view.");
            return;
        }

        // Display all resolved alerts
        System.out.println("\n==== Resolved Alerts ====");
        for (Alert alert : resolvedAlerts) {
            alert.display();
        }
    }
    static void editAlert() {
        List<Alert> alerts = getAlerts();

        if (alerts.isEmpty()) {
            System.out.println("No alerts available to edit.");
            return;
        }

        System.out.println("\nAvailable Alerts:");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        for (int i = 0; i < alerts.size(); i++) {
            Alert alert = alerts.get(i);
            System.out.println(i + ": [Location: " + alert.getLocation() + ", Description: " + alert.description +
                    ", Severity: " + alert.severity + ", Ends: " + alert.endTime.format(formatter) + "]");
        }

        System.out.print("\nEnter the number of the alert you want to edit: ");
        int index = getInt();

        if (index >= 0 && index < alerts.size()) {
            Alert alertToEdit = alerts.get(index);

            System.out.println("\nEditing Alert:");
            System.out.println("[Location: " + alertToEdit.getLocation() + ", Description: " + alertToEdit.description +
                    ", Severity: " + alertToEdit.severity + ", Ends: " + alertToEdit.endTime.format(formatter) + "]");

            System.out.print("Enter new description (leave blank to keep current): ");
            String newDescription = scanner.nextLine();
            if (!newDescription.isBlank()) {
                alertToEdit.description = newDescription;
            }

            System.out.print("Enter new duration in hours (leave blank to keep current): ");
            String newDurationInput = scanner.nextLine();
            if (!newDurationInput.isBlank()) {
                try {
                    int newDuration = Integer.parseInt(newDurationInput);
                    alertToEdit.endTime = LocalDateTime.now().plusHours(newDuration);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid duration. Keeping the current duration.");
                }
            }

            System.out.println("Alert updated successfully!");
        } else {
            System.out.println("Invalid selection. No alert was edited.");
        }
    }
    static List<Alert> getAlerts() {
        List<Alert> list = new ArrayList<>();
        for (Post p : posts) {
            if (p instanceof Alert) {
                list.add((Alert) p);
            }
        }
        return list;
    }
    static void createTip() {
        System.out.print("Location: ");
        String loc = scanner.nextLine();
        System.out.print("Tip: ");
        String tip = scanner.nextLine();
        posts.add(new Tip(loc, tip));
    }
    static void viewTips() {
        // Filter the list to only include tips
        List<Tip> tips = new ArrayList<>();
        for (Post p : posts) {
            if (p instanceof Tip) {
                tips.add((Tip) p);
            }
        }

        // Check if there are no tips
        if (tips.isEmpty()) {
            System.out.println("\nNo tips available to view.");
            return;
        }

        // Display all tips
        System.out.println("\n==== Tips ====");
        for (Tip tip : tips) {
            tip.display();
        }
    }
    static void searchPosts() {
        System.out.print("Enter location or keyword to search: ");
        String query = scanner.nextLine().toLowerCase();

        List<Post> results = new ArrayList<>();
        for (Post p : posts) {
            if (p == null) continue; // Skip null objects

            if (p.getLocation().toLowerCase().contains(query) ||
                (p instanceof Alert && ((Alert) p).description.toLowerCase().contains(query)) ||
                (p instanceof Tip && ((Tip) p).message.toLowerCase().contains(query))) {
                results.add(p);
            }
        }

        if (results.isEmpty()) {
            System.out.println("\nNo posts found matching your search.");
        } else {
            System.out.println("\n==== Search Results ====");
            for (Post p : results) {
                p.display();
            }
        }
    }
    static void markAlertAsResolved() {
        List<Alert> alerts = getAlerts();
        if (alerts.isEmpty()) {
            System.out.println("No alerts available to mark as resolved.");
            return;
        }
        System.out.println("\nAvailable Alerts:");
        for (int i = 0; i < alerts.size(); i++) {
            Alert alert = alerts.get(i);
            System.out.println(i + ": [Location: " + alert.getLocation() + ", Description: " + alert.description +
                    ", Severity: " + alert.severity + "]");
        }
        System.out.print("\nEnter the number of the alert to mark as resolved: ");
        int index = getInt();
        if (index >= 0 && index < alerts.size()) {
            Alert alertToResolve = alerts.get(index);
            alertToResolve.markAsResolved();
            System.out.println("Alert marked as resolved.");
        } else {
            System.out.println("Invalid selection.");
        }
    }
    // ================= FILE =================
    static void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Post p : posts) {
                pw.println(p.serialize()); // Serialize each Post object and write to file
            }
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data to file.");
        }
    }
    static void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

                // Deserialize based on the prefix
                Post post = null;
                switch (parts[0]) {
                    case "ALERT" -> post = Alert.deserialize(parts);
                    case "EMERGENCY" -> post = EmergencyAlert.deserialize(parts);
                    case "TIP" -> post = Tip.deserialize(parts);
                }

                if (post != null) { // Only add valid posts
                    posts.add(post);
                } else {
                    System.out.println("Skipped invalid data: " + Arrays.toString(parts));
                }
            }
            System.out.println("Data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading data from file.");
        }
    }
    // ================= UTIL =================
    static int getInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.print("Enter number: ");
            }
        }
    }
}
