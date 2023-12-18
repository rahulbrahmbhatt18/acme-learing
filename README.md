# ACME Learning

ACME Learning is an online education platform built on Java SpringBoot and REST APIs that helps Instructors to create Courses and make them available for Students to enroll. Happy Learning with ACME :)

### Use Cases:
- [x] Instructors and Students can sign up to the system
- [x] Instructors and Students can log in to the system
- [x] Instructors can create new Courses
- [x] Instructors can list only their Courses
- [x] Instructors can start Courses, after this time no enrollments are allowed
- [x] Instructors can cancel non started Courses
- [x] Students can list all the Courses
- [x] Students can enroll to any non-started Course
- [x] Students can drop a Course
- [x] Students can list all the Courses they are enrolled to
- [x] Instructors can list enrolled students in a Course

### Non Functional Requirements:
- [x] Publish the project on your personal github account as a public repository
- [x] Commit often so we can evaluate how you make progress and how you fix issues
- [x] You must use SpringBoot 2.x and Java 8+
-  Used SpringBoot 2.7.0 and followed Java 8 coding conventions to build this project. 
- [x] Maven or Gradle is required to build the project
-  Used Maven to build this project.
- [x] Use any embedded database
-  Used embedded H2 (file based, SQL) database to build the project. 
- [x] Use embedded tomcat
-  Used embedded tomcat server to host the project. 
- [x] Your project must contain tests that run as part of the build process. Feel free to use any
framework you like.
- [x] Create a README file with instructions to clone, build and run your project. Include a
section with details about how you meet the business requirements.
- [x] Frontend is optional. If you feel that the API is able to handle all use cases, just provide a
Swagger file or a Postman collection so the reviewer can easily test your API.
- Created documentation for APIs using Postman collection.

### Test Coverage:
![test-coverage-1](https://github.com/rahulbrahmbhatt18/acme-learning/assets/44444106/d1b1bd56-e856-474c-8fe3-2442e0e978ae)

![test-coverage-2](https://github.com/rahulbrahmbhatt18/acme-learning/assets/44444106/4266a695-d46c-4e7c-9289-8d372d951872)

![test-coverage-3](https://github.com/rahulbrahmbhatt18/acme-learning/assets/44444106/94efa184-1b94-46c1-ae64-1cfc544648c8)

### API Documentation (Postman Collection):
[Course API Postman Documentation](https://documenter.getpostman.com/view/30262407/2s9YknAhb5)

[Instructor API Postman Documentation](https://documenter.getpostman.com/view/30262407/2s9Ykobfqk)

[Student API Postman Documentation](https://documenter.getpostman.com/view/30262407/2s9Ykobfqo)

### Instructions to clone, build, and run acme-learning (Project):

#### Clone:
To clone the project to your local machine, use the following command:

git clone [https://github.com/rahulbrahmbhatt18/acme-learning](https://github.com/rahulbrahmbhatt18/acme-learning)

#### Build:
To build the project, navigate to the project directory and use the following command:

mvn clean install 

(Assuming that you are using Maven as your build tool. Adjust the command if you are using a different build tool.)

#### Run:
To run the project, use the following command:

java -jar target/acme-learning.jar

### Business Requirements:

#### Course Management:
The Acme Learning System allows instructors to create, start, and cancel courses. It meets all the business requirements for course management.

#### Student Enrollment:
Students can sign up, log in, enroll in courses, and drop courses. The system satisfies all the business requirements related to student enrollment.

#### Instructor Management:
Instructors can sign up, log in, and manage courses. The Acme Learning System fulfills all the business requirements for instructor management.

#### Interaction and Reporting:
The platform provides interactions between students and instructors, such as viewing enrolled courses. It also includes reporting features for tracking course details.
