import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * In-memory storage for StudySession objects.
 */
public class StudySessionRepository {

    private final List<StudySession> sessions = new ArrayList<>();
    private int nextID = 1;

    public int getNextID() { return nextID++; }

    public void save(StudySession session) {
        sessions.add(session);
    }

    public Optional<StudySession> findByID(int id) {
        return sessions.stream().filter(s -> s.getSessionID() == id).findFirst();
    }

    public List<StudySession> findByUserID(int userID) {
        return sessions.stream()
                .filter(s -> s.getUser().getUserID() == userID)
                .collect(Collectors.toList());
    }

    public List<StudySession> findByDate(int userID, LocalDate date) {
        return sessions.stream()
                .filter(s -> s.getUser().getUserID() == userID && s.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<StudySession> findBySubject(int userID, String subjectName) {
        return sessions.stream()
                .filter(s -> s.getUser().getUserID() == userID &&
                        s.getSubject().getSubjectName().equalsIgnoreCase(subjectName))
                .collect(Collectors.toList());
    }

    public boolean delete(int sessionID) {
        return sessions.removeIf(s -> s.getSessionID() == sessionID);
    }

    public List<StudySession> findAll() { return new ArrayList<>(sessions); }
}
