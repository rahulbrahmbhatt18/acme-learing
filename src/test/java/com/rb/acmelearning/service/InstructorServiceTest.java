package com.rb.acmelearning.service;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Enrollment;
import com.rb.acmelearning.model.Instructor;
import com.rb.acmelearning.model.Student;
import com.rb.acmelearning.repository.CourseRepository;
import com.rb.acmelearning.repository.EnrollmentRepository;
import com.rb.acmelearning.repository.InstructorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class InstructorServiceTest {
    @InjectMocks
    private InstructorServiceImpl instructorService;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Test
    public void testCreateInstructor() {
        var instructor = new Instructor();
        when(instructorRepository.save(instructor)).thenReturn(instructor);
        var result = instructorService.createInstructor(instructor);
        assertEquals(instructor, result);
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    public void testShowAllInstructors() {
        var instructors = List.of(new Instructor(), new Instructor());
        when(instructorRepository.findAll()).thenReturn(instructors);
        var result = instructorService.showAllInstructors();
        assertEquals(instructors, result);
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    public void testListAllCourses() {
        var instructorId = 1L;
        var course1 = new Course();
        course1.setInstructor(new Instructor(1L, "Dummy", "Dummy", "Dummy"));
        var course2 = new Course();
        course2.setInstructor(new Instructor(1L, "Dummy", "Dummy", "Dummy"));
        var courses = List.of(course1, course2);
        when(courseRepository.findAll()).thenReturn(courses);
        var result = instructorService.listAllCourses(instructorId);
        assertEquals(courses, result);
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    public void testGetStudentsByCourses() {
        long instructorId = 1L;
        long courseId = 2L;
        List<Enrollment> mockEnrollments = Arrays.asList(
                new Enrollment(1L, new Student(1L, "Student1", "password1", "S1"), new Course(1L, "Course1", "Description1", false, new Instructor())),
                new Enrollment(2L, new Student(2L, "Student2", "password2", "S2"), new Course(courseId, "Course2", "Description2", false, new Instructor())),
                new Enrollment(3L, new Student(3L, "Student3", "password3", "S3"), new Course(courseId, "Course2", "Description2", false, new Instructor()))
        );
        when(enrollmentRepository.findAll()).thenReturn(mockEnrollments);
        List<Student> result = instructorService.getStudentsByCourses(instructorId, courseId);
        List<Student> expectedStudents = Arrays.asList(
                new Student(2L, "Student2", "password2", "S2"),
                new Student(3L, "Student3", "password3", "S3")
        );
        assertEquals(expectedStudents, result);
    }

    @Test
    public void testAuthenticateInstructor() {
        String username = "testUser";
        String password = "testPassword";
        Instructor mockInstructor = new Instructor(1L, username, password, "Test Instructor");
        when(instructorRepository.findByUsernameAndPassword(username, password)).thenReturn(mockInstructor);
        Instructor result = instructorService.authenticateInstructor(username, password);
        assertEquals(mockInstructor, result);
    }

}
