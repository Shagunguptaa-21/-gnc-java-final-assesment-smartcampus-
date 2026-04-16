package smartcampus;

// Represents a course with validated ID, name, and fee.
public class Course {
    private final int krCourseId;
    private final String krCourseName;
    private final double krFee;

    public Course(int courseId, String courseName, double fee) throws InvalidDataException {
        if (courseId <= 0) {
            throw new InvalidDataException("Course ID must be positive.");
        }
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new InvalidDataException("Course name cannot be empty.");
        }
        if (fee < 0) {
            throw new InvalidDataException("Course fee cannot be negative.");
        }
        this.krCourseId = courseId;
        this.krCourseName = courseName.trim();
        this.krFee = fee;
    }

    public int getCourseId() {
        return krCourseId;
    }

    public String getCourseName() {
        return krCourseName;
    }

    public void setCourseName(String courseName) throws InvalidDataException {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new InvalidDataException("Course name cannot be empty.");
        }
        this.krCourseName = courseName.trim();
    }

    public double getFee() {
        return krFee;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - ₹%.2f", krCourseId, krCourseName, krFee);
    }
}
