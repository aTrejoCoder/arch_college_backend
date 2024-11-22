package microservice.subject_service.Service.Implementations;

import microservice.subject_service.Model.Career;
import microservice.subject_service.Model.ElectiveSubject;
import microservice.subject_service.Model.ObligatorySubject;
import microservice.subject_service.Repository.ElectiveSubjectRepository;
import microservice.subject_service.Repository.ObligatorySubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyGenerationService {

    private final ObligatorySubjectRepository obligatorySubjectRepository;
    private final ElectiveSubjectRepository electiveSubjectRepository;

    @Autowired
    public KeyGenerationService(ObligatorySubjectRepository obligatorySubjectRepository, ElectiveSubjectRepository electiveSubjectRepository) {
        this.obligatorySubjectRepository = obligatorySubjectRepository;
        this.electiveSubjectRepository = electiveSubjectRepository;
    }

    public String generateCareerKey(Career career) {
        String careerName = career.getName().toUpperCase();
        StringBuilder key = new StringBuilder();

        if (careerName.contains(" ")) {
            String[] words = careerName.split(" ");
            for (String word : words) {
                key.append(word.charAt(0));
            }
        } else  {
            key.append(careerName, 0, 3);
        }
        return key.toString();
    }

    /*
    Key for Elective Subjects will have 4 numbers --> example : 151
    (1) means the ID from professional line id
    (50) means the number of subject from professional line
    */
    public String generateSubjectKey(ElectiveSubject subject) {
        int professionalLineIdLastDigit = Math.toIntExact(subject.getProfessionalLine().getId());
        int subjectSequence = electiveSubjectRepository.countByProfessionalLineId(subject.getProfessionalLine().getId()) + 1;

        return String.format("%d%02d", professionalLineIdLastDigit, subjectSequence);
    }

    /*
    Key for Obligatory Subjects will have 4 numbers --> example : 1203
     (1) means the id of career (architecture careers can't grow above 10, there just 4 in current system and can't get deleted)
     (2) means the number of the semester that the subject belongs (10 semester subject will be considered as 0)
     (03) means the number of subject from some semester
     */
    public String generateSubjectKey(ObligatorySubject subject) {
        int careerIdLastDigit = Math.toIntExact(subject.getCareer().getId());
        int semesterNumber = subject.getSemester() <= 9 ? subject.getSemester() : 0;
        int subjectSequence = obligatorySubjectRepository.findBySemester(subject.getSemester()).size();

        return String.format("%d%d%02d", careerIdLastDigit, semesterNumber, subjectSequence);
    }
}
