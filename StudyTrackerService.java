import com.studytracker.model.ArchiveService;
import com.studytracker.model.Subject;
import com.studytracker.model.StudySession;
import com.studytracker.model.User;
import com.studytracker.repository.StudySessionRepository;
import com.studytracker.repository.SubjectRepository;
import com.studytracker.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Central service layer: handles all business logic for the Study Habit Tracker.
 */
public class StudyTrackerService {

    private final UserRepository userRepo;
    private final SubjectRepository subjectRepo;
    private final StudySessionRepository sessionRepo;
    private final ReminderRepository reminderRepo;

    public StudyTrackerService() {
        this.userRepo = new UserRepository();
        this.subjectRepo = new SubjectRepository();
        this.sessionRepo = new StudySessionRepository();
        this.reminderRepo = new ReminderRepository();
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

    // --- Archive ---

    public boolean archiveSession(int sessionID) {
        StudySession session = sessionRepo.findByID(sessionID);
        if (session == null) return false;
        archiveService.archiveSession(session);
        return true;
    }

    public boolean unarchiveSession(int sessionID) {
        return archiveService.unarchiveSession(sessionID);
    }

    public List<StudySession> getArchivedSessions(int userID) {
        return archiveService.getArchivedSessions(userID);
    }

    //---------------Reminder---------------
 
    /**
     * Creates a new reminder for a user.
     */
    public Reminder createReminder(int userID, String subject, LocalDate date, LocalTime time, String message) {
        return reminderRepo.save(userID, subject, date, time, message);
    }

    /**
     * Gets all reminders for one user.
     */
    public List<Reminder> getUserReminders(int userID) {
        return reminderRepo.findByUser(userID);
    }

    /**
     * Deletes a reminder by ID, only if it belongs to the specified user.
     */
    public boolean removeReminder(int reminderID, int userID) {
        return reminderRepo.delete(reminderID, userID);
    }

    /**
     * Displays reminders above the main menu.
     * Shows OVERDUE for past reminders and today if time passed.
     * Shows TODAY for today if time not yet passed.
     * Shows TOMORROW for exactly 1 day from now.
     * Shows IN X DAYS for any future reminder.
     */
    public void showReminderPopup(int userID) {
        List<Reminder> reminders = reminderRepo.findByUser(userID);
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        boolean found = false;

        for (Reminder r : reminders) {
            LocalDate reminderDate = r.getDate();
            String label = null;

            if (reminderDate.isBefore(today)) {
                label = "[OVERDUE]  ";
            } else if (reminderDate.equals(today)) {
                if (now.isAfter(r.getTime())) {
                    label = "[OVERDUE]  ";
                } else {
                    label = "[TODAY]    ";
                }
            } else {
                long daysUntil = ChronoUnit.DAYS.between(today, reminderDate);
                if (daysUntil == 1) {
                    label = "[TOMORROW] ";
                } else {
                    label = "[IN " + daysUntil + " DAYS] ";
                }
            }

            if (!found) {
                System.out.println("
+--------------------------------------+");
                System.out.println("|         REMINDER ALERT!              |");
                System.out.println("+--------------------------------------+");
                found = true;
            }

            System.out.println(label + r);
        }
}
