package com.rb.acmelearning.repository;

import com.rb.acmelearning.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
