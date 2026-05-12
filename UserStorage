import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory storage for User objects.
 */
public class UserRepository {

    private final List<User> users = new ArrayList<>();
    private int nextID = 1;

    public User save(String name, String email, String password) {
        if (findByEmail(email).isPresent())
            throw new IllegalArgumentException("Email already registered: " + email);
        User user = User.register(nextID++, name, email, password);
        users.add(user);
        return user;
    }

    public Optional<User> findByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public Optional<User> findByID(int id) {
        return users.stream().filter(u -> u.getUserID() == id).findFirst();
    }

    public List<User> findAll() { return new ArrayList<>(users); }

    public boolean delete(int userID) {
        return users.removeIf(u -> u.getUserID() == userID);
    }
