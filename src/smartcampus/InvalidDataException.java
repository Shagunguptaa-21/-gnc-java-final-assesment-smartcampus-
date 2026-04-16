package smartcampus;

// Custom checked exception used for invalid student and course data.
public class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}
