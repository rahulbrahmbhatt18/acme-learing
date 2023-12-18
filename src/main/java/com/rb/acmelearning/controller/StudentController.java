package com.rb.acmelearning.controller;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Student;
import com.rb.acmelearning.service.CourseService;
import com.rb.acmelearning.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.showAllStudents();

        if (students != null && !students.isEmpty()) {
            return ResponseEntity.ok().body(students);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUpStudent(@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);

        if (createdStudent != null) {
            return ResponseEntity.ok("Student signup successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to create student");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginStudent(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Student authenticatedStudent = studentService.authenticateStudent(username, password);

        if (authenticatedStudent != null) {
            return ResponseEntity.ok("Student login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/{studentId}/enrollCourse/{courseId}")
    public ResponseEntity<String> enrollToCourse(@PathVariable long studentId, @PathVariable long courseId) {
        Course course = courseService.enrollCourse(studentId, courseId);

        if (course != null) {
            return ResponseEntity.ok("Enrollment successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to enroll in the course");
        }
    }

    @GetMapping("/{studentId}/enrolledCourses")
    public ResponseEntity<List<Course>> listEnrolledCourses(@PathVariable long studentId) {
        List<Course> enrolledCourses = studentService.listEnrolledCourses(studentId);

        if (enrolledCourses != null && !enrolledCourses.isEmpty()) {
            return ResponseEntity.ok().body(enrolledCourses);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @DeleteMapping("/{studentId}/dropCourse/{courseId}")
    public ResponseEntity<String> dropCourse(@PathVariable long studentId, @PathVariable long courseId) {
        if (studentService.dropCourse(studentId, courseId)) {
            return ResponseEntity.ok().body("Course dropped successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
        }
    }

}
