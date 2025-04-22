package Data.Translation_Service.repository;

import Data.Translation_Service.model.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TranslationRepo extends JpaRepository<Translation, Long> {

    @Query("SELECT t FROM Translation  t WHERE t.translationKey IN :keys AND t.language = :language")
    List<Translation> findAllByTranslationKeyInAndLanguage(@Param("keys") Set<String> keys, @Param("language") String language);
}
