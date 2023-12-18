package com.rb.acmelearning.controller;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Instructor;
import com.rb.acmelearning.model.Student;
import com.rb.acmelearning.service.CourseService;
import com.rb.acmelearning.service.InstructorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InstructorControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private InstructorService instructorService;

    @MockBean
    private CourseService courseService;

    @Test
    public void testGetAllInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        when(instructorService.showAllInstructors()).thenReturn(instructors);
        var response = restTemplate.exchange(
                "/instructor/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Instructor>>() {});
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(0, response.getBody().size());
        verify(instructorService, times(1)).showAllInstructors();
    }

    @Test
    public void testSignUpInstructor() {
        Instructor instructorToSignUp = new Instructor(1L, "dummy", "dummy", "Dummy Data");
        Instructor createdInstructor = instructorToSignUp;
        var expectedResponse = ResponseEntity.ok("Instructor signup successful");
        when(instructorService.createInstructor(instructorToSignUp)).thenReturn(createdInstructor);
        var response = restTemplate.postForEntity(
                "/instructor/signUp",
                instructorToSignUp,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(instructorService, times(1)).createInstructor(instructorToSignUp);
    }

    @Test
    public void testLoginInstructor() {
        String username = "testUsername";
        String password = "testPassword";
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);
        Instructor authenticatedInstructor = new Instructor(1L, "dummy", "dummy", "Dummy Data");
        when(instructorService.authenticateInstructor(username, password)).thenReturn(authenticatedInstructor);
        var response = restTemplate.postForEntity(
                "/instructor/login",
                credentials,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Instructor login successful", response.getBody());
        verify(instructorService, times(1)).authenticateInstructor(username, password);
    }

    @Test
    public void testCreateCourse() {
        long instructorId = 1L;
        Course courseToCreate = new Course(1L, "Dummy 101", "Into to Dummy course", false, new Instructor());
        Course createdCourse = courseToCreate;
        when(courseService.createCourse(courseToCreate, instructorId)).thenReturn(createdCourse);
        var response = restTemplate.postForEntity(
                "/instructor/{instructorId}/createCourse",
                courseToCreate,
                Course.class,
                instructorId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdCourse, response.getBody());
        verify(courseService, times(1)).createCourse(courseToCreate, instructorId);
    }

    @Test
    public void testGetAllCourses() {
        long instructorId = 1L;
        List<Course> courses = new ArrayList<>();
        when(instructorService.listAllCourses(instructorId)).thenReturn(courses);
        var response = restTemplate.exchange(
                "/instructor/{instructorId}/getCourses",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Course>>() {},
                instructorId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(0, response.getBody().size());
        verify(instructorService, times(1)).listAllCourses(instructorId);
    }

    @Test
    public void testStartCourse() {
        long instructorId = 1L;
        long courseId = 2L;
        Course startedCourse = new Course(1L, "Dummy 101", "Into to Dummy course", false, new Instructor());
        when(courseService.startCourse(instructorId, courseId)).thenReturn(startedCourse);
        var response = restTemplate.postForEntity(
                "/instructor/{instructorId}/start/{courseId}",
                null,
                Course.class,
                instructorId,
                courseId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(startedCourse, response.getBody());
        verify(courseService, times(1)).startCourse(instructorId, courseId);
    }

    @Test
    public void testCancelCourse() {
        long instructorId = 1L;
        long courseId = 2L;
        when(courseService.validateCourseAndInstructor(courseId, instructorId)).thenReturn(true);
        var response = restTemplate.postForEntity(
                "/instructor/{instructorId}/cancel/{courseId}",
                null,
                String.class,
                instructorId,
                courseId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course canceled successfully", response.getBody());
        verify(courseService, times(1)).cancelCourse(instructorId, courseId);
    }

    @Test
    public void testGetStudentsByCourses() {
        long instructorId = 1L;
        long courseId = 2L;
        List<Student> students = new ArrayList<>();
        when(instructorService.getStudentsByCourses(instructorId, courseId)).thenReturn(students);
        var response = restTemplate.exchange(
                "/instructor/{instructorId}/course/{courseId}/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {},
                instructorId,
                courseId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(0, response.getBody().size());
        verify(instructorService, times(1)).getStudentsByCourses(instructorId, courseId);
    }
}
