package com.studytracker.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.studytracker.model.StudySession;
import com.studytracker.model.Subject;
import com.studytracker.model.User;
import com.studytracker.service.StudyTrackerService;

/**
 * Console-based user interface for the Study Habit Tracker.
 */
public class ConsoleUI {

    private final StudyTrackerService service;
    private final Scanner scanner;
    private User currentUser;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ConsoleUI(StudyTrackerService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     Study Habit Tracker System       ║");
        System.out.println("╚══════════════════════════════════════╝");

        // Seed some subjects on startup
        seedDefaultSubjects();

        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = showAuthMenu();
            } else {
                running = showMainMenu();
            }
        }
        System.out.println("\nGoodbye! Keep studying!");
        scanner.close();
    }

    // ─────────────── AUTH MENU ───────────────

    private boolean showAuthMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1": handleRegister(); break;
            case "2": handleLogin(); break;
            case "0": return false;
            default: System.out.println("Invalid option.");
        }
        return true;
    }

    private void handleRegister() {
        System.out.println("\n-- Register --");
        System.out.print("Name    : "); String name = scanner.nextLine().trim();
        System.out.print("Email   : "); String email = scanner.nextLine().trim();
        System.out.print("Password: "); String pass = scanner.nextLine().trim();
        try {
            User user = service.registerUser(name, email, pass);
            System.out.println("Registered successfully! Welcome, " + user.getName() + ".");
            currentUser = user;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleLogin() {
        System.out.println("\n-- Login --");
        System.out.print("Email   : "); String email = scanner.nextLine().trim();
        System.out.print("Password: "); String pass = scanner.nextLine().trim();
        Optional<User> user = service.login(email, pass);
        if (user.isPresent()) {
            currentUser = user.get();
            System.out.println("Welcome back, " + currentUser.getName() + "!");
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    // ─────────────── MAIN MENU ───────────────

    private boolean showMainMenu() {
        System.out.println("\n=== [" + currentUser.getName() + "] ===");
        System.out.println("1. Add study session");
        System.out.println("2. View my sessions");
        System.out.println("3. Update a session");
        System.out.println("4. Delete a session");
        System.out.println("5. Add log note to session");
        System.out.println("6. View progress & streak");
        System.out.println("7. Manage subjects");
        System.out.println("8. Search sessions");
        System.out.println("9. Archive sessions");
        System.out.println("10. Logout");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1": handleLogSession(); break;
            case "2": handleViewSessions(); break;
            case "3": handleUpdateSession(); break;
            case "4": handleDeleteSession(); break;
            case "5": handleAddLog(); break;
            case "6": service.viewProgress(currentUser); break;
            case "7": handleManageSubjects(); break;
            case "8": handleSearchSessions(); break;
            case "9": currentUser = null; System.out.println("Logged out."); break;
            case "0": return false;
            default: System.out.println("Invalid option.");
        }
        return true;
    }

    // ─────────────── SESSIONS ───────────────

    private void handleLogSession() {
        System.out.println("\n-- Log Study Session --");
        List<Subject> subjects = service.getAllSubjects();
        if (subjects.isEmpty()) {
            System.out.println("No subjects found. Please add a subject first.");
            return;
        }
        System.out.println("Available subjects:");
        subjects.forEach(s -> System.out.println("  " + s.getSubjectID() + ". " + s.getSubjectName()));
        System.out.print("Subject name: ");
        String subj = scanner.nextLine().trim();

        LocalDate date = readDate("Date (yyyy-MM-dd) [Enter for today]: ", LocalDate.now());
        System.out.print("Duration (minutes): ");
        int duration;
        try { duration = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Invalid duration."); return; }

        System.out.print("Notes (optional): ");
        String notes = scanner.nextLine().trim();

        try {
            StudySession session = service.logSession(currentUser, subj, date, duration, notes);
            System.out.println("Session logged: " + session);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleViewSessions() {
        List<StudySession> sessions = service.getSessionsByUser(currentUser.getUserID());
        System.out.println("\n-- My Sessions (" + sessions.size() + ") --");
        if (sessions.isEmpty()) { System.out.println("No sessions yet."); return; }
        sessions.forEach(s -> System.out.println("  " + s));
    }

    private void handleUpdateSession() {
        System.out.print("\nEnter Session ID to update: ");
        int id = readInt();
        if (id < 0) return;

        LocalDate newDate = readDate("New date (yyyy-MM-dd) [Enter to skip]: ", null);
        System.out.print("New duration in minutes (0 to skip): ");
        int dur = readInt();
        System.out.print("New notes (Enter to skip): ");
        String notes = scanner.nextLine().trim();

        boolean ok = service.updateSession(id, newDate, dur > 0 ? dur : 0, notes.isBlank() ? null : notes);
        System.out.println(ok ? "Session updated." : "Session not found.");
    }

    private void handleDeleteSession() {
        System.out.print("\nEnter Session ID to delete: ");
        int id = readInt();
        if (id < 0) return;
        boolean ok = service.deleteSession(id);
        System.out.println(ok ? "Session deleted." : "Session not found.");
    }

    private void handleAddLog() {
        System.out.print("\nEnter Session ID to add note: ");
        int id = readInt();
        if (id < 0) return;
        System.out.print("Note: ");
        String note = scanner.nextLine().trim();
        boolean ok = service.addLog(id, note);
        System.out.println(ok ? "Note added." : "Session not found.");
    }

    // ─────────────── SUBJECTS ───────────────

    private void handleManageSubjects() {
        System.out.println("\n-- Subjects --");
        System.out.println("1. View subjects");
        System.out.println("2. Add subject");
        System.out.println("3. Delete subject");
        System.out.print("Choose: ");
        String c = scanner.nextLine().trim();
        switch (c) {
            case "1":
                service.getAllSubjects().forEach(s -> System.out.println("  [" + s.getSubjectID() + "] " + s.getSubjectName()));
                break;
            case "2":
                System.out.print("Subject name: ");
                String name = scanner.nextLine().trim();
                try { Subject sub = service.addSubject(name); System.out.println("Added: " + sub); }
                catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
                break;
            case "3":
                System.out.print("Subject ID to delete: ");
                int did = readInt();
                System.out.println(service.deleteSubject(did) ? "Deleted." : "Not found.");
                break;
            default: System.out.println("Invalid.");
        }
    }

    // ─────────────── SEARCH ───────────────

    private void handleSearchSessions() {
        System.out.println("\n-- Search --");
        System.out.println("1. By date");
        System.out.println("2. By subject");
        System.out.print("Choose: ");
        String c = scanner.nextLine().trim();
        List<StudySession> results;
        if (c.equals("1")) {
            LocalDate d = readDate("Date (yyyy-MM-dd): ", null);
            if (d == null) return;
            results = service.getSessionsByDate(currentUser.getUserID(), d);
        } else if (c.equals("2")) {
            System.out.print("Subject name: "); String sname = scanner.nextLine().trim();
            results = service.getSessionsBySubject(currentUser.getUserID(), sname);
        } else { System.out.println("Invalid."); return; }

        if (results.isEmpty()) { System.out.println("No sessions found."); return; }
        results.forEach(s -> System.out.println("  " + s));
    }

    // ─────────────── HELPERS ───────────────

    private LocalDate readDate(String prompt, LocalDate fallback) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isBlank()) return fallback;
        try { return LocalDate.parse(input, DATE_FMT); }
        catch (DateTimeParseException e) { System.out.println("Invalid date. Using " + (fallback != null ? fallback : "null")); return fallback; }
    }

    private int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Invalid number."); return -1; }
    }

    private void seedDefaultSubjects() {
        String[] defaults = {"Mathematics", "Science", "English", "History", "Programming"};
        for (String s : defaults) {
            try { service.addSubject(s); } catch (Exception ignored) {}
        }
    }
}
