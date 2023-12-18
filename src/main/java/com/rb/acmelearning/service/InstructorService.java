package com.rb.acmelearning.service;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Instructor;
import com.rb.acmelearning.model.Student;

import java.util.List;

public interface InstructorService {
    Instructor createInstructor(Instructor instructor);

    List<Instructor> showAllInstructors();

    List<Course> listAllCourses(long instructorId);

    List<Student> getStudentsByCourses(long instructorId, long courseId);

    Instructor authenticateInstructor(String username, String password);
}
