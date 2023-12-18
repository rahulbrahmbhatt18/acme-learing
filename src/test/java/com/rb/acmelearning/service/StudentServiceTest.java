package com.rb.acmelearning.service;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Enrollment;
import com.rb.acmelearning.model.Instructor;
import com.rb.acmelearning.model.Student;
import com.rb.acmelearning.repository.EnrollmentRepository;
import com.rb.acmelearning.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import java.util.*;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @InjectMocks
    private StudentServiceImpl studentService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Test
    public void testCreateStudent() {
        Student student = new Student(1L, "testUser", "testPassword", "Test User");
        when(studentRepository.save(student)).thenReturn(student);
        var result = studentService.createStudent(student);
        assertEquals(student, result);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    public void testShowAllStudents() {
        List<Student> students = List.of(new Student(), new Student());
        when(studentRepository.findAll()).thenReturn(students);
        var result = studentService.showAllStudents();
        assertEquals(students, result);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testAuthenticateStudent() {
        String username = "testUsername";
        String password = "testPassword";
        Student mockStudent = new Student();
        when(studentRepository.findByUsernameAndPassword(username, password)).thenReturn(mockStudent);
        Student result = studentService.authenticateStudent(username, password);
        assertEquals(mockStudent, result);
        verify(studentRepository, times(1)).findByUsernameAndPassword(username, password);
    }

    @Test
    public void testListEnrolledCourses() {
        long studentId = 1L;
        Student student = new Student(studentId, "test", "test", "Test");
        List<Enrollment> mockEnrollments = Arrays.asList(
                new Enrollment(1L, student, new Course(1L, "Course 1", "Description 1", false, new Instructor())),
                new Enrollment(2L, student, new Course(2L, "Course 2", "Description 2", false, new Instructor()))
        );
        when(enrollmentRepository.findAll()).thenReturn(mockEnrollments);
        List<Course> result = studentService.listEnrolledCourses(studentId);
        List<Course> expectedCourses = mockEnrollments.stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
        assertEquals(expectedCourses, result);
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    public void testDropCourse() {
        long studentId = 1L;
        long courseId = 2L;
        Student student = new Student(studentId, "test", "test", "Test");
        List<Enrollment> mockEnrollments = Arrays.asList(
                new Enrollment(1L, student, new Course(1L, "Course 1", "Description 1", false, new Instructor())),
                new Enrollment(2L, student, new Course(courseId, "Course 2", "Description 2", false, new Instructor()))
        );
        when(enrollmentRepository.findAll()).thenReturn(mockEnrollments);
        doNothing().when(enrollmentRepository).delete(any(Enrollment.class));
        boolean result = studentService.dropCourse(studentId, courseId);
        assertTrue(result);
        verify(enrollmentRepository, times(1)).delete(argThat(enrollment ->
                enrollment.getStudent().getStudentId() == studentId && enrollment.getCourse().getCourseId() == courseId));
    }

}

