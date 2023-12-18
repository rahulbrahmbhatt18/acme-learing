package com.rb.acmelearning.service;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Instructor;
import com.rb.acmelearning.repository.CourseRepository;
import com.rb.acmelearning.repository.InstructorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @InjectMocks
    private CourseServiceImpl courseService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Test
    public void testCreateCourseWithInstructorId() {
        long instructorId = 1L;
        var instructor = new Instructor();
        instructor.setInstructorId(instructorId);

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenReturn(new Course());

        var course = new Course();
        var result = courseService.createCourse(course, instructorId);
        assertEquals(instructor, result.getInstructor());
        verify(instructorRepository, times(1)).findById(instructorId);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    public void testStartCourse() {
        Long instructorId = 1L;
        Long courseId = 2L;
        Instructor mockInstructor = new Instructor(instructorId, "testUser", "testPassword", "Test Instructor");
        Course mockCourse = new Course(courseId, "Test Course", "Test Description", false, mockInstructor);

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(mockInstructor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(courseRepository.save(any())).thenReturn(mockCourse);


        Course result = courseService.startCourse(instructorId, courseId);

        assertTrue(result.isCourseStarted());
        verify(courseRepository, times(1)).save(mockCourse);
    }

    @Test
    void testCancelCourse() {
        long instructorId = 1L;
        long courseId = 2L;
        var instructor = new Instructor();
        instructor.setInstructorId(instructorId);

        var course = new Course();
        course.setCourseId(courseId);
        course.setInstructor(instructor);

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        courseService.cancelCourse(instructorId, courseId);

        verify(instructorRepository).findById(instructorId);
        verify(courseRepository).findById(courseId);
        if (course.isCourseStarted()) {
            verify(courseRepository).save(course);
        } else {
            verify(courseRepository).delete(course);
        }
    }

    @Test
    void testValidateCourseAndInstructor() {
        long courseId = 1L;
        long instructorId = 2L;
        Instructor instructor = new Instructor(instructorId, "testUser", "testPassword", "Test Instructor");
        Course course = new Course(courseId, "Test Course", "Test Description", false, instructor);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        boolean result = courseService.validateCourseAndInstructor(courseId, instructorId);

        verify(courseRepository).findById(courseId);
        verify(instructorRepository).findById(instructorId);
        assertTrue(result, "Validation should succeed for valid course and instructor");
    }

    @Test
    void testGetAllCourses() {
        Course course1 = new Course(1L, "Course 1", "Description 1", false, new Instructor());
        Course course2 = new Course(2L, "Course 2", "Description 2", false, new Instructor());
        List<Course> mockCourses = Arrays.asList(course1, course2);

        when(courseRepository.findAll()).thenReturn(mockCourses);

        List<Course> result = courseService.getAllCourses();

        verify(courseRepository).findAll();
        assertEquals(mockCourses, result, "Returned courses should match mock data");
    }

}
