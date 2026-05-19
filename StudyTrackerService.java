package com.studytracker.service;

import com.studytracker.model.Subject;
import com.studytracker.model.StudySession;
import com.studytracker.model.User;
import com.studytracker.repository.StudySessionRepository;
import com.studytracker.repository.SubjectRepository;
import com.studytracker.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Central service layer: handles all business logic for the Study Habit Tracker.
 */
public class StudyTrackerService {

    private final UserRepository userRepo;
    private final SubjectRepository subjectRepo;
    private final StudySessionRepository sessionRepo;

    public StudyTrackerService() {
        this.userRepo = new UserRepository();
        this.subjectRepo = new SubjectRepository();
        this.sessionRepo = new StudySessionRepository();
    }

    // ─────────────── USER ───────────────

    /**
     * Registers a new user.
     */
    public User registerUser(String name, String email, String password) {
        return userRepo.save(name, email, password);
    }

    /**
     * Authenticates a user and returns them if credentials match.
     */
    public Optional<User> login(String email, String password) {
        return userRepo.findByEmail(email)
                .filter(u -> u.login(email, password));
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // ─────────────── SUBJECT ───────────────

    /**
     * Adds a new subject to the system.
     */
    public Subject addSubject(String name) {
        return subjectRepo.save(name);
    }

    public Optional<Subject> findSubject(String name) {
        return subjectRepo.findByName(name);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepo.findAll();
    }

    public boolean deleteSubject(int subjectID) {
        return subjectRepo.delete(subjectID);
    }

    // ─────────────── STUDY SESSION ───────────────

    /**
     * Logs a new study session for the given user and subject.
     */
    public StudySession logSession(User user, String subjectName, LocalDate date, int durationMinutes, String notes) {
        Subject subject = subjectRepo.findByName(subjectName)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + subjectName));

        int id = sessionRepo.getNextID();
        StudySession session = user.createSession(id, date, durationMinutes, notes, subject);
        sessionRepo.save(session);
        return session;
    }

    /**
     * Updates an existing session by ID.
     */
    public boolean updateSession(int sessionID, LocalDate newDate, int newDuration, String newNotes) {
        Optional<StudySession> opt = sessionRepo.findByID(sessionID);
        opt.ifPresent(s -> s.updateSession(newDate, newDuration, newNotes));
        return opt.isPresent();
    }

    /**
     * Deletes a session by ID (removes from both repo and user's list).
     */
    public boolean deleteSession(int sessionID) {
        Optional<StudySession> opt = sessionRepo.findByID(sessionID);
        opt.ifPresent(StudySession::deleteSession);
        return sessionRepo.delete(sessionID);
    }

    /**
     * Adds a note log to an existing session.
     */
    public boolean addLog(int sessionID, String note) {
        Optional<StudySession> opt = sessionRepo.findByID(sessionID);
        opt.ifPresent(s -> s.addLog(note));
        return opt.isPresent();
    }

    public List<StudySession> getSessionsByUser(int userID) {
        return sessionRepo.findByUserID(userID);
    }

    public List<StudySession> getSessionsByDate(int userID, LocalDate date) {
        return sessionRepo.findByDate(userID, date);
    }

    public List<StudySession> getSessionsBySubject(int userID, String subjectName) {
        return sessionRepo.findBySubject(userID, subjectName);
    }

    // ─────────────── TRACKER / PROGRESS ───────────────

    /**
     * Displays progress and streak for the given user.
     */
    public void viewProgress(User user) {
        user.viewProgress();
    }
}
