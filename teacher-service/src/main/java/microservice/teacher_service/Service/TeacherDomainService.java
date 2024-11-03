package microservice.teacher_service.Service;

import microservice.teacher_service.Model.Teacher;
import microservice.teacher_service.Repository.TeacherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherDomainService {

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherDomainService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    /**
     * Generates a unique account number for a teacher.
     * Format example: 05291-4 where "05291" is the teacher ID and "4" is the check digit.
     *
     * @param teacher The teacher for whom the account number is generated
     * @return A formatted account number string
     */
    public String generateTeacherAccountNumber(Teacher teacher) {
        Optional<Long> optionalLastId = teacherRepository.findLastId();

        // Start count at "00001" if no last teacher exists
        String formattedId = optionalLastId.map(id -> String.format("%05d", id + 1))
                .orElse("00001");

        int checkDigit = calculateCheckDigit(formattedId);
        return formattedId + "-" + checkDigit;
    }

    /**
     * Calculates the check digit for the account number using the modulus 10 approach.
     *
     * @param accountNumber Numeric part of the account number as a string
     * @return A single-digit integer check digit
     */
    private int calculateCheckDigit(String accountNumber) {
        int sum = accountNumber.chars()
                .map(Character::getNumericValue)
                .sum();

        return sum % 10;
    }
}
