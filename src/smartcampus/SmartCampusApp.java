package smartcampus;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Main application class for SmartCampus management.
// Provides a menu-driven console interface for student, course, and enrollment operations.
public class SmartCampusApp {
    private final EnrollmentManager krManager = new EnrollmentManager();
    private final Scanner krScanner = new Scanner(System.in);

    public static void main(String[] args) {
        new SmartCampusApp().run();
    }

    // Runs the application loop until the user chooses to exit.
    private void run() {
        System.out.println("Welcome to SmartCampus Management System");
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int krChoice = readInt("Choose an option: ");
                switch (krChoice) {
                    case 1 -> addStudent();
                    case 2 -> addCourse();
                    case 3 -> enrollStudent();
                    case 4 -> viewStudents();
                    case 5 -> viewEnrollments();
                    case 6 -> processEnrollment();
                    case 7 -> updateStudent();
                    case 8 -> updateCourse();
                    case 9 -> {
                        exitApplication();
                        running = false;
                    }
                    default -> System.out.println("Please choose a valid menu option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input type. Please enter valid data.");
                krScanner.nextLine();
            } catch (InvalidDataException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Prints the main menu options.
    private void printMenu() {
        System.out.println();
        System.out.println("--- SmartCampus Menu ---");
        System.out.println("1. Add Student");
        System.out.println("2. Add Course");
        System.out.println("3. Enroll Student");
        System.out.println("4. View Students");
        System.out.println("5. View Enrollments");
        System.out.println("6. Process Enrollment (Thread)");
        System.out.println("7. Update Student");
        System.out.println("8. Update Course");
        System.out.println("9. Exit");
    }

    // Reads user input and registers a new student.
    private void addStudent() throws InvalidDataException {
        System.out.println("Enter student details:");
        int id = readInt("Student ID: ");
        String name = readLine("Name: ");
        String email = readLine("Email: ");
        krManager.addStudent(id, name, email);
        System.out.println("Student added successfully.");
    }

    // Reads user input and registers a new course.
    private void addCourse() throws InvalidDataException {
        System.out.println("Enter course details:");
        int id = readInt("Course ID: ");
        String name = readLine("Course name: ");
        double fee = readDouble("Course fee: ");
        krManager.addCourse(id, name, fee);
        System.out.println("Course added successfully.");
    }

    // Queues an enrollment request for later asynchronous processing.
    private void enrollStudent() throws InvalidDataException {
        if (!krManager.hasStudents() || !krManager.hasCourses()) {
            System.out.println("Add students and courses before requesting enrollment.");
            return;
        }
        int studentId = readInt("Student ID to enroll: ");
        int courseId = readInt("Course ID to enroll into: ");
        krManager.requestEnrollment(studentId, courseId);
        System.out.println("Enrollment request queued. Use Process Enrollment to complete it.");
    }

    // Lists all registered students.
    private void viewStudents() {
        List<Student> krStudents = krManager.getAllStudents();
        if (krStudents.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
        System.out.println("Students:");
        krStudents.forEach(student -> System.out.println("  " + student));
    }

    // Displays completed enrollments and pending enrollment count.
    private void viewEnrollments() {
        Map<Integer, List<Course>> krEnrollments = krManager.getEnrollments();
        if (krEnrollments.isEmpty()) {
            System.out.println("No completed enrollments yet.");
        } else {
            System.out.println("Completed enrollments:");
            krEnrollments.forEach((studentId, courses) -> {
                Student student = krManager.getAllStudents().stream()
                        .filter(s -> s.getStudentId() == studentId)
                        .findFirst()
                        .orElse(null);
                String studentLabel = student != null ? student.getName() : "Student " + studentId;
                System.out.printf("  %s: %s%n", studentLabel,
                        courses.stream().map(Course::getCourseName).toList());
            });
        }
        System.out.printf("Pending enrollment requests: %d%n", krManager.getPendingEnrollmentCount());
    }

    // Starts enrollment processing in a separate thread.
    private void processEnrollment() {
        Thread processor = new Thread(new EnrollmentProcessor(krManager));
        processor.start();
        try {
            processor.join();
        } catch (InterruptedException e) {
            System.out.println("Enrollment processing interrupted.");
        }
    }

    // Updates an existing student's name.
    private void updateStudent() throws InvalidDataException {
        if (!krManager.hasStudents()) {
            System.out.println("No students available to update.");
            return;
        }
        int id = readInt("Student ID to update: ");
        String newName = readLine("New name: ");
        krManager.updateStudentName(id, newName);
        System.out.println("Student name updated successfully.");
    }

    // Updates an existing course's name.
    private void updateCourse() throws InvalidDataException {
        if (!krManager.hasCourses()) {
            System.out.println("No courses available to update.");
            return;
        }
        int id = readInt("Course ID to update: ");
        String newName = readLine("New course name: ");
        krManager.updateCourseName(id, newName);
        System.out.println("Course name updated successfully.");
    }

    // Saves data and closes the application.
    private void exitApplication() {
        System.out.println("Saving data and exiting...");
        krManager.saveData();
        krScanner.close();
        System.out.println("Goodbye.");
    }

    // Helper methods to read user inputs safely.
    private int readInt(String prompt) {
        System.out.print(prompt);
        int value = krScanner.nextInt();
        krScanner.nextLine();
        return value;
    }

    private double readDouble(String prompt) {
        System.out.print(prompt);
        double value = krScanner.nextDouble();
        krScanner.nextLine();
        return value;
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return krScanner.nextLine();
    }
}
