package com.studytracker.repository;

import com.studytracker.model.StudySession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudySessionRepository {

    private List<StudySession> sessions = new ArrayList<>();
    private int nextID = 1;

    public int getNextID() { return nextID++; }

    public void save(StudySession session) {
        sessions.add(session);
    }

    public StudySession findByID(int id) {
        for (StudySession s : sessions) {
            if (s.getSessionID() == id) {
                return s;
            }
        }
        return null;
    }

    public boolean delete(int id) {
        StudySession toRemove = findByID(id);
        if (toRemove != null) {
            sessions.remove(toRemove);
            return true;
        }
        return false;
    }

    public List<StudySession> findByUserID(int userID) {
        List<StudySession> result = new ArrayList<>();
        for (StudySession s : sessions) {
            if (s.getUser().getUserID() == userID) {
                result.add(s);
            }
        }
        return result;
    }

    public List<StudySession> findByDate(int userID, LocalDate date) {
        List<StudySession> result = new ArrayList<>();
        for (StudySession s : sessions) {
            if (s.getUser().getUserID() == userID && s.getDate().equals(date)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<StudySession> findBySubject(int userID, String subjectName) {
        List<StudySession> result = new ArrayList<>();
        for (StudySession s : sessions) {
            if (s.getUser().getUserID() == userID &&
                s.getSubject().getSubjectName().equalsIgnoreCase(subjectName)) {
                result.add(s);
            }
        }
        return result;
    }
}
