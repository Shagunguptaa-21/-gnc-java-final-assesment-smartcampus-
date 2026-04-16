package smartcampus;

import java.util.List;

// Processes pending enrollment requests in a background thread.
public class EnrollmentProcessor implements Runnable {
    private final EnrollmentManager krManager;

    public EnrollmentProcessor(EnrollmentManager krManager) {
        this.krManager = krManager;
    }

    @Override
    public void run() {
        List<EnrollmentRequest> requests = krManager.drainPendingEnrollments();
        if (requests.isEmpty()) {
            System.out.println("No pending enrollment requests to process.");
            return;
        }

        System.out.printf("Processing %d enrollment request(s) asynchronously...%n", requests.size());
        for (EnrollmentRequest request : requests) {
            try {
                // Simulate asynchronous work with a short delay.
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Enrollment processing was interrupted.");
                return;
            }
            try {
                krManager.completeEnrollment(request);
                System.out.printf("Processed enrollment: Student %d -> Course %d%n",
                        request.getStudentId(), request.getCourseId());
            } catch (InvalidDataException e) {
                System.out.printf("Failed to complete request %s: %s%n", request, e.getMessage());
            }
        }
        System.out.println("Enrollment processing completed.");
    }
}
