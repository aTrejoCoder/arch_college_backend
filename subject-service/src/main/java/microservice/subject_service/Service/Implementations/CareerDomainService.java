package microservice.subject_service.Service.Implementations;

import microservice.subject_service.Model.Career;
import org.jvnet.hk2.annotations.Service;

@Service
public class CareerDomainService {

    public String generateKey(Career career) {
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
}
