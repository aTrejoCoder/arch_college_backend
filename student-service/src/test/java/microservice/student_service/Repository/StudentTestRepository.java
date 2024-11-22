package microservice.student_service.Repository;

import microservice.student_service.Model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
public class StudentTestRepository {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentTestRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public static Student getTestStudent() {
        Student testStudent = new Student();
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setDateOfBirth(LocalDateTime.of(2000, 1, 1, 0, 0));
        testStudent.initializeAcademicValues("123456", "2024");
        testStudent.setCreatedAt(LocalDateTime.now());
        testStudent.setUpdatedAt(LocalDateTime.now());
        return testStudent;
    }

    @Test
    public void testCreateStudent() {
        Student student = getTestStudent();
        Student savedStudent = studentRepository.save(student);
        assertNotNull(savedStudent.getId());
        assertEquals("John", savedStudent.getFirstName());
    }

    @Test
    public void testReadStudent() {
        Student student = getTestStudent();
        Student savedStudent = studentRepository.save(student);
        Student foundStudent = studentRepository.findById(savedStudent.getId()).orElse(null);
        assertNotNull(foundStudent);
        assertEquals(savedStudent.getId(), foundStudent.getId());
    }

    @Test
    public void testUpdateStudent() {
        Student student = getTestStudent();
        Student savedStudent = studentRepository.save(student);

        savedStudent.setFirstName("Jane");
        Student updatedStudent = studentRepository.save(savedStudent);

        assertEquals("Jane", updatedStudent.getFirstName());
    }

    @Test
    public void testDeleteStudent() {
        Student student = getTestStudent();
        Student savedStudent = studentRepository.save(student);

        studentRepository.deleteById(savedStudent.getId());

        assertFalse(studentRepository.findById(savedStudent.getId()).isPresent());
    }
}


