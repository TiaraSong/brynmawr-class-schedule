import java.io.*;
import java.util.*;

public class ClassFitCalculator {
    public static void main(String[] args) {
        String studentsOutputFile = "students_output.txt";
        String completeScheduleFile = "complete_schedule_with_students.txt";

        // Step 1: Read the student preferences for each class
        Map<String, Integer> classPreferences = readStudentPreferences(studentsOutputFile);

        // Step 2: Read the actual enrollments for each class
        Map<String, Integer> classEnrollments = readActualEnrollments(completeScheduleFile);

        // Step 3: Calculate the fit percentages for each class
        Map<String, Double> classFitPercentages = calculateFitPercentages(classPreferences, classEnrollments);

        // Step 4: Print and save the results
        printAndSaveClassFitPercentages(classPreferences, classEnrollments, classFitPercentages, "class_fit_percentages.txt");

        // Step 5: Calculate and print the overall fit percentage
        double overallFitPercentage = calculateOverallFitPercentage(classPreferences, classEnrollments);
        System.out.printf("Overall Fit Percentage: %.2f%%\n", overallFitPercentage);
    }

    // Step 1: Read student preferences from students_output.txt
    private static Map<String, Integer> readStudentPreferences(String filePath) {
        Map<String, Integer> classPreferencesCount = new HashMap<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
    
                int startIdx = line.indexOf("coursePreferences=[");
                if (startIdx != -1) {
                    String preferencesPart = line.substring(startIdx + "coursePreferences=[".length(), line.length() - 1);
                    String[] classIds = preferencesPart.split(",\\s*");

                    for (String classId : classIds) {
                        classId = classId.replaceAll("[^\\d]", "");  // Remove non-numeric characters
                        if (!classId.isEmpty()) {
                            classPreferencesCount.put(classId, classPreferencesCount.getOrDefault(classId, 0) + 1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return classPreferencesCount;
    }    

    // Step 2: Read actual enrollments from complete_schedule_with_students.txt
    private static Map<String, Integer> readActualEnrollments(String filePath) {
        Map<String, Integer> classEnrollments = new HashMap<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the first line (header)
            br.readLine();
    
            while ((line = br.readLine()) != null) {
                line = line.trim();
    
                if (line.isEmpty()) {
                    continue;
                }
    
                String[] parts = line.split("\\t");
                if (parts.length >= 5) {
                    String classId = parts[2]; // Third column is the class ID
                    String studentsList = parts[4]; // Fifth column onwards is the student list
    
                    String[] enrolledStudents = studentsList.split(",");
                    int enrolledCount = enrolledStudents.length;
    
                    classEnrollments.put(classId, classEnrollments.getOrDefault(classId, 0) + enrolledCount);
                } else {
                    System.err.println("Malformed line in complete_schedule_with_students.txt: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return classEnrollments;
    }

    // Step 3: Calculate fit percentages
    private static Map<String, Double> calculateFitPercentages(Map<String, Integer> classPreferences, Map<String, Integer> classEnrollments) {
        Map<String, Double> fitPercentages = new HashMap<>();
    
        for (Map.Entry<String, Integer> entry : classPreferences.entrySet()) {
            String classId = entry.getKey();
            int preferredCount = entry.getValue();
            int enrolledCount = classEnrollments.getOrDefault(classId, 0);
    
            double fitPercentage = (preferredCount == 0) ? 0.0 : ((double) enrolledCount / preferredCount) * 100;
            fitPercentages.put(classId, fitPercentage);
        }
    
        return fitPercentages;
    }

    // Step 4: Print and save fit percentages along with preference count and enrollment count
    private static void printAndSaveClassFitPercentages(Map<String, Integer> classPreferences, Map<String, Integer> classEnrollments,
                                                        Map<String, Double> classFitPercentages, String outputFile) {
        // Sort the classes by fit percentage
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(classFitPercentages.entrySet());
        sortedEntries.sort((a, b) -> Double.compare(b.getValue(), a.getValue())); // Sort in descending order

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("Class ID\tPreference Count\tEnrollment Count\tFit Percentage\n");

            for (Map.Entry<String, Double> entry : sortedEntries) {
                String classId = entry.getKey();
                int preferredCount = classPreferences.getOrDefault(classId, 0);
                int enrolledCount = classEnrollments.getOrDefault(classId, 0);
                double fitPercentage = entry.getValue();

                String outputLine = String.format("%s\t%d\t%d\t%.2f%%", classId, preferredCount, enrolledCount, fitPercentage);
                System.out.println(outputLine);
                writer.write(outputLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Step 5: Calculate overall fit percentage
    private static double calculateOverallFitPercentage(Map<String, Integer> classPreferences, Map<String, Integer> classEnrollments) {
        int totalPreferred = 0;
        int totalEnrolled = 0;

        for (Map.Entry<String, Integer> entry : classPreferences.entrySet()) {
            String classId = entry.getKey();
            int preferredCount = entry.getValue();
            int enrolledCount = classEnrollments.getOrDefault(classId, 0);

            totalPreferred += preferredCount;
            totalEnrolled += Math.min(enrolledCount, preferredCount); // To avoid overcounting if enrolled exceeds preferences
        }

        return (totalPreferred == 0) ? 0.0 : ((double) totalEnrolled / totalPreferred) * 100;
    }
}
