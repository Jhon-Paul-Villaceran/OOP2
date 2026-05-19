import java.util.ArrayList;
import java.util.List;

public class ArchiveRepository {

    private List<StudySession> archivedSessions = new ArrayList<>();

    public void save(StudySession session) {
        archivedSessions.add(session);
    }

    public boolean remove(int sessionID) {
        for (int i = 0; i < archivedSessions.size(); i++) {
            if (archivedSessions.get(i).getSessionID() == sessionID) {
                archivedSessions.remove(i);
                return true;
            }
        }
        return false;
    }

    public StudySession findByID(int sessionID) {
        for (StudySession s : archivedSessions) {
            if (s.getSessionID() == sessionID) {
                return s;
            }
        }
        return null;
    }

    public List<StudySession> findByUserID(int userID) {
        List<StudySession> result = new ArrayList<>();
        for (StudySession s : archivedSessions) {
            if (s.getUser().getUserID() == userID) {
                result.add(s);
            }
        }
        return result;
    }
}
