package microservice.subject_service.Repository;

import microservice.subject_service.Model.SocialNetwork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialNetworksRepository extends JpaRepository<SocialNetwork, Long> {

}
