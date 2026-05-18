package edu.ccrm.web.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.ccrm.domain.*;
import edu.ccrm.exception.DataIntegrityException;
import edu.ccrm.exception.RecordNotFoundException;
import edu.ccrm.service.*;
import edu.ccrm.util.SimpleJsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ApiHandlers {

    // Initialize Services (matching Main.java setup)
    private static final StudentService studentService = new StudentService();
    private static final InstructorService instructorService = new InstructorService();
    private static final CourseService courseService = new CourseService(instructorService);
    private static final EnrollmentService enrollmentService = new EnrollmentService(studentService, courseService);

    // --- Utility Methods for Handlers ---

    private static void sendResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String getRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    // --- Handlers ---

    public static class StudentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("GET".equals(method)) {
                // Return list of all students
                List<Student> students = studentService.getAllStudentsSortedById();
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < students.size(); i++) {
                    Student s = students.get(i);
                    json.append(String.format("{\"id\":%d, \"regNo\":\"%s\", \"firstName\":\"%s\", \"lastName\":\"%s\", \"email\":\"%s\", \"status\":\"%s\"}",
                            s.getId(), s.getRegNo(), SimpleJsonUtils.escapeJson(s.getFullName().getFirstName()),
                            SimpleJsonUtils.escapeJson(s.getFullName().getLastName()), SimpleJsonUtils.escapeJson(s.getEmail()), s.getStatus().name()));
                    if (i < students.size() - 1) json.append(",");
                }
                json.append("]");
                sendResponse(exchange, 200, json.toString());

            } else if ("POST".equals(method)) {
                // Add a new student
                try {
                    Map<String, String> data = SimpleJsonUtils.parseFlatJson(getRequestBody(exchange));
                    int id = Integer.parseInt(data.getOrDefault("id", "0"));
                    String regNo = data.get("regNo");
                    String firstName = data.get("firstName");
                    String lastName = data.get("lastName");
                    String email = data.get("email");

                    Student student = new Student(id, regNo, new Name(firstName, lastName), email);
                    studentService.addStudent(student);
                    sendResponse(exchange, 201, "{\"message\":\"Student added successfully\"}");
                } catch (Exception e) {
                    sendResponse(exchange, 400, "{\"error\":\"" + SimpleJsonUtils.escapeJson(e.getMessage()) + "\"}");
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    public static class CourseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("GET".equals(method)) {
                List<Course> courses = courseService.getAllCoursesSortedByCode();
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < courses.size(); i++) {
                    Course c = courses.get(i);
                    String instructorName = c.getInstructor() != null ? c.getInstructor().getFullName().toString() : "N/A";
                    json.append(String.format("{\"code\":\"%s\", \"title\":\"%s\", \"credits\":%d, \"department\":\"%s\", \"semester\":\"%s\", \"instructor\":\"%s\"}",
                            SimpleJsonUtils.escapeJson(c.getCourseCode().getCode()), SimpleJsonUtils.escapeJson(c.getTitle()), c.getCredits(),
                            SimpleJsonUtils.escapeJson(c.getDepartment()), c.getSemester().name(), SimpleJsonUtils.escapeJson(instructorName)));
                    if (i < courses.size() - 1) json.append(",");
                }
                json.append("]");
                sendResponse(exchange, 200, json.toString());

            } else if ("POST".equals(method)) {
                try {
                    Map<String, String> data = SimpleJsonUtils.parseFlatJson(getRequestBody(exchange));
                    CourseCode code = new CourseCode(data.get("code"));
                    String title = data.get("title");
                    int credits = Integer.parseInt(data.getOrDefault("credits", "0"));
                    String department = data.get("department");
                    Semester semester = Semester.valueOf(data.get("semester").toUpperCase());

                    Course course = new Course.Builder(code)
                            .withTitle(title)
                            .withCredits(credits)
                            .withDepartment(department)
                            .withSemester(semester)
                            .build();

                    courseService.addCourse(course);
                    sendResponse(exchange, 201, "{\"message\":\"Course added successfully\"}");
                } catch (Exception e) {
                    sendResponse(exchange, 400, "{\"error\":\"" + SimpleJsonUtils.escapeJson(e.getMessage()) + "\"}");
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    public static class InstructorHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("GET".equals(method)) {
                List<Instructor> instructors = instructorService.getAllInstructorsSortedById();
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < instructors.size(); i++) {
                    Instructor inst = instructors.get(i);
                    json.append(String.format("{\"fid\":\"%s\", \"firstName\":\"%s\", \"lastName\":\"%s\", \"email\":\"%s\", \"department\":\"%s\"}",
                            SimpleJsonUtils.escapeJson(inst.getFiD()), SimpleJsonUtils.escapeJson(inst.getFullName().getFirstName()),
                            SimpleJsonUtils.escapeJson(inst.getFullName().getLastName()), SimpleJsonUtils.escapeJson(inst.getEmail()),
                            SimpleJsonUtils.escapeJson(inst.getDepartment())));
                    if (i < instructors.size() - 1) json.append(",");
                }
                json.append("]");
                sendResponse(exchange, 200, json.toString());

            } else if ("POST".equals(method)) {
                try {
                    Map<String, String> data = SimpleJsonUtils.parseFlatJson(getRequestBody(exchange));
                    String fid = data.get("fid");
                    String firstName = data.get("firstName");
                    String lastName = data.get("lastName");
                    String email = data.get("email");
                    String department = data.get("department");

                    Instructor inst = new Instructor(fid, new Name(firstName, lastName), email, department);
                    instructorService.addInstructor(inst);
                    sendResponse(exchange, 201, "{\"message\":\"Instructor added successfully\"}");
                } catch (Exception e) {
                    sendResponse(exchange, 400, "{\"error\":\"" + SimpleJsonUtils.escapeJson(e.getMessage()) + "\"}");
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    public static class EnrollmentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("GET".equals(method)) {
                // For simplicity, we get all students, then all their enrollments
                // Note: Not optimal for massive data, but matches original CLI view logic.
                List<Student> students = studentService.getAllStudentsSortedById();
                StringBuilder json = new StringBuilder("[");
                boolean first = true;
                for (Student s : students) {
                    try {
                        List<Enrollment> enrollments = enrollmentService.getEnrollmentsForStudent(s.getRegNo());
                        for (Enrollment e : enrollments) {
                            if (!first) json.append(",");
                            json.append(String.format("{\"studentRegNo\":\"%s\", \"studentName\":\"%s\", \"courseCode\":\"%s\", \"courseTitle\":\"%s\", \"grade\":\"%s\"}",
                                    s.getRegNo(), SimpleJsonUtils.escapeJson(s.getFullName().toString()),
                                    SimpleJsonUtils.escapeJson(e.getCourse().getCourseCode().getCode()), SimpleJsonUtils.escapeJson(e.getCourse().getTitle()),
                                    e.getGrade() != null ? e.getGrade().name() : "N/A"));
                            first = false;
                        }
                    } catch (Exception ignored) {}
                }
                json.append("]");
                sendResponse(exchange, 200, json.toString());

            } else if ("POST".equals(method)) {
                try {
                    Map<String, String> data = SimpleJsonUtils.parseFlatJson(getRequestBody(exchange));
                    String regNo = data.get("regNo");
                    CourseCode code = new CourseCode(data.get("courseCode"));
                    
                    enrollmentService.enrollStudent(regNo, code);
                    sendResponse(exchange, 201, "{\"message\":\"Enrollment successful\"}");
                } catch (Exception e) {
                    sendResponse(exchange, 400, "{\"error\":\"" + SimpleJsonUtils.escapeJson(e.getMessage()) + "\"}");
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}
