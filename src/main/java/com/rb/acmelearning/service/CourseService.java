package com.rb.acmelearning.service;

import com.rb.acmelearning.model.Course;

import java.util.List;

public interface CourseService {
    Course createCourse(Course course, Long instructorId);

    Course startCourse(Long instructorId, Long courseId);

    void cancelCourse(Long instructorId, Long courseId);

    boolean validateCourseAndInstructor(long courseId, long instructorId);

    List<Course> getAllCourses();

    Course enrollCourse(long studentId, long courseId);
}
