public class Subject {

    private int subjectID;
    private String subjectName;

    public Subject(int subjectID, String subjectName) {
        if (subjectName == null || subjectName.isBlank())
            throw new IllegalArgumentException("Subject name cannot be empty.");
        this.subjectID = subjectID;
        this.subjectName = subjectName.trim();
    }

    // Getters and Setters
    public int getSubjectID() { return subjectID; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    @Override
    public String toString() {
        return "Subject{subjectID=" + subjectID + ", name='" + subjectName + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject s = (Subject) o;
        return subjectID == s.subjectID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(subjectID);
    }
}
