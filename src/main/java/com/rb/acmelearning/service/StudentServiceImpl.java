package com.rb.acmelearning.service;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Enrollment;
import com.rb.acmelearning.model.Student;
import com.rb.acmelearning.repository.EnrollmentRepository;
import com.rb.acmelearning.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> showAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student authenticateStudent(String username, String password) {
        return studentRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public List<Course> listEnrolledCourses(long studentId) {
        List<Course> courses = new ArrayList<>();
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        for (Enrollment enrollment : enrollments) {
            if (enrollment != null && enrollment.getStudent().getStudentId() == studentId) {
                courses.add(enrollment.getCourse());
            }
        }
        return courses;
    }

    @Override
    public boolean dropCourse(long studentId, long courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent().getStudentId() == studentId && enrollment.getCourse().getCourseId() == courseId) {
                enrollmentRepository.delete(enrollment);
                return true;
            }
        }
        return false;
    }

}
