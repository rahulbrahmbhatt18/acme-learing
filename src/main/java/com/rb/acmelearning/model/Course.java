package com.rb.acmelearning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue
    private Long courseId;
    private String name;
    private String description;
    private boolean courseStarted;
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
}
