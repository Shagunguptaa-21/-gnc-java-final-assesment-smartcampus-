package smartcampus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

// Manages student and course records, enrollment assignments, and persistence.
public class EnrollmentManager {
    private final Map<Integer, Student> krStudents = new HashMap<>();
    private final Map<Integer, Course> krCourses = new HashMap<>();
    private final Map<Integer, List<Course>> krEnrollments = new HashMap<>();
    private final Queue<EnrollmentRequest> krPendingEnrollments = new LinkedList<>();
    private final Path krDataFolder = Paths.get("data");

    public EnrollmentManager() {
        loadData();
    }

    // Adds a student after validation.
    public void addStudent(int id, String name, String email) throws InvalidDataException {
        if (krStudents.containsKey(id)) {
            throw new InvalidDataException("Student ID " + id + " already exists.");
        }
        Student student = new Student(id, name, email);
        krStudents.put(id, student);
    }

    // Adds a course after validation.
    public void addCourse(int id, String name, double fee) throws InvalidDataException {
        if (krCourses.containsKey(id)) {
            throw new InvalidDataException("Course ID " + id + " already exists.");
        }
        Course course = new Course(id, name, fee);
        krCourses.put(id, course);
    }

    // Updates an existing student's name.
    public void updateStudentName(int id, String newName) throws InvalidDataException {
        Student student = krStudents.get(id);
        if (student == null) {
            throw new InvalidDataException("Student ID " + id + " not found.");
        }
        student.setName(newName);
    }

    // Updates an existing course's name.
    public void updateCourseName(int id, String newName) throws InvalidDataException {
        Course course = krCourses.get(id);
        if (course == null) {
            throw new InvalidDataException("Course ID " + id + " not found.");
        }
        course.setCourseName(newName);
    }

    // Adds an enrollment request to the pending queue.
    public synchronized void requestEnrollment(int studentId, int courseId) throws InvalidDataException {
        if (!krStudents.containsKey(studentId)) {
            throw new InvalidDataException("Student ID not found.");
        }
        if (!krCourses.containsKey(courseId)) {
            throw new InvalidDataException("Course ID not found.");
        }
        krPendingEnrollments.add(new EnrollmentRequest(studentId, courseId));
    }

    // Retrieves and clears pending enrollment requests for processing.
    public synchronized List<EnrollmentRequest> drainPendingEnrollments() {
        List<EnrollmentRequest> requests = new ArrayList<>(krPendingEnrollments);
        krPendingEnrollments.clear();
        return requests;
    }

    // Completes a pending enrollment into the permanent enrollment map.
    public synchronized void completeEnrollment(EnrollmentRequest request) throws InvalidDataException {
        Student student = krStudents.get(request.getStudentId());
        Course course = krCourses.get(request.getCourseId());
        if (student == null) {
            throw new InvalidDataException("Student not found while completing enrollment.");
        }
        if (course == null) {
            throw new InvalidDataException("Course not found while completing enrollment.");
        }
        krEnrollments.computeIfAbsent(student.getStudentId(), id -> new ArrayList<>());
        List<Course> currentCourses = krEnrollments.get(student.getStudentId());
        if (!currentCourses.contains(course)) {
            currentCourses.add(course);
        }
    }

    // Returns a copy of all students.
    public List<Student> getAllStudents() {
        return new ArrayList<>(krStudents.values());
    }

    // Returns a copy of all courses.
    public List<Course> getAllCourses() {
        return new ArrayList<>(krCourses.values());
    }

    // Returns a copy of the enrollment mapping.
    public Map<Integer, List<Course>> getEnrollments() {
        return new HashMap<>(krEnrollments);
    }

    // Returns the number of pending enrollment requests.
    public int getPendingEnrollmentCount() {
        synchronized (this) {
            return krPendingEnrollments.size();
        }
    }

    public boolean hasStudents() {
        return !krStudents.isEmpty();
    }

    public boolean hasCourses() {
        return !krCourses.isEmpty();
    }

    // Saves current data to text files in the data folder.
    public void saveData() {
        try {
            if (!Files.exists(krDataFolder)) {
                Files.createDirectories(krDataFolder);
            }
            Path studentFile = krDataFolder.resolve("students.txt");
            Path courseFile = krDataFolder.resolve("courses.txt");
            Path enrollmentFile = krDataFolder.resolve("enrollments.txt");

            Files.write(studentFile, krStudents.values().stream()
                    .map(s -> String.format("%d,%s,%s", s.getStudentId(), s.getName(), s.getEmail()))
                    .collect(Collectors.toList()));

            Files.write(courseFile, krCourses.values().stream()
                    .map(c -> String.format("%d,%s,%.2f", c.getCourseId(), c.getCourseName(), c.getFee()))
                    .collect(Collectors.toList()));

            Files.write(enrollmentFile, krEnrollments.entrySet().stream()
                    .flatMap(entry -> entry.getValue().stream()
                            .map(course -> String.format("%d,%d", entry.getKey(), course.getCourseId())))
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            System.out.println("Warning: unable to save data - " + e.getMessage());
        }
    }

    // Loads persisted data from disk if available.
    private void loadData() {
        try {
            if (!Files.exists(krDataFolder)) {
                return;
            }
            Path studentFile = krDataFolder.resolve("students.txt");
            Path courseFile = krDataFolder.resolve("courses.txt");
            Path enrollmentFile = krDataFolder.resolve("enrollments.txt");

            if (Files.exists(studentFile)) {
                Files.readAllLines(studentFile).forEach(line -> {
                    String[] parts = line.split(",", 3);
                    if (parts.length == 3) {
                        try {
                            addStudent(Integer.parseInt(parts[0]), parts[1], parts[2]);
                        } catch (Exception e) {
                            // ignore invalid persisted record
                        }
                    }
                });
            }

            if (Files.exists(courseFile)) {
                Files.readAllLines(courseFile).forEach(line -> {
                    String[] parts = line.split(",", 3);
                    if (parts.length == 3) {
                        try {
                            addCourse(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]));
                        } catch (Exception e) {
                            // ignore invalid persisted record
                        }
                    }
                });
            }

            if (Files.exists(enrollmentFile)) {
                Files.readAllLines(enrollmentFile).forEach(line -> {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        try {
                            int studentId = Integer.parseInt(parts[0]);
                            int courseId = Integer.parseInt(parts[1]);
                            List<Course> studentCourses = krEnrollments.computeIfAbsent(studentId, id -> new ArrayList<>());
                            Course course = krCourses.get(courseId);
                            if (course != null && !studentCourses.contains(course)) {
                                studentCourses.add(course);
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                });
            }
        } catch (IOException e) {
            System.out.println("Warning: unable to load persisted data - " + e.getMessage());
        }
    }
}
