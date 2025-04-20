package Data.Translation_Service.service;

import java.util.List;
import java.util.Map;

public interface TranslationService {

    public Map<String, String> getTranslation(List<String> translationKeys, Map<String, Double> languages);
}
