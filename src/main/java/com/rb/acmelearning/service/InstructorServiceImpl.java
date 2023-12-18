package com.rb.acmelearning.service;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Enrollment;
import com.rb.acmelearning.model.Instructor;
import com.rb.acmelearning.model.Student;
import com.rb.acmelearning.repository.CourseRepository;
import com.rb.acmelearning.repository.EnrollmentRepository;
import com.rb.acmelearning.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService{

    @Autowired
    InstructorRepository instructorRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Override
    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @Override
    public List<Instructor> showAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public List<Course> listAllCourses(long instructorId) {
        List<Course> courses = courseRepository.findAll();
        List<Course> returnCourses = new ArrayList<>();
        for (Course course : courses) {
            if (course.getInstructor() != null && course.getInstructor().getInstructorId() == instructorId)
                returnCourses.add(course);
        }

        return returnCourses;
    }

    @Override
    public List<Student> getStudentsByCourses(long instructorId, long courseId) {
        List<Student> students = new ArrayList<>();

        for (Enrollment enrollment : enrollmentRepository.findAll()) {
            if (enrollment.getCourse().getCourseId().equals(courseId)) {
                students.add(enrollment.getStudent());
            }
        }

        return students;
    }

    @Override
    public Instructor authenticateInstructor(String username, String password) {
        return instructorRepository.findByUsernameAndPassword(username, password);
    }
}
