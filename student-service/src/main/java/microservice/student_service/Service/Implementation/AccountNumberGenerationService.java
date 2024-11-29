package microservice.student_service.Service.Implementation;

import microservice.student_service.Model.Student;
import microservice.student_service.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountNumberGenerationService {

    private static final int FIRST_GENERATION_YEAR = 2024;
    private static final int FIRST_GENERATION_NUMBER = 1;
    private final StudentRepository studentRepository;

    @Autowired
    public AccountNumberGenerationService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Account numbers are auto incremental and are a composition of 9 numbers
    // Example: 31529710-4
    //  (31) means generation number --> (529710) means studentId --> (4) means check digit
    public String generateStudentAccountNumber(Student student) {
        Optional<Long> optionalLastId = studentRepository.findLastId();
        StringBuilder newAccountNumber = new StringBuilder();

        int requestedYear = LocalDateTime.now().getYear();
        validateYear(requestedYear);

        int generationNumber = calculateGenerationNumber(requestedYear);

        String formattedGenerationNumber = String.format("%02d", generationNumber);  // Ensures 2-digit formatting
        newAccountNumber.append(formattedGenerationNumber);

        // If last student doesn't exist, start count at "000001"
        if (optionalLastId.isEmpty()) {
            newAccountNumber.append("000001");
        } else {
            Long nextId = optionalLastId.get() + 1;
            String formattedId = String.format("%06d", nextId); // Ensures 6-digit formatting
            newAccountNumber.append(formattedId);
        }

        newAccountNumber.append("-");

        int checkDigit = calculateCheckDigit(newAccountNumber.toString());
        newAccountNumber.append(checkDigit);

        return newAccountNumber.toString();
    }

    private int calculateCheckDigit(String accountNumber) {
        String numericPart = accountNumber.replace("-", "");

        int sum = 0;
        for (char c : numericPart.toCharArray()) {
            sum += Character.getNumericValue(c);
        }

        // Modulus 10 to get a single-digit check digit
        return sum % 10;
    }


    private void validateYear(int requestedYear) {
        if (requestedYear < FIRST_GENERATION_YEAR) {
            throw new RuntimeException("Not supported date provided to assign generation to student");
        }
    }

    private int calculateGenerationNumber(int requestedYear) {
        return FIRST_GENERATION_NUMBER + (requestedYear - FIRST_GENERATION_YEAR);
    }
}