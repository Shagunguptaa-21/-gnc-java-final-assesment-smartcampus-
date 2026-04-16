package smartcampus;

// Holds a single enrollment request for a student and a course.
public class EnrollmentRequest {
    private final int krStudentId;
    private final int krCourseId;

    public EnrollmentRequest(int krStudentId, int krCourseId) {
        this.krStudentId = krStudentId;
        this.krCourseId = krCourseId;
    }

    public int getStudentId() {
        return krStudentId;
    }

    public int getCourseId() {
        return krCourseId;
    }

    @Override
    public String toString() {
        return String.format("Student %d -> Course %d", krStudentId, krCourseId);
    }
}
