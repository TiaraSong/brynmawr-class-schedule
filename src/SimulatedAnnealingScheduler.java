import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
    
public class SimulatedAnnealingScheduler {
    
        public static void main(String[] args) {
            String inputFile = "complete_schedule_with_students.txt";
            List<ClassFit> classFits = loadClassFits(inputFile);
    
            // Defensive check if class fit data is empty
            if (classFits.isEmpty()) {
                System.out.println("No class fit data found in the input file. Please check the file and try again.");
                return;
            }
    
            // Number of iterations for the simulated annealing process
            int iterations = 10;
    
            // Run the simulated annealing optimization process
            simulatedAnnealing(classFits, iterations);
        }
    
        private static void simulatedAnnealing(List<ClassFit> classFits, int iterations) {
            if (classFits.isEmpty()) {
                return;
            }
    
            // File to write the output
            String outputFile = "simulated_annealing_output.txt";
    
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                // Sort classes by fit percentage in ascending order (lowest fit percentage first)
                classFits.sort(Comparator.comparingDouble(ClassFit::getFitPercentage));
    
                for (int i = 0; i < iterations; i++) {
                    // Defensive check
                    if (classFits.isEmpty()) {
                        String message = "Class fits list is empty during iteration " + i;
                        writeOutputToFile(message, outputFile, writer);
                        break;
                    }
    
                    boolean swapped = false;
                    // Iterate from the lowest fit rate class upwards
                    for (int j = 0; j < classFits.size(); j++) {
                        ClassFit classA = classFits.get(j);
                        String classAttemptMessage = "Iteration " + i + ": Trying to improve fit for class - " + classA;
                        writeOutputToFile(classAttemptMessage, outputFile, writer);
    
                        // Get the department for the class
                        String departmentA = classA.getDepartment();
    
                        // Find all other classes in the same department
                        List<ClassFit> sameDepartmentClasses = new ArrayList<>();
                        for (ClassFit classFit : classFits) {
                            if (classFit.getDepartment().equals(departmentA) && !classFit.getClassId().equals(classA.getClassId())) {
                                sameDepartmentClasses.add(classFit);
                            }
                        }
    
                        // If there are no other classes in the same department to swap with, skip to the next class
                        if (sameDepartmentClasses.isEmpty()) {
                            String noSwapMessage = "Iteration " + i + ": No other classes in the same department to swap with for class " + classA.getClassId();
                            writeOutputToFile(noSwapMessage, outputFile, writer);
                            continue;
                        }
    
                        // Iterate through all classes in the same department to find a better fit
                        for (ClassFit classB : sameDepartmentClasses) {
                            String swapAttemptMessage = "Iteration " + i + ": Attempting to swap class " + classA.getClassId() + " with class " + classB.getClassId();
                            writeOutputToFile(swapAttemptMessage, outputFile, writer);
    
                            // Check if time slots are compatible
                            if (!classA.getTimeSlot().equals(classB.getTimeSlot())) {
                                String timeSlotMismatchMessage = "Iteration " + i + ": Time slot mismatch between class " + classA.getClassId() + " and class " + classB.getClassId() + ". Skipping swap.";
                                writeOutputToFile(timeSlotMismatchMessage, outputFile, writer);
                                continue;
                            }
    
                            // Get room capacities for both classes
                            int capacityA = classA.getRoomCapacity();
                            int capacityB = classB.getRoomCapacity();
    
                            // Recalculate enrollments after swapping
                            int newEnrollmentA = 0;
                            int newEnrollmentB = 0;
    
                            // Recalculate enrollment counts, considering students' preferences and room capacities
                            for (String studentId : classA.getStudents()) {
                                boolean prefersClassB = classB.getStudentPreferences().contains(studentId);
                                boolean canFitInRoomB = newEnrollmentA < capacityB;
    
                                if (prefersClassB && canFitInRoomB) {
                                    newEnrollmentA++;
                                } else {
                                    writeOutputToFile("Iteration " + i + ": Student " + studentId + " cannot enroll in class B due to capacity or preference.", outputFile, writer);
                                }
                            }
    
                            for (String studentId : classB.getStudents()) {
                                boolean prefersClassA = classA.getStudentPreferences().contains(studentId);
                                boolean canFitInRoomA = newEnrollmentB < capacityA;
    
                                if (prefersClassA && canFitInRoomA) {
                                    newEnrollmentB++;
                                } else {
                                    writeOutputToFile("Iteration " + i + ": Student " + studentId + " cannot enroll in class A due to capacity or preference.", outputFile, writer);
                                }
                            }
    
                            int initialCombinedEnrollment = classA.getEnrollmentCount() + classB.getEnrollmentCount();
                            int newCombinedEnrollment = newEnrollmentA + newEnrollmentB;
    
                            // Log initial and potential new enrollments
                            String initialStateMessage = String.format("Iteration %d: Initial Enrollment - Class %s: %d, Class %s: %d, Combined: %d", i, classA.getClassId(), classA.getEnrollmentCount(), classB.getClassId(), classB.getEnrollmentCount(), initialCombinedEnrollment);
                            writeOutputToFile(initialStateMessage, outputFile, writer);
    
                            String potentialFitMessage = String.format("Iteration %d: Potential fit after recalculating - Class %s: %d, Class %s: %d, Combined: %d", i, classA.getClassId(), newEnrollmentA, classB.getClassId(), newEnrollmentB, newCombinedEnrollment);
                            writeOutputToFile(potentialFitMessage, outputFile, writer);
    
                            // If the swap improves the fit number (enrollment count), keep it
                            if (newCombinedEnrollment > initialCombinedEnrollment) {
                                classA.setEnrollmentCount(newEnrollmentA);
                                classB.setEnrollmentCount(newEnrollmentB);
                                updateFitPercentage(classA);
                                updateFitPercentage(classB);
                                swapped = true;
    
                                String swapSuccessMessage = "Iteration " + i + ": Swapped class " + classA.getClassId() + " with class " + classB.getClassId();
                                writeOutputToFile(swapSuccessMessage, outputFile, writer);
                            } else {
                                String revertSwapMessage = "Iteration " + i + ": Swap between class " + classA.getClassId() + " and class " + classB.getClassId() + " did not improve fit. No changes made.";
                                writeOutputToFile(revertSwapMessage, outputFile, writer);
                            }
                        }
    
                        // If a successful swap was made, sort the classes again and proceed with the next iteration
                        if (swapped) {
                            classFits.sort(Comparator.comparingDouble(ClassFit::getFitPercentage));
                            break;
                        }
                    }
    
                    // If no improvement found for any class, log the result
                    if (!swapped) {
                        String noImprovementMessage = "Iteration " + i + ": No improvement found for any class.";
                        writeOutputToFile(noImprovementMessage, outputFile, writer);
                    }
                }
    
                // Print final fit percentages after simulated annealing
                for (ClassFit classFit : classFits) {
                    String fitData = classFit.toString();
                    writeOutputToFile(fitData, outputFile, writer);
                }
    
                // Calculate and print the overall fit percentage after simulated annealing
                double overallFitPercentage = calculateOverallFitPercentage(classFits);
                String overallFitMessage = String.format("Overall Fit Percentage after simulated annealing: %.2f%%", overallFitPercentage);
                writeOutputToFile(overallFitMessage, outputFile, writer);
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }
    
        private static List<ClassFit> loadClassFits(String inputFile) {
            List<ClassFit> classFits = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
                String line = br.readLine();  // Skip header line
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\s*");
                    if (parts.length >= 5) {
                        String timeSlot = parts[0] + " " + parts[1];
                        String roomName = parts[2];
                        String classId = parts[3];
                        String teacherId = parts[4];
                        List<String> students = new ArrayList<>();
                        if (parts.length > 5) {
                            students = Arrays.asList(parts[5].split(","));
                        }
                        ClassFit classFit = new ClassFit(classId, students.size(), students.size(), 0.0);
                        classFit.setTimeSlot(timeSlot);
                        classFit.setRoomName(roomName);
                        classFit.setTeacherId(teacherId);
                        classFit.setStudents(students);
                        classFits.add(classFit);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading class fit data: " + e.getMessage());
            }
            return classFits;
        }
    
        private static void writeOutputToFile(String message, String outputFile, BufferedWriter writer) throws IOException {
            System.out.println(message);
            writer.write(message);
            writer.newLine();
        }
    
        private static void updateFitPercentage(ClassFit classFit) {
            double fitPercentage = (classFit.getPreferenceCount() == 0) ? 0.0 : ((double) classFit.getEnrollmentCount() / classFit.getPreferenceCount()) * 100;
            classFit.setFitPercentage(fitPercentage);
        }
    
        private static double calculateOverallFitPercentage(List<ClassFit> classFits) {
            int totalPreferred = 0;
            int totalEnrolled = 0;
    
            for (ClassFit classFit : classFits) {
                totalPreferred += classFit.getPreferenceCount();
                totalEnrolled += Math.min(classFit.getEnrollmentCount(), classFit.getPreferenceCount());
            }
    
            return (totalPreferred == 0) ? 0.0 : ((double) totalEnrolled / totalPreferred) * 100;
        }
}