import java.util.List;

public class Student {
    private String studentId;
    private List<String> coursePreferences;

    public Student(String studentId, List<String> prefs) {
        this.studentId = studentId;
        this.coursePreferences = prefs;
    }

    // Getter and setter for id
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    // Getter and setter for coursePreferences
    public List<String> getCoursePreferences() {
        return coursePreferences;
    }

    public void setCoursePreferences(List<String> coursePreferences) {
        this.coursePreferences = coursePreferences;
    }

    // Overriding toString method to customize output
    @Override
    public String toString() {
        return String.format("Student{id='%s', coursePreferences=%s}", studentId, coursePreferences);
    }
}