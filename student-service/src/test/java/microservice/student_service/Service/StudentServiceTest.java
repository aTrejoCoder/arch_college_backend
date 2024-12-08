package microservice.student_service.Service;

import microservice.student_service.Model.Student;
import microservice.student_service.Repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class StudentServiceTest {

    /*
    public final StudentRepository studentRepository;
    public final StudentCommandService studentService;

    @Autowired
    public StudentServiceTest(StudentRepository studentRepository,
                              StudentCommandService studentService) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    @Test
    public void testGetStudentById() {
        Student student = new Student();
        Mockito.when(studentRepository.findById(1)).thenReturn(Optional.of(student));


        Student result = studentService.getStudentById(1);

        // Verifica el resultado
        assertNotNull(result);
        assertEquals("Alex", result.getName());
        Mockito.verify(studentRepository).findById(1); // Verifica que se llamó a findById en el mock
    }

    */
}

