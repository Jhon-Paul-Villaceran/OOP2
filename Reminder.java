import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a study reminder.
 */
public class Reminder {
    private int id;
    private int userID;
    private String subject;
    private LocalDate date;
    private LocalTime time;
    private String message;

    public Reminder(int id, int userID, String subject, LocalDate date, LocalTime time, String message) {
        this.id = id;
        this.userID = userID;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Formats reminder as:
     * [id] MM/dd/yyyy h:mm AM/PM - subject: message
     */
    @Override
    public String toString() {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a");

        return "[" + id + "] " 
               + date.format(dateFmt) + " " 
               + time.format(timeFmt) 
               + " - " + subject + ": " + message;
    }
}
