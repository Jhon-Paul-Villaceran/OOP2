import java.util.List;

public class ArchiveService {

    private ArchiveRepository archiveRepo;

    public ArchiveService() {
        this.archiveRepo = new ArchiveRepository();
    }

    public void archiveSession(StudySession session) {
        if (session.isArchived()) {
            System.out.println("Session #" + session.getSessionID() + " is already archived.");
            return;
        }
        session.archive();
        archiveRepo.save(session);
    }

    public boolean unarchiveSession(int sessionID) {
        StudySession session = archiveRepo.findByID(sessionID);
        if (session == null) {
            System.out.println("Session #" + sessionID + " not found in archive.");
            return false;
        }
        session.unarchive();
        archiveRepo.remove(sessionID);
        return true;
    }

    public List<StudySession> getArchivedSessions(int userID) {
        return archiveRepo.findByUserID(userID);
    }
}
