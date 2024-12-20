import java.util.*;

public class Course {
    private String courseId;
    private String teacherId;
    private String department; // New field for department
    private List<String> classrooms; // New field for available classrooms
    private int studentsCount;
    private List<String> studentPreferences;

    public Course(String courseId, String teacherId, String department, List<String> classrooms) {
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.department = department;
        this.classrooms = classrooms;
        this.studentPreferences = new ArrayList<>();
        this.studentsCount = 0;
    }

    // Getter and setter for id
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    // Getter and setter for teacher
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacher) {
        this.teacherId = teacher;
    }

    // Getter and setter for department
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // Getter and setter for classrooms
    public List<String> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<String> classrooms) {
        this.classrooms = classrooms;
    }

    // Getter and setter for studentsCount
    public int getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(int studentsCount) {
        this.studentsCount = studentsCount;
    }

    // Getter and setter for studentPreferences
    public List<String> getStudentPreferences() {
        return studentPreferences;
    }

    public void setStudentPreferences(List<String> studentPreferences) {
        this.studentPreferences = studentPreferences;
        this.studentsCount = studentPreferences.size(); // Update count based on list size
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", teacherId=" + teacherId +
                ", department='" + department + '\'' +
                ", classrooms=" + classrooms +
                ", studentPreferences=" + studentPreferences +
                '}';
    }
}