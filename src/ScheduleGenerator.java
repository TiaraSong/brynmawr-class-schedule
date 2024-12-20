import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ScheduleGenerator {
    public static List<Room> loadRooms(String constraintsFile) {
        List<Room> rooms = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(constraintsFile))) {
            String line;
            boolean roomSection = false;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Rooms")) {
                    roomSection = true;
                } else if (line.startsWith("Classes") || line.isEmpty()) {
                    roomSection = false;
                } else if (roomSection) {
                    String[] parts = line.split("\\s+");
                    String name = parts[0];
                    int capacity = Integer.parseInt(parts[1]);
                    List<String> departments = assignDepartmentsToRoom(name); // Add logic to map room name to
                                                                              // department
                    rooms.add(new Room(name, capacity, departments));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    private static List<String> assignDepartmentsToRoom(String roomName) {
        // Implement logic to assign department based on room name
        if (roomName.startsWith("PK")) {
            return Arrays.asList("BIOL", "CHEM", "PHYS", "GEOL", "MATH", "CMCS");
        } else if (roomName.startsWith("DAL")) {
            return Arrays.asList("POLS", "ENGL", "ECON", "PHIL", "ANTH", "EDUC", "SOCL",
                    "ARTW", "COML", "HIST", "CITY", "CNSE", "GERM", "INST", "ACSK");
        } else if (roomName.startsWith("TH")) {
            return Arrays.asList("PHIL", "ECON", "HART", "ARTW", "COML", "HIST", "FREN", "ITAL",
                    "SPAN", "ARCH", "CSTS", "CITY", "GNST", "RUSS", "ARTT", "ARCM",
                    "LATN", "EALC", "ANTH");
        } else if (roomName.startsWith("CARP")) {
            return Arrays.asList("ECON", "PHIL", "PSYC", "HART", "COML", "HIST", "ARCH", "GREK", "CITY");
        } else if (roomName.startsWith("BYC")) {
            return Arrays.asList("PSYC", "HIST", "ITAL", "GERM", "COML", "ARBR");
        } else if (roomName.startsWith("TAYB")) {
            return Arrays.asList("FREN");
        } else if (roomName.startsWith("TAYC")) {
            return Arrays.asList("SPAN");
        } else if (roomName.startsWith("TAYD")) {
            return Arrays.asList("POLS", "PHIL", "ANTH", "FREN", "GNST", "GERM");
        } else if (roomName.startsWith("TAYE")) {
            return Arrays.asList("PSYC", "SOCL", "FREN", "RUSS");
        } else if (roomName.startsWith("TAYF")) {
            return Arrays.asList("PSYC", "HIST", "ITAL", "GERM", "COML", "ARBR");
        } else if (roomName.startsWith("TAYG")) {
            return Arrays.asList("EDUC", "SOCL", "COML", "CSTS");
        } else if (roomName.startsWith("TAYSEM")) {
            return Arrays.asList("POLS", "ENVS", "CITY", "HEBR");
        } else if (roomName.startsWith("EHI")) {
            return Arrays.asList("ENGL", "ARTW", "HIST", "COML");
        } else if (roomName.startsWith("EHII")) {
            return Arrays.asList("ENGL", "ARTW");
        } else if (roomName.startsWith("EHIII")) {
            return Arrays.asList("ARTW");
        } else if (roomName.startsWith("EHLEC")) {
            return Arrays.asList("ENGL");
        } else if (roomName.startsWith("PEMSTD")) {
            return Arrays.asList("ARTD");
        } else if (roomName.startsWith("CAN205")) {
            return Arrays.asList("HART");
        } else if (roomName.startsWith("DEDANCE")) {
            return Arrays.asList("ARTD");
        } else if (roomName.startsWith("ROSTUD")) {
            return Arrays.asList("CITY");
        } else if (roomName.startsWith("GOB")) {
            return Arrays.asList("ARTD");
        } else if (roomName.startsWith("GOCOM")) {
            return Arrays.asList("ARTT");
        } else if (roomName.startsWith("GOHEP")) {
            return Arrays.asList("ARTT");
        } else if (roomName.startsWith("RCSEM")) {
            return Arrays.asList("ARTW", "RUSS");
        } else if (roomName.startsWith("RCCON")) {
            return Arrays.asList("GNST", "RUSS");
        }

        // Default to "General" if no mapping exists
        return Arrays.asList("General");
    }

    public static void writeRoomsToFile(List<Room> rooms, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (Room room : rooms) {
                writer.write(room.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing rooms to file: " + e.getMessage());
        }
    }

    public static Map<String, List<String>> loadCourses(String constraintsFile, List<Course> courses) {
        Map<String, List<String>> teacherToCourses = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(constraintsFile))) {
            String line;

            // Skip lines until "Classes" is encountered
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("Classes")) {
                    break;
                }
            }

            // Skip the next line ("Teachers")
            br.readLine();

            // Process each line for courses
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                // Split the line into parts
                String[] parts = line.split("\\s+");

                if (parts.length < 4) {
                    System.err.println("Malformed course line: " + line);
                    continue;
                }

                String courseId = parts[0]; // Preserve leading zeros
                String teacherId = parts[1];
                String department = parts[2];
                List<String> classrooms = Arrays.asList(parts).subList(3, parts.length);

                // Add the course
                Course course = new Course(courseId, teacherId, department, classrooms);
                courses.add(course);

                // Map the teacher to the course
                teacherToCourses.putIfAbsent(teacherId, new ArrayList<>());
                teacherToCourses.get(teacherId).add(courseId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teacherToCourses;
    }

    public static List<Student> loadStudents(String studentPrefsFile) {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(studentPrefsFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Students") || line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                if (parts.length < 2) {
                    System.err.println("Malformed student line: " + line);
                    continue;
                }

                String studentId = parts[0];
                List<String> preferences = new ArrayList<>();

                // Clean preferences by removing unnecessary characters
                for (int i = 1; i < parts.length && preferences.size() < 5; i++) {
                    String courseId = parts[i].replaceAll("[,\\s]+", ""); // Remove commas and whitespace
                    if (!courseId.isEmpty()) {
                        preferences.add(courseId);
                    }
                }

                students.add(new Student(studentId, preferences));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    public static List<Teacher> loadTeachers(String constraintsFile, Map<String, List<String>> teacherToCourses) {
        List<Teacher> teachers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(constraintsFile))) {
            String line;

            // Skip lines until "Classes" is encountered
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("Classes")) {
                    break;
                }
            }

            // Skip the next line ("Teachers")
            br.readLine();

            // Read each course line to extract teachers and their departments
            Set<String> processedTeacherIds = new HashSet<>();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split("\\s+");
                if (parts.length < 4) {
                    System.err.println("Malformed course line: " + line);
                    continue;
                }

                String teacherId = parts[1];
                String department = parts[2];

                // Add teacher only if not already added
                if (!processedTeacherIds.contains(teacherId)) {
                    Teacher teacher = new Teacher(teacherId, department);

                    // Assign courses to the teacher
                    List<String> coursesForTeacher = teacherToCourses.getOrDefault(teacherId, new ArrayList<>());
                    for (int i = 0; i < Math.min(4, coursesForTeacher.size()); i++) {
                        teacher.addCourse(coursesForTeacher.get(i));
                    }

                    teachers.add(teacher);
                    processedTeacherIds.add(teacherId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    public static int[][] generateConflictsMatrix(String coursesFile, String studentsFile, String outputFile) {
        Map<String, Integer> courseIndexMap = new HashMap<>();
        List<String> courseList = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        // Load courses
        try (BufferedReader br = new BufferedReader(new FileReader(coursesFile))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\s+");
                    String courseId = parts[0].replaceAll("[^\\d]", ""); // Remove unnecessary characters
                    courseIndexMap.put(courseId, index++);
                    courseList.add(courseId);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading courses file: " + e.getMessage());
            return null;
        }

        //int numOfcourses = courseIndexMap.size();
        //System.out.println("course: " + numOfcourses);

        // Debug: Print loaded courseIndexMap
        // System.out.println("Course Index Map: " + courseIndexMap);

        // Load students
        try (BufferedReader br = new BufferedReader(new FileReader(studentsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\s+");
                    String studentId = parts[0].replaceAll("[^\\d]", ""); // Remove unnecessary characters
                    List<String> preferences = new ArrayList<>();
                    for (int i = 1; i < parts.length; i++) {
                        String courseId = parts[i].replaceAll("[^\\d]", ""); // Remove unnecessary characters
                        if (!courseId.isEmpty()) {
                            preferences.add(courseId);
                        }
                    }
                    students.add(new Student(studentId, preferences));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading students file: " + e.getMessage());
            return null;
        }

        // Create the conflict matrix
        int numOfCourses = courseList.size();
        int[][] conflictsMatrix = new int[numOfCourses][numOfCourses];

        for (Student student : students) {
            List<String> preferredCourses = student.getCoursePreferences();

            for (int i = 0; i < preferredCourses.size(); i++) {
                for (int j = i + 1; j < preferredCourses.size(); j++) {
                    String courseA = preferredCourses.get(i);
                    String courseB = preferredCourses.get(j);

                    Integer indexA = courseIndexMap.get(courseA);
                    Integer indexB = courseIndexMap.get(courseB);

                    if (indexA != null && indexB != null) {
                        conflictsMatrix[indexA][indexB]++;
                        conflictsMatrix[indexB][indexA]++; // Symmetric matrix
                    } else {
                        System.err.println("Error: Course ID not found in index map. CourseA: " + courseA
                                + ", CourseB: " + courseB);
                    }
                }
            }
        }

        // Write conflict matrix to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("Conflict Matrix:\n");
            for (int[] row : conflictsMatrix) {
                for (int value : row) {
                    writer.write(value + "\t");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing conflict matrix to file: " + e.getMessage());
        }

        return conflictsMatrix;
    }

    public static List<TimeSlot> getTimeSlots(String filename) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean timeSlotSection = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Check when we are in the Class Times section
                if (line.startsWith("Class Times")) {
                    timeSlotSection = true;
                    continue;
                } else if (line.startsWith("Rooms") || line.isEmpty()) {
                    // Stop reading if we reach the "Rooms" section or an empty line
                    timeSlotSection = false;
                    break;
                }

                // Process only lines from the Class Times section
                if (timeSlotSection && !line.isEmpty()) {
                    String[] parts = line.split("\\s+", 4);
                    if (parts.length >= 4) {
                        int timeId = Integer.parseInt(parts[0].trim());
                        String startTime = parts[1].trim() + " " + parts[2].trim();
                        String rest = parts[3].trim();
                        String[] restParts = rest.split("\\s+", 2);
                        String endTime = restParts[0].trim() + " " + restParts[1].substring(0, 2); // Extract end time
                                                                                                   // with AM/PM
                        String days = restParts[1].substring(3).trim(); // Extract days

                        timeSlots.add(new TimeSlot(timeId, startTime, endTime, days));
                    } else {
                        System.err.println("Malformed time slot line: " + line);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading time slots file: " + e.getMessage());
        }

        return timeSlots;
    }

    public static Map<String, TimeSlot> generateClassSchedule(int[][] conflictsMatrix, List<Course> courses,
        List<Room> rooms, List<Teacher> teachers, List<TimeSlot> timeSlots) {
        int numOfCourses = courses.size();
        boolean[] scheduled = new boolean[numOfCourses];
        Map<String, TimeSlot> courseToTimeSlot = new HashMap<>(); // Mapping courseId -> TimeSlot

        // Sort courses by cumulative conflict to prioritize high-conflict classes
        List<Integer> courseIndices = new ArrayList<>();
        for (int i = 0; i < numOfCourses; i++) {
            courseIndices.add(i);
        }

        courseIndices.sort((a, b) -> {
            int conflictA = 0;
            int conflictB = 0;
            for (int i = 0; i < numOfCourses; i++) {
                conflictA += conflictsMatrix[a][i];
                conflictB += conflictsMatrix[b][i];
            }
            return Integer.compare(conflictB, conflictA); // Sort in descending order of conflicts
        });

        // Assign high-conflict classes to distinct time slots
        Set<Integer> usedTimeSlotIds = new HashSet<>();
        for (int index : courseIndices) {
            if (scheduled[index]) {
                continue;
            }

            Course course = courses.get(index);
            TimeSlot availableTimeSlot = findAvailableTimeSlot(timeSlots, usedTimeSlotIds);
            if (availableTimeSlot == null) {
                // System.err.println("Error: No available time slots for course: " +
                // course.getCourseId());
                continue;
            }

            usedTimeSlotIds.add(availableTimeSlot.getTimeId());
            courseToTimeSlot.put(course.getCourseId(), availableTimeSlot);
            scheduled[index] = true;
        }

        // Assign remaining classes based on popularity and room size
        for (int i = 0; i < numOfCourses; i++) {
            if (scheduled[i]) {
                continue;
            }

            Course course = courses.get(i);
            TimeSlot bestTimeSlot = null;
            int leastConflict = Integer.MAX_VALUE;

            for (TimeSlot timeSlot : timeSlots) {
                int conflictSum = 0;
                for (int j = 0; j < numOfCourses; j++) {
                    if (!scheduled[j])
                        continue;
                    TimeSlot scheduledTimeSlot = courseToTimeSlot.get(courses.get(j).getCourseId());
                    if (scheduledTimeSlot.getTimeId() == timeSlot.getTimeId()) {
                        conflictSum += conflictsMatrix[i][j];
                    }
                }

                if (conflictSum < leastConflict) {
                    bestTimeSlot = timeSlot;
                    leastConflict = conflictSum;
                }
            }

            if (bestTimeSlot == null) {
                System.err.println("Error: No suitable time slot found for course: " + course.getCourseId());
                continue;
            }

            courseToTimeSlot.put(course.getCourseId(), bestTimeSlot);
            scheduled[i] = true;
        }

        // Ensure rooms meet department tags and capacity
        assignRoomsToCourses(courseToTimeSlot, courses, rooms, teachers);

        return courseToTimeSlot;
    }

    public static void main(String[] args) {
        String constraintsFile = args[0];
        String studentPrefsFile = args[1];
    
        // Load data
        List<Room> rooms = loadRooms(constraintsFile);
        List<Course> courses = new ArrayList<>();
        Map<String, List<String>> teacherToCourses = loadCourses(constraintsFile, courses);
        List<Student> students = loadStudents(studentPrefsFile);
        List<Teacher> teachers = loadTeachers(constraintsFile, teacherToCourses);
        List<TimeSlot> timeSlots = getTimeSlots(constraintsFile); // 加载时间段
    
        // Generate combinations of TimeSlot and Room
        List<TimeSlotRoom> timeSlotRooms = generateTimeSlotRoomCombinations(timeSlots, rooms);
    
        // Write to files
        try {
            writeToTxt("rooms_output.txt", rooms);
            writeToTxt("courses_output.txt", courses);
            writeToTxt("students_output.txt", students);
            writeToTxt("teachers_output.txt", teachers);
            writeToTxt("timeslots_output.txt", timeSlots);
    
            int[][] conflictsMatrix = generateConflictsMatrix("courses_output.txt", "students_output.txt",
                    "conflicts_matrix.txt");
    
            if (conflictsMatrix != null) {
                // Use the new generateClassSchedule method to schedule courses with time slot and room combinations
                Map<String, TimeSlotRoom> schedule = generateClassSchedule(conflictsMatrix, courses, timeSlotRooms);
    
                // Assign students to courses after the schedule is generated
                assignStudentsToCourses(schedule, students, courses);
                
                // Write the schedule to a file
                writeCompleteScheduleToFile(schedule, courses, "complete_schedule_with_students.txt");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    

    private static TimeSlot findAvailableTimeSlot(List<TimeSlot> timeSlots, Set<Integer> usedTimeSlotIds) {
        for (TimeSlot timeSlot : timeSlots) {
            if (!usedTimeSlotIds.contains(timeSlot.getTimeId())) {
                return timeSlot;
            }
        }
        return null; // No available time slot
    }

    private static void assignRoomsToCourses(Map<String, TimeSlot> courseToTimeSlot, List<Course> courses,
            List<Room> rooms, List<Teacher> teachers) {
        Map<String, Room> courseToRoom = new HashMap<>();
        Set<String> teacherTimeAssignments = new HashSet<>(); // Keep track of teacher-time assignments

        for (Course course : courses) {
            String department = course.getDepartment();
            TimeSlot timeSlot = courseToTimeSlot.get(course.getCourseId());
            Room assignedRoom = null;

            // Find a suitable room for the course based on department and capacity
            for (Room room : rooms) {
                if (room.getDepartment().contains(department) && room.getCapacity() >= course.getStudentsCount()) {
                    String teacherTimeKey = course.getTeacherId() + "-" + timeSlot.getTimeId();
                    if (!teacherTimeAssignments.contains(teacherTimeKey)) {
                        assignedRoom = room;
                        teacherTimeAssignments.add(teacherTimeKey);
                        break;
                    }
                }
            }

            if (assignedRoom == null) {
                // System.err.println("Error: No suitable room found for course: " +
                // course.getCourseId());
            } else {
                courseToRoom.put(course.getCourseId(), assignedRoom);
                // System.out.println(
                // "Assigned Room: Course " + course.getCourseId() + " -> Room " +
                // assignedRoom.getName());
            }
        }

        // Write assigned rooms to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("assigned_rooms_output.txt"))) {
            writer.write("Assigned Rooms:\n");
            for (Map.Entry<String, Room> entry : courseToRoom.entrySet()) {
                writer.write("Course: " + entry.getKey() + " -> Room: " + entry.getValue().getName());
                writer.newLine();
            }
        } catch (IOException e) {
            // System.err.println("Error writing assigned rooms to file: " +
            // e.getMessage());
        }
    }
    
    public static void assignStudentsToCourses(Map<String, TimeSlotRoom> schedule, List<Student> students, List<Course> courses) {
        // Create a mapping for quick course lookup
        Map<String, Course> courseMap = new HashMap<>();
        for (Course course : courses) {
            courseMap.put(course.getCourseId(), course);
        }
    
        // Map to keep track of student time slot assignments
        Map<String, Set<Integer>> studentAssignedTimeSlots = new HashMap<>();
    
        for (Student student : students) {
            List<String> preferences = student.getCoursePreferences();
            Set<String> assignedCourses = new HashSet<>(); // Store assigned course IDs for each student
    
            // Iterate through student preferences and try to assign them to available courses
            for (String courseId : preferences) {
                if (schedule.containsKey(courseId) && !assignedCourses.contains(courseId)) {
                    // Get the course and time slot from the schedule
                    Course course = courseMap.get(courseId);
                    if (course == null) {
                        continue; // Course not found, skip
                    }
    
                    TimeSlotRoom timeSlotRoom = schedule.get(courseId);
                    int timeSlotId = timeSlotRoom.getTimeSlot().getTimeId();
    
                    // Get or create the set of assigned time slots for the student
                    studentAssignedTimeSlots.putIfAbsent(student.getStudentId(), new HashSet<>());
                    Set<Integer> assignedTimeSlots = studentAssignedTimeSlots.get(student.getStudentId());
    
                    // Check if the student is already assigned to a course at this time slot
                    if (assignedTimeSlots.contains(timeSlotId)) {
                        continue; // Student is already assigned to a course during this time slot, skip
                    }
    
                    // Assign the student to the course
                    course.getStudentPreferences().add(student.getStudentId());
                    course.setStudentsCount(course.getStudentsCount() + 1); // Update the student count for the course
                    assignedCourses.add(courseId);
    
                    // Mark this time slot as assigned for the student
                    assignedTimeSlots.add(timeSlotId);
                }
            }
        }
    }
    
    private static void writeCompleteScheduleToFile(Map<String, TimeSlotRoom> schedule, List<Course> courses, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Complete Class Schedule with Students:\n");
            writer.write("Time Slot\tRoom\tCourse\tTeacher\tStudents\n");
    
            // Create a course map for easier lookup
            Map<String, Course> courseMap = new HashMap<>();
            for (Course course : courses) {
                courseMap.put(course.getCourseId(), course);
            }
    
            for (Map.Entry<String, TimeSlotRoom> entry : schedule.entrySet()) {
                String courseId = entry.getKey();
                TimeSlotRoom timeSlotRoom = entry.getValue();
                TimeSlot timeSlot = timeSlotRoom.getTimeSlot();
                Room room = timeSlotRoom.getRoom();
    
                // Get the course, teacher ID, and student list
                Course course = courseMap.get(courseId);
                if (course == null) {
                    continue; // Skip if no course found
                }
    
                String teacherId = course.getTeacherId();
                List<String> studentIds = course.getStudentPreferences(); // List of student IDs assigned to the course
    
                // Format student IDs as a comma-separated string
                String students = studentIds.isEmpty() ? "" : String.join(", ", studentIds);
    
                // Write the details to the file
                writer.write(String.format("%s %s-%s\t%s\t%s\t%s\t%s\n",
                        timeSlot.getDays(),
                        timeSlot.getStartTime(),
                        timeSlot.getEndTime(),
                        room.getName(),
                        course.getCourseId(),
                        course.getTeacherId(),
                        String.join(", ", course.getStudentPreferences())));
            }
        }
    }
    

    // New method to convert schedule (Map) to a matrix representation for student
    // assignment
    private static Course[][] convertScheduleToMatrix(Map<String, TimeSlot> schedule, List<Course> courses,
            List<Room> rooms, List<TimeSlot> timeSlots) {
        Course[][] scheduleMatrix = new Course[timeSlots.size()][rooms.size()];

        // Create a mapping of course IDs to courses for easier lookup
        Map<String, Course> courseMap = new HashMap<>();
        for (Course course : courses) {
            courseMap.put(course.getCourseId(), course);
        }

        // Populate the schedule matrix using the generated schedule
        for (Map.Entry<String, TimeSlot> entry : schedule.entrySet()) {
            String courseId = entry.getKey();
            TimeSlot timeSlot = entry.getValue();
            Course course = courseMap.get(courseId);

            if (course != null) {
                int timeSlotIndex = timeSlot.getTimeId() - 1; // Assuming time slot IDs are 1-based
                for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
                    if (rooms.get(roomIndex).getDepartment().contains(course.getDepartment()) &&
                            rooms.get(roomIndex).getCapacity() >= course.getStudentsCount()) {
                        // Assign the course to this room and time slot
                        scheduleMatrix[timeSlotIndex][roomIndex] = course;
                        break;
                    }
                }
            }
        }

        return scheduleMatrix;
    }

    public static List<TimeSlotRoom> generateTimeSlotRoomCombinations(List<TimeSlot> timeSlots, List<Room> rooms) {
        List<TimeSlotRoom> combinations = new ArrayList<>();
        for (TimeSlot timeSlot : timeSlots) {
            for (Room room : rooms) {
                combinations.add(new TimeSlotRoom(timeSlot, room));
            }
        }
        return combinations;
    }
    
    public static Map<String, TimeSlotRoom> generateClassSchedule(int[][] conflictsMatrix, List<Course> courses, List<TimeSlotRoom> timeSlotRooms) {
        int numOfCourses = courses.size();
        boolean[] scheduled = new boolean[numOfCourses];
        Map<String, TimeSlotRoom> courseToTimeSlotRoom = new HashMap<>(); // Mapping courseId -> TimeSlotRoom
    
        // Sort courses by cumulative conflict to prioritize high-conflict classes
        List<Integer> courseIndices = new ArrayList<>();
        for (int i = 0; i < numOfCourses; i++) {
            courseIndices.add(i);
        }
    
        courseIndices.sort((a, b) -> {
            int conflictA = 0;
            int conflictB = 0;
            for (int i = 0; i < numOfCourses; i++) {
                conflictA += conflictsMatrix[a][i];
                conflictB += conflictsMatrix[b][i];
            }
            return Integer.compare(conflictB, conflictA); // Sort in descending order of conflicts
        });
    
        // Assign high-conflict classes to distinct time slots and rooms
        Set<TimeSlotRoom> usedTimeSlotRooms = new HashSet<>();
        for (int index : courseIndices) {
            if (scheduled[index]) {
                continue;
            }
    
            Course course = courses.get(index);
            TimeSlotRoom availableTimeSlotRoom = findAvailableTimeSlotRoom(timeSlotRooms, usedTimeSlotRooms, course);
            if (availableTimeSlotRoom == null) {
                System.err.println("Error: No available time slot and room for course: " + course.getCourseId());
                continue;
            }
    
            usedTimeSlotRooms.add(availableTimeSlotRoom);
            courseToTimeSlotRoom.put(course.getCourseId(), availableTimeSlotRoom);
            scheduled[index] = true;
        }
    
        return courseToTimeSlotRoom;
    }

    private static TimeSlotRoom findAvailableTimeSlotRoom(List<TimeSlotRoom> timeSlotRooms, Set<TimeSlotRoom> usedTimeSlotRooms, Course course) {
        for (TimeSlotRoom timeSlotRoom : timeSlotRooms) {
            // check if the classroom is already been used
            if (usedTimeSlotRooms.contains(timeSlotRoom)) {
                continue;
            }
            
            // check if the conpacity is large enough
            if (timeSlotRoom.getRoom().getCapacity() >= course.getStudentsCount()) {
                return timeSlotRoom;
            }
        }
        return null; // no avilable time slot and classroom
    }

    private static <T> void writeToTxt(String filename, List<T> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (T item : data) {
                writer.write(item.toString());
                writer.newLine();
            }
        }
    }
}
