package com.rb.acmelearning.controller;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Instructor;
import com.rb.acmelearning.model.Student;
import com.rb.acmelearning.service.CourseService;
import com.rb.acmelearning.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/instructor")
public class InstructorController {
    @Autowired
    private InstructorService instructorService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/")
    public ResponseEntity<List<Instructor>> getAllInstructors() {
        List<Instructor> instructors = instructorService.showAllInstructors();
        if (instructors != null && !instructors.isEmpty()) {
            return ResponseEntity.ok().body(instructors);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUpInstructor(@RequestBody Instructor instructor) {
        Instructor createdInstructor = instructorService.createInstructor(instructor);
        if (createdInstructor != null) {
            return ResponseEntity.ok("Instructor signup successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to create instructor");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginInstructor(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Instructor authenticatedInstructor = instructorService.authenticateInstructor(username, password);

        if (authenticatedInstructor != null) {
            return ResponseEntity.ok("Instructor login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/{instructorId}/createCourse")
    public ResponseEntity<Course> createCourse(@RequestBody Course course, @PathVariable long instructorId) {
        Course createdCourse = courseService.createCourse(course, instructorId);
        if (createdCourse != null) {
            return ResponseEntity.ok().body(createdCourse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{instructorId}/getCourses")
    public ResponseEntity<List<Course>> getAllCourses(@PathVariable long instructorId) {
        List<Course> courses = instructorService.listAllCourses(instructorId);
        if (courses != null && !courses.isEmpty()) {
            return ResponseEntity.ok().body(courses);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @PostMapping("/{instructorId}/start/{courseId}")
    public ResponseEntity<Course> startCourse(@PathVariable long courseId, @PathVariable long instructorId) {
        Course startedCourse = courseService.startCourse(instructorId, courseId);
        if (startedCourse != null) {
            return ResponseEntity.ok().body(startedCourse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/{instructorId}/cancel/{courseId}")
    public ResponseEntity<String> cancelCourse(@PathVariable long courseId, @PathVariable long instructorId) {
        if (courseService.validateCourseAndInstructor(courseId, instructorId)) {
            courseService.cancelCourse(instructorId, courseId);
            return ResponseEntity.ok().body("Course canceled successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course and Instructor ID mismatch");
        }
    }

    @GetMapping("/{instructorId}/course/{courseId}/students")
    public ResponseEntity<List<Student>> getStudentsByCourses(@PathVariable long instructorId, @PathVariable long courseId) {
        List<Student> students = instructorService.getStudentsByCourses(instructorId, courseId);
        if (students != null && !students.isEmpty()) {
            return ResponseEntity.ok().body(students);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

}
