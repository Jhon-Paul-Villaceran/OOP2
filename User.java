package com.studytracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a registered user of the Study Habit Tracker system.
 */
public class User {

    private int userID;
    private String name;
    private String email;
    private String password;
    private Tracker tracker;
    private List<StudySession> sessions;

    public User(int userID, String name, String email, String password) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.sessions = new ArrayList<>();
        this.tracker = new Tracker(this);
    }

    /**
     * Registers a new user account.
     */
    public static User register(int userID, String name, String email, String password) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty.");
        if (email == null || !email.contains("@") || !email.toLowerCase().contains("gmail") || !email.toLowerCase().endsWith(".com")) {
            throw new IllegalArgumentException("Email must be a valid Gmail address.");
        }
        if (password == null || password.length() < 6) throw new IllegalArgumentException("Password must be at least 6 characters.");
        return new User(userID, name, email, password);
    }

    /**
     * Validates login credentials.
     */
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    /**
     * Creates a new study session and links it to the user.
     */
    public StudySession createSession(int sessionID, java.time.LocalDate date, int durationMinutes, String notes, Subject subject) {
        StudySession session = new StudySession(sessionID, date, durationMinutes, notes, subject, this);
        sessions.add(session);
        tracker.updateStats(durationMinutes, date);
        return session;
    }

    public void addLoadedSession(StudySession session) {
        sessions.add(session);
        if (!session.isArchived()) {
            tracker.updateStats(session.getDurationMinutes(), session.getDate());
        }
    }

    public void refreshTracker() {
        tracker.rebuildStats();
    }

    /**
     * Displays progress summary via the tracker.
     */
    public void viewProgress() {
        System.out.println("=== Progress for " + name + " ===");
        tracker.calculateProgress();
        tracker.viewStreak();
    }

    // Getters and Setters
    public int getUserID() { return userID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public String getPassword() { return password; }
    public Tracker getTracker() { return tracker; }
    public List<StudySession> getSessions() { return sessions; }

    @Override
    public String toString() {
        return "User{userID=" + userID + ", name='" + name + "', email='" + email + "'}";
    }
}
