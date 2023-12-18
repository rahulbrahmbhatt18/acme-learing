package com.rb.acmelearning.service;

import com.rb.acmelearning.model.Course;
import com.rb.acmelearning.model.Enrollment;
import com.rb.acmelearning.model.Instructor;
import com.rb.acmelearning.model.Student;
import com.rb.acmelearning.repository.CourseRepository;
import com.rb.acmelearning.repository.EnrollmentRepository;
import com.rb.acmelearning.repository.InstructorRepository;
import com.rb.acmelearning.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService{
    @Autowired
    InstructorRepository instructorRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Override
    public Course createCourse(Course course, Long instructorId) {
        var instructorDb = this.instructorRepository.findById(instructorId);

        if (instructorDb.isPresent()) {
            course.setInstructor(instructorDb.get());
            courseRepository.save(course);
            return  course;
        }
        return null;
    }

    @Override
    public Course startCourse(Long instructorId, Long courseId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);

        if (instructor != null && course != null && !course.isCourseStarted()) {
            course.setCourseStarted(true);
            return courseRepository.save(course);
        }
        return null;
    }

    @Override
    public void cancelCourse(Long instructorId, Long courseId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);

        if (instructor != null && course != null) {
            if (course.isCourseStarted()) {
                course.setCourseStarted(false);
                courseRepository.save(course);
            } else {
                courseRepository.delete(course);
            }
        }
    }

    @Override
    public boolean validateCourseAndInstructor(long courseId, long instructorId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        Optional<Instructor> instructorOptional = instructorRepository.findById(instructorId);

        return courseOptional.isPresent() && instructorOptional.isPresent() &&
                courseOptional.get().getInstructor().getInstructorId() == instructorId;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course enrollCourse(long studentId, long courseId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (studentOptional.isPresent() && courseOptional.isPresent() && !courseOptional.get().isCourseStarted()) {
            Student student = studentOptional.get();
            Course course = courseOptional.get();

                Enrollment enrollment = new Enrollment();
                enrollment.setStudent(student);
                enrollment.setCourse(course);

                enrollmentRepository.save(enrollment);

                return course;

        }

        return null;
    }
}
