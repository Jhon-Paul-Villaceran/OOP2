import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory storage for reminders.
 */
public class ReminderRepository {
    private List<Reminder> reminders = new ArrayList<>();
    private int nextID = 1;

    public Reminder save(int userID, String subject, LocalDate date, LocalTime time, String message) {
        Reminder r = new Reminder(nextID++, userID, subject, date, time, message);
        reminders.add(r);
        return r;
    }

    public List<Reminder> findByUser(int userID) {
        List<Reminder> result = new ArrayList<>();
        for (Reminder r : reminders) {
            if (r.getUserID() == userID) {
                result.add(r);
            }
        }
        return result;
    }

    public boolean delete(int id) {
        for (int i = 0; i < reminders.size(); i++) {
            if (reminders.get(i).getId() == id) {
                reminders.remove(i);
                return true;
            }
        }
        return false;
    }
}
