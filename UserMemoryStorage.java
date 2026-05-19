package com.studytracker.repository;

import com.studytracker.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private List<User> users = new ArrayList<>();
    private int nextID = 1;

    public User save(String name, String email, String password) {
        // check if email already used
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException("Email already registered: " + email);
            }
        }
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty.");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email.");
        if (password == null || password.length() < 6) throw new IllegalArgumentException("Password must be at least 6 characters.");

        User user = new User(nextID++, name, email, password);
        users.add(user);
        return user;
    }

    public User findByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public List<User> findAll() {
        return users;
    }
}
