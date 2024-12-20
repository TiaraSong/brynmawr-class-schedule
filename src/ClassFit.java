import java.util.*;

class ClassFit {
    private String classId;
    private String department;
    private String timeSlot;
    private String roomName;
    private String teacherId;
    private int roomCapacity;
    private List<String> students;
    private List<String> studentPreferences;
    private double fitPercentage;
    private int enrollmentCount;

    // Constructor
    public ClassFit(String classId, int roomCapacity, int enrollmentCount, double fitPercentage) {
        this.classId = classId;
        this.roomCapacity = roomCapacity;
        this.enrollmentCount = enrollmentCount;
        this.fitPercentage = fitPercentage;
        this.students = new ArrayList<>();
        this.studentPreferences = new ArrayList<>();
    }

    // Getters and Setters
    public String getClassId() {
        return classId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public List<String> getStudentPreferences() {
        return studentPreferences;
    }

    public void setStudentPreferences(List<String> studentPreferences) {
        this.studentPreferences = studentPreferences;
    }

    public double getFitPercentage() {
        return fitPercentage;
    }

    public void setFitPercentage(double fitPercentage) {
        this.fitPercentage = fitPercentage;
    }

    public int getEnrollmentCount() {
        return enrollmentCount;
    }

    public void setEnrollmentCount(int enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
    }

    public int getPreferenceCount() {
        return studentPreferences.size();
    }

    @Override
    public String toString() {
        return "ClassFit{" +
                "classId='" + classId + '\'' +
                ", department='" + department + '\'' +
                ", timeSlot='" + timeSlot + '\'' +
                ", roomName='" + roomName + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", roomCapacity=" + roomCapacity +
                ", students=" + students +
                ", fitPercentage=" + fitPercentage +
                ", enrollmentCount=" + enrollmentCount +
                '}';
    }
}