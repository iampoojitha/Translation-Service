package Data.Translation_Service.repository;

import Data.Translation_Service.model.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslationRepo extends JpaRepository<Translation, Long> {

    public Optional<Translation> findByTranslationKeyAndLanguage(String translationKey, String language);
}
