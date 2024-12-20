import java.util.List;

public class Room {
    private String name;
    private int capacity;
    private List<String> department; // New field for department

    public Room(String name, int capacity, List<String> department) {
        this.name = name;
        this.capacity = capacity;
        this.department = department;
    }

   // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter for capacity
    public int getCapacity() {
        return capacity;
    }

    // Setter for capacity
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // Getter for department
    public List<String> getDepartment() {
        return department;
    }

    public void setDepartment(List<String> department) {
        this.department = department;
    }

    // Overriding toString() for better output
    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", department=" + department + 
                '}';
    }
}
