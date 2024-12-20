import java.util.ArrayList;
import java.util.List;

public class Teacher {
    private String teacherId;
    private String department;
    private List<String> courses; // List of course IDs assigned to the teacher

    public Teacher(String teacherId, String department) {
        this.teacherId = teacherId;
        this.department = department;
        this.courses = new ArrayList<>();
    }

    // Getter and setter for id
    public String getId() {
        return teacherId;
    }

    public void setId(String teacherId) {
        this.teacherId = teacherId;
    }

    // Getter and setter for department
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // Getter and setter for courses
    public List<String> getCourses() {
        return courses;
    }

    public void addCourse(String courseId) {
        courses.add(courseId);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + teacherId +
                ", department='" + department + '\'' +
                ", courses=" + courses +
                '}';
    }
}