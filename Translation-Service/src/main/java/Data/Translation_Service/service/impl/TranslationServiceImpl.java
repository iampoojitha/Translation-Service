package Data.Translation_Service.service.impl;

import Data.Translation_Service.repository.TranslationRepo;
import Data.Translation_Service.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepo translationRepo;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${redis.expiry.time}")
    private Long expiryTime;

    @Override
    public Map<String, String> getTranslation(List<String> translationKeys, Map<String, Double> languages) {
        Map<String, String> response = new HashMap<>();
        try {
            if (translationKeys == null || translationKeys.isEmpty()) {
                throw new NullPointerException("Translation keys cannot be null");
            }

            String highestLanguage = "en";
            if (languages != null && !languages.isEmpty()) {
                double highestValue = Double.MIN_VALUE;
                for (var language : languages.entrySet()) {
                    if (language.getValue() > highestValue) {
                        highestValue = language.getValue();
                        highestLanguage = language.getKey();
                    }
                }
            }
            for (String translationKey : translationKeys) {
                String cacheKey = translationKey + "_" + highestLanguage;

                if (redisTemplate.hasKey(cacheKey)) {
                    response.put(translationKey, redisTemplate.opsForValue().get(cacheKey));
                    continue;
                }
                translationRepo.findByTranslationKeyAndLanguage(translationKey, highestLanguage)
                        .ifPresent(translation -> {
                            response.put(translationKey, translation.getTranslationValue());
                            redisTemplate.opsForValue().set(cacheKey, translation.getTranslationValue(), Duration.ofMinutes(expiryTime));
                        });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
