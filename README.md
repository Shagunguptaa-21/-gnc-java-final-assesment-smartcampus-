# gnc-java-final-assessment-smartcampus

## SmartCampus Management System
A Java console application for managing students, courses, and enrollments using OOP, collections, exception handling, and multithreading.

## Features
- Add and manage students
- Add and manage courses
- Request student enrollment into courses
- Asynchronous enrollment processing via a separate thread
- Custom exception handling for invalid input and validation errors
- Persistent save/load of student, course, and enrollment data using text files
- Menu-driven command line interface

## Project Structure
- `src/smartcampus/SmartCampusApp.java` - main application and menu
- `src/smartcampus/EnrollmentManager.java` - collection and enrollment logic
- `src/smartcampus/EnrollmentProcessor.java` - asynchronous enrollment processing
- `src/smartcampus/EnrollmentRequest.java` - pending enrollment request model
- `src/smartcampus/Student.java` - student model
- `src/smartcampus/Course.java` - course model
- `src/smartcampus/InvalidDataException.java` - custom exception type

## How to Run
1. Open a terminal in the project root.
2. Compile the Java sources:
   ```bash
   javac -d out src/smartcampus/*.java
   ```
3. Run the application:
   ```bash
   java -cp out smartcampus.SmartCampusApp
   ```

## Expected Output
- The menu displays options to add students, add courses, queue enrollment requests, view records, and process enrollments.
- Enrollment processing runs asynchronously and prints progress messages while the main menu remains available.
- Data is saved into the `data/` folder when the application exits.

## Unique Additions
- Enrollment requests are queued and processed asynchronously.
- Data persistence is implemented as a bonus.
- Input validation uses a custom exception for better reliability.

## Example Usage

```
Welcome to SmartCampus Management System

--- SmartCampus Menu ---
1. Add Student
2. Add Course
3. Enroll Student
4. View Students
5. View Enrollments
6. Process Enrollment (Thread)
7. Exit
Choose an option: 1
Enter student details:
Student ID: 101
Name: Khushi
Email: khushi@example.com
Student added successfully.

--- SmartCampus Menu ---
1. Add Student
2. Add Course
3. Enroll Student
4. View Students
5. View Enrollments
6. Process Enrollment (Thread)
7. Exit
Choose an option: 2
Enter course details:
Course ID: 1
Course name: Java Programming
Course fee: 5000
Course added successfully.

--- SmartCampus Menu ---
1. Add Student
2. Add Course
3. Enroll Student
4. View Students
5. View Enrollments
6. Process Enrollment (Thread)
7. Exit
Choose an option: 3
Student ID to enroll: 101
Course ID to enroll into: 1
Enrollment request queued. Use Process Enrollment to complete it.

--- SmartCampus Menu ---
1. Add Student
2. Add Course
3. Enroll Student
4. View Students
5. View Enrollments
6. Process Enrollment (Thread)
7. Exit
Choose an option: 6
Processing 1 pending enrollment(s)...
Processed enrollment: Student 101 -> Course 1
Enrollment processing completed.

--- SmartCampus Menu ---
1. Add Student
2. Add Course
3. Enroll Student
4. View Students
5. View Enrollments
6. Process Enrollment (Thread)
7. Exit
Choose an option: 5
Completed enrollments:
  Khushi: [Java Programming]
Pending enrollment requests: 0
```
MCQ 1: Collections Design – B
MCQ 2: Exception Handling Scenario – C
MCQ 3: Multithreading Use Case – B
MCQ 4: OOP Design Decision – B