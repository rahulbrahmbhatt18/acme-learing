package com.rb.acmelearning.controller;


import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Instructor;
import com.rb.acmelearning.model.Student;
import com.rb.acmelearning.service.CourseService;
import com.rb.acmelearning.service.InstructorService;
import com.rb.acmelearning.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private InstructorService instructorService;
    @MockBean
    private StudentService studentService;
    @MockBean
    private CourseService courseService;

    @Test
    public void testGetAllStudents() {
        Student student = new Student();
        var mockStudents = Collections.singletonList(student);
        when(studentService.showAllStudents()).thenReturn(mockStudents);
        var response = restTemplate.exchange(
                "/student/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(studentService, times(1)).showAllStudents();
    }

    @Test
    public void testSignUpStudent() {
        Student studentToSignUp = new Student(1L, "dummy", "dummy", "DummyData");
        Student createdStudent = studentToSignUp;
        when(studentService.createStudent(studentToSignUp)).thenReturn(createdStudent);
        var response = restTemplate.exchange(
                "/student/signUp",
                HttpMethod.POST,
                new HttpEntity<>(studentToSignUp),
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Student signup successful", response.getBody());
        verify(studentService, times(1)).createStudent(studentToSignUp);
    }

    @Test
    public void testLoginStudent() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testUsername");
        credentials.put("password", "testPassword");
        Student authenticatedStudent = new Student(1L, "dummy", "dummy", "DummyData");
        when(studentService.authenticateStudent("testUsername", "testPassword")).thenReturn(authenticatedStudent);
        var response = restTemplate.exchange(
                "/student/login",
                HttpMethod.POST,
                new HttpEntity<>(credentials),
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Student login successful", response.getBody());
        verify(studentService, times(1)).authenticateStudent("testUsername", "testPassword");
    }

    @Test
    public void testEnrollToCourse() {
        long studentId = 1L;
        long courseId = 2L;
        Course enrolledCourse = new Course(courseId, "Dummy 101", "Into to Dummy course", false, new Instructor());
        when(courseService.enrollCourse(studentId, courseId)).thenReturn(enrolledCourse);
        var response = restTemplate.exchange(
                "/student/" + studentId + "/enrollCourse/" + courseId,
                HttpMethod.POST,
                null,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Enrollment successful", response.getBody());
        verify(courseService, times(1)).enrollCourse(studentId, courseId);
    }


    @Test
    public void testListEnrolledCourses() {
        long studentId = 1L;
        var dummyCourses = Arrays.asList(new Course(), new Course());
        when(studentService.listEnrolledCourses(anyLong())).thenReturn(dummyCourses);
        var response = restTemplate.exchange(
                "/student/{studentId}/enrolledCourses",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Course>>() {},
                studentId
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testDropCourse() {
        long studentId = 1L;
        long courseId = 2L;
        when(studentService.dropCourse(studentId, courseId)).thenReturn(true);
        var response = restTemplate.exchange(
                "/student/{studentId}/dropCourse/{courseId}",
                HttpMethod.DELETE,
                null,
                String.class,
                studentId,
                courseId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course dropped successfully!", response.getBody());
        verify(studentService, times(1)).dropCourse(studentId, courseId);
    }
}
