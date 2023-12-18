package com.rb.acmelearning.controller;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.service.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    @Test
    @DisplayName("Test blank endpoint method to fetch list of all courses - Success")
    public void testGetAllCoursesSuccess() {
        List<Course> mockCourses = Arrays.asList(new Course(), new Course());
        when(courseService.getAllCourses()).thenReturn(mockCourses);

        ResponseEntity<List<Course>> response = courseController.getAllCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourses, response.getBody());
        verify(courseService, times(1)).getAllCourses();
    }

    @Test
    @DisplayName("Test blank endpoint method to fetch list of all courses - No Courses Found")
    public void testGetAllCoursesNoCoursesFound() {
        when(courseService.getAllCourses()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Course>> response = courseController.getAllCourses();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ArrayList<>(), response.getBody());
        verify(courseService, times(1)).getAllCourses();
    }
}

