package smartcampus;

// Represents a student with validated ID, name, and email.
public class Student {
    private final int krStudentId;
    private final String krName;
    private final String krEmail;

    public Student(int studentId, String name, String email) throws InvalidDataException {
        if (studentId <= 0) {
            throw new InvalidDataException("Student ID must be positive.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Student name cannot be empty.");
        }
        if (email == null || !email.contains("@") || email.trim().length() < 5) {
            throw new InvalidDataException("Email must be valid.");
        }
        this.krStudentId = studentId;
        this.krName = name.trim();
        this.krEmail = email.trim();
    }

    public int getStudentId() {
        return krStudentId;
    }

    public String getName() {
        return krName;
    }

    public void setName(String name) throws InvalidDataException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Student name cannot be empty.");
        }
        this.krName = name.trim();
    }

    public String getEmail() {
        return krEmail;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s)", krStudentId, krName, krEmail);
    }
}
