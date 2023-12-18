package com.rb.acmelearning.controller;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Instructor;
import com.rb.acmelearning.model.Student;
import com.rb.acmelearning.service.CourseService;
import com.rb.acmelearning.service.InstructorService;
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
public class InstructorControllerTest {

    @InjectMocks
    private InstructorController instructorController;
    @Mock
    private InstructorService instructorService;
    @Mock
    private CourseService courseService;

    @Test
    @DisplayName("Test endpoint to fetch list of all instructors")
    public void testGetAllInstructors() {
        var mockInstructors = Arrays.asList(new Instructor(), new Instructor());
        when(instructorService.showAllInstructors()).thenReturn(mockInstructors);
        var response = instructorController.getAllInstructors();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockInstructors, response.getBody());
        verify(instructorService, times(1)).showAllInstructors();
    }

    @Test
    @DisplayName("Test endpoint to sign up a new instructor - Success")
    public void testSignUpInstructorSuccess() {
        Instructor mockInstructor = new Instructor();
        when(instructorService.createInstructor(any())).thenReturn(mockInstructor);
        var response = instructorController.signUpInstructor(mockInstructor);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Instructor signup successful", response.getBody());
        verify(instructorService, times(1)).createInstructor(any());
    }

    @Test
    @DisplayName("Test endpoint to sign up a new instructor - Failure")
    public void testSignUpInstructorFailure() {
        when(instructorService.createInstructor(any())).thenReturn(null);
        var response = instructorController.signUpInstructor(new Instructor());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Unable to create instructor", response.getBody());
        verify(instructorService, times(1)).createInstructor(any());
    }


    @Test
    @DisplayName("Test endpoint to login an instructor - Success")
    public void testLoginInstructorSuccess() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testUser");
        credentials.put("password", "testPassword");
        Instructor mockInstructor = new Instructor();
        when(instructorService.authenticateInstructor("testUser", "testPassword")).thenReturn(mockInstructor);
        var response = instructorController.loginInstructor(credentials);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Instructor login successful", response.getBody());
        verify(instructorService, times(1)).authenticateInstructor("testUser", "testPassword");
    }

    @Test
    @DisplayName("Test endpoint to login an instructor - Failure")
    public void testLoginInstructorFailure() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testUser");
        credentials.put("password", "testPassword");
        when(instructorService.authenticateInstructor("testUser", "testPassword")).thenReturn(null);
        var response = instructorController.loginInstructor(credentials);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
        verify(instructorService, times(1)).authenticateInstructor("testUser", "testPassword");
    }

    @Test
    @DisplayName("Test method to create a new course by an instructor")
    public void testCreateCourse() {
        var mockCourse = new Course(999L, "DummyName", "Dummy Description", Boolean.TRUE, new Instructor(1L,"username", "password", "HBP"));
        when(courseService.createCourse(any(), any())).thenReturn(mockCourse);
        var response = instructorController.createCourse(mockCourse, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourse, response.getBody());
        verify(courseService, times(1)).createCourse(any(), any());
    }

    @Test
    @DisplayName("Test method to get all course(s) by an instructor")
    public void testGetAllCourses() {
        long instructorId = 1L;
        var mockCourses = Arrays.asList(new Course(), new Course());
        when(instructorService.listAllCourses(instructorId)).thenReturn(mockCourses);
        var response = instructorController.getAllCourses(instructorId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourses, response.getBody());
        verify(instructorService, times(1)).listAllCourses(instructorId);
    }

    @Test
    @DisplayName("Test endpoint to start a course - Success")
    public void testStartCourseSuccess() {
        long instructorId = 1L;
        long courseId = 2L;
        Course mockCourse = new Course();
        when(courseService.startCourse(instructorId, courseId)).thenReturn(mockCourse);
        var response = instructorController.startCourse(courseId, instructorId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourse, response.getBody());
        verify(courseService, times(1)).startCourse(instructorId, courseId);
    }

    @Test
    @DisplayName("Test endpoint to start a course - Failure")
    public void testStartCourseFailure() {
        long instructorId = 1L;
        long courseId = 2L;
        when(courseService.startCourse(instructorId, courseId)).thenReturn(null);
        var response = instructorController.startCourse(courseId, instructorId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(courseService, times(1)).startCourse(instructorId, courseId);
    }

    @Test
    @DisplayName("Test endpoint to cancel a course - Success")
    public void testCancelCourseSuccess() {
        long instructorId = 1L;
        long courseId = 2L;
        when(courseService.validateCourseAndInstructor(courseId, instructorId)).thenReturn(true);
        var response = instructorController.cancelCourse(courseId, instructorId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course canceled successfully", response.getBody());
        verify(courseService, times(1)).cancelCourse(instructorId, courseId);
    }

    @Test
    @DisplayName("Test endpoint to cancel a course - Failure")
    public void testCancelCourseFailure() {
        long instructorId = 1L;
        long courseId = 2L;
        when(courseService.validateCourseAndInstructor(courseId, instructorId)).thenReturn(false);
        var response = instructorController.cancelCourse(courseId, instructorId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Course and Instructor ID mismatch", response.getBody());
        verify(courseService, never()).cancelCourse(instructorId, courseId);
    }

    @Test
    @DisplayName("Test endpoint to get students by course - Students found")
    public void testGetStudentsByCourseStudentsFound() {
        long instructorId = 1L;
        long courseId = 2L;
        List<Student> mockStudents = Arrays.asList(new Student(), new Student());
        when(instructorService.getStudentsByCourses(instructorId, courseId)).thenReturn(mockStudents);
        var response = instructorController.getStudentsByCourses(instructorId, courseId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockStudents, response.getBody());
        verify(instructorService, times(1)).getStudentsByCourses(instructorId, courseId);
    }

    @Test
    @DisplayName("Test endpoint to get students by course - No students found")
    public void testGetStudentsByCourseNoStudentsFound() {
        long instructorId = 1L;
        long courseId = 2L;
        when(instructorService.getStudentsByCourses(instructorId, courseId)).thenReturn(Collections.emptyList());
        var response = instructorController.getStudentsByCourses(instructorId, courseId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(instructorService, times(1)).getStudentsByCourses(instructorId, courseId);
    }

}

