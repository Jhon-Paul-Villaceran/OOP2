package com.studytracker.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.studytracker.model.Subject;

/**
 * In-memory storage for Subject objects.
 */
public class SubjectRepository {

    private final List<Subject> subjects = new ArrayList<>();
    private int nextID = 1;

    public Subject save(String name) {
        if (findByName(name).isPresent())
            throw new IllegalArgumentException("Subject already exists: " + name);
        Subject subject = new Subject(nextID++, name);
        subjects.add(subject);
        return subject;
    }

    public Optional<Subject> findByName(String name) {
        return subjects.stream()
                .filter(s -> s.getSubjectName().equalsIgnoreCase(name))
                .findFirst();
    }

    public Optional<Subject> findByID(int id) {
        return subjects.stream().filter(s -> s.getSubjectID() == id).findFirst();
    }

    public List<Subject> findAll() { return new ArrayList<>(subjects); }

    public void addLoadedSubject(Subject subject) {
        subjects.add(subject);
    }

    public void setNextID(int nextID) {
        this.nextID = nextID;
    }

    public boolean delete(int subjectID) {
        return subjects.removeIf(s -> s.getSubjectID() == subjectID);
    }
}
