package microservice.subject_service.Config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "areaByIdCache",
                "areaWithSubjectsCache",
                "areaByNameCache",
                "allAreasCache",
                "careerByNameCache",
                "allCareersCache",
                "careerByIdCache",
                "obligatorySubjectByIdCache",
                "electiveSubjectByIdCache",
                "professionalLineByIdCache",
                "electiveSubjectByNameCache",
                "electiveSubjectsByAreaIdCache",
                "electiveSubjectsByFilterCache",
                "allElectiveSubjectsCache",
                "obligatorySubjectByNameCache",
                "obligatorySubjectsByFilterCache",
                "allObligatorySubjectsCache",
                "professionalLineWithSubjectsCache",
                "professionalLineWithSubjectsCache",
                "professionalLineByNameCache",
                "allProfessionalLinesCache"
                );
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(1, TimeUnit.DAYS));
        cacheManager.setAllowNullValues(false);
        cacheManager.setAsyncCacheMode(true);
        return cacheManager;
    }
}