package com.rb.acmelearning.controller;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Student;
import com.rb.acmelearning.service.CourseService;
import com.rb.acmelearning.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {
    @InjectMocks
    private StudentController studentController;
    @Mock
    private StudentService studentService;
    @Mock
    private CourseService courseService;

    @Test
    @DisplayName("Test endpoint to fetch list of all students")
    public void testGetAllStudents() {
        var mockStudents = Arrays.asList(new Student(), new Student());
        when(studentService.showAllStudents()).thenReturn(mockStudents);
        var response = studentController.getAllStudents();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockStudents, response.getBody());
        verify(studentService, times(1)).showAllStudents();
    }

    @Test
    @DisplayName("Test endpoint to sign up a new student - Success")
    public void testSignUpStudentSuccess() {
        Student mockStudent = new Student();
        when(studentService.createStudent(any())).thenReturn(mockStudent);
        var response = studentController.signUpStudent(mockStudent);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Student signup successful", response.getBody());
        verify(studentService, times(1)).createStudent(any());
    }

    @Test
    @DisplayName("Test endpoint to sign up a new student - Failure")
    public void testSignUpStudentFailure() {
        when(studentService.createStudent(any())).thenReturn(null);
        var response = studentController.signUpStudent(new Student());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Unable to create student", response.getBody());
        verify(studentService, times(1)).createStudent(any());
    }

    @Test
    @DisplayName("Test endpoint to login a student - Success")
    public void testLoginStudentSuccess() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testUser");
        credentials.put("password", "testPassword");
        Student mockStudent = new Student();
        when(studentService.authenticateStudent("testUser", "testPassword")).thenReturn(mockStudent);
        var response = studentController.loginStudent(credentials);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Student login successful", response.getBody());
        verify(studentService, times(1)).authenticateStudent("testUser", "testPassword");
    }

    @Test
    @DisplayName("Test endpoint to login a student - Failure")
    public void testLoginStudentFailure() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testUser");
        credentials.put("password", "testPassword");
        when(studentService.authenticateStudent("testUser", "testPassword")).thenReturn(null);
        var response = studentController.loginStudent(credentials);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
        verify(studentService, times(1)).authenticateStudent("testUser", "testPassword");
    }

    @Test
    @DisplayName("Test enroll to course")
    public void testEnrollToCourse() {
        long studentId = 1L;
        long courseId = 2L;
        Course mockCourse = new Course();
        when(courseService.enrollCourse(studentId, courseId)).thenReturn(mockCourse);
        var response = studentController.enrollToCourse(studentId, courseId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Enrollment successful", response.getBody());

        when(courseService.enrollCourse(studentId, courseId)).thenReturn(null);
        response = studentController.enrollToCourse(studentId, courseId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody(), "Unable to enroll in the course");

        verify(courseService, times(2)).enrollCourse(studentId, courseId);
    }

    @Test
    @DisplayName("Test student enrolled courses list")
    public void testListEnrolledCourses() {
        long studentId = 1L;
        var mockEnrolledCourses = Arrays.asList(new Course(), new Course());
        when(studentService.listEnrolledCourses(studentId)).thenReturn(mockEnrolledCourses);
        var response = studentController.listEnrolledCourses(studentId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockEnrolledCourses, response.getBody());
        verify(studentService, times(1)).listEnrolledCourses(studentId);
    }

    @Test
    @DisplayName("Test dropCourse - Successful")
    public void testDropCourse() {
        var studentId = 1L;
        var courseId = 2L;
        when(studentService.dropCourse(studentId, courseId)).thenReturn(true);
        var response = studentController.dropCourse(studentId, courseId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course dropped successfully!", response.getBody());
        verify(studentService, times(1)).dropCourse(studentId, courseId);
    }

    @Test
    @DisplayName("Test dropCourse - Invalid Request")
    public void testDropCourseInvalidRequest() {
        var studentId = 1L;
        var courseId = 2L;
        when(studentService.dropCourse(studentId, courseId)).thenReturn(false);
        var response = studentController.dropCourse(studentId, courseId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid Request", response.getBody());
        verify(studentService, times(1)).dropCourse(studentId, courseId);
    }
}

