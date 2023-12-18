package com.rb.acmelearning.service;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Student;

import java.util.List;

public interface StudentService {
    Student createStudent(Student student);

    List<Student> showAllStudents();

    Student authenticateStudent(String username, String password);

    List<Course> listEnrolledCourses(long studentId);

    boolean dropCourse(long studentId, long courseId);
}
