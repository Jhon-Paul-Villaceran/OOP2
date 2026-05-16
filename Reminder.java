import java.time.LocalTime;

public class Reminder{
    private int id;
    private int userID;
    private String subject;
    private LocalTime time;
    private String message;
    
    public Reminder(int id, int userID, String subject, LocalTime time, String message){
        this.id = id;
        this.userID = userID;
        this.subject = subject;
        this.time = time;
        this.message = message;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
