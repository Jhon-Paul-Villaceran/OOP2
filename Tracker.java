import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracks study statistics, progress, and streaks for a User.
 */
public class Tracker {

    private final User user;
    private int totalStudyTime;      // in minutes
    private int streakCount;
    private LocalDate lastStudyDate;

    public Tracker(User user) {
        this.user = user;
        this.totalStudyTime = 0;
        this.streakCount = 0;
        this.lastStudyDate = null;
    }

    /**
     * Updates stats when a new study session is created.
     */
    public void updateStats(int durationMinutes, LocalDate date) {
        this.totalStudyTime += durationMinutes;

        if (date == null) return;

        if (lastStudyDate == null) {
            streakCount = 1;
        } else {
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(lastStudyDate, date);
            
            if (daysBetween == 1) {
                streakCount++;
            } else if (daysBetween > 1) {
                streakCount = 1; // reset streak
            }
            // if same day, do nothing (streak stays)
        }

        this.lastStudyDate = date;
    }

    /**
     * Calculates and displays overall progress.
     */
    public void calculateProgress() {
        System.out.println("Total Study Time: " + totalStudyTime + " minutes");
        System.out.println("Average per session: " + 
            (user.getSessions().isEmpty() ? 0 : totalStudyTime / user.getSessions().size()) + " minutes");
        
        if (totalStudyTime >= 1000) {
            System.out.println("🏆 Excellent progress! Keep it up!");
        } else if (totalStudyTime >= 500) {
            System.out.println("Good progress!");
        } else {
            System.out.println("Keep studying consistently.");
        }
    }

    /**
     * Displays current streak.
     */
    public void viewStreak() {
        System.out.println("Current Streak: " + streakCount + " day(s) 🔥");
        if (streakCount >= 7) {
            System.out.println("Amazing! You're on a roll!");
        }
    }

    // Getters
    public int getTotalStudyTime() {
        return totalStudyTime;
    }

    public int getStreakCount() {
        return streakCount;
    }

    public LocalDate getLastStudyDate() {
        return lastStudyDate;
    }
}
