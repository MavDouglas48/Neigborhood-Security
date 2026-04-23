package FinalProject;

import java.time.LocalDateTime;

public abstract class Post {
    private String location;
    private LocalDateTime createdAt;

    public Post(String location) {
        this.location = location;
        this.createdAt = LocalDateTime.now();
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean matchesLocation(String loc) {
        return location.equalsIgnoreCase(loc);
    }

    public abstract void display();
    public abstract String serialize();
}
