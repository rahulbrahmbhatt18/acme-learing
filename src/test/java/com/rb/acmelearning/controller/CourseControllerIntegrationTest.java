package com.rb.acmelearning.controller;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Instructor;
import com.rb.acmelearning.service.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private CourseService courseService;

    @Test
    public void testGetAllCourses() {
        Course course1 = new Course(1L, "Dummy Course", "Dummy Desc", false, new Instructor());
        Course course2 = new Course(2L, "Dummy Development", "Dummy Development", false, new Instructor());
        List<Course> courses = Arrays.asList(course1, course2);
        when(courseService.getAllCourses()).thenReturn(courses);
        var response = restTemplate.exchange(
                "/courses/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Course>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Dummy Course", response.getBody().get(0).getName());
        assertEquals("Dummy Development", response.getBody().get(1).getName());
        Mockito.verify(courseService, Mockito.times(1)).getAllCourses();
    }
}
