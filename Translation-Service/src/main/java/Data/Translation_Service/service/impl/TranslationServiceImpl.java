package Data.Translation_Service.service.impl;

import Data.Translation_Service.dto.TranslationDto;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepo translationRepo;
    private final RedisTemplate<String, TranslationDto> redisTemplate;
    private static final String DEFAULT_LANGUAGE = "en";
    private static final String DEFAULT_USERNAME = "anonymous";

    @Value("${redis.expiry.time}")
    private Long expiryTime;

    @Override
    public Map<String, String> getTranslation(List<String> translationKeys, Map<String, Double> languages, String username) {
        Map<String, String> response = new HashMap<>();
        try {
            username = validateUsername(username);
            String finalUsername = username;
            if (translationKeys == null || translationKeys.isEmpty()) {
                throw new NullPointerException("Translation keys cannot be null");
            }

            String highestLanguage = getHighestValueLanguage(languages);
            for (String translationKey : translationKeys) {
                String cacheKey = translationKey + "_" + highestLanguage;
                if (redisTemplate.hasKey(cacheKey)) {
                    TranslationDto translationDto = Objects.requireNonNull(redisTemplate.opsForValue().get(cacheKey));
                    String translationValue = translationDto.getTranslationValue();
                    if (translationDto.getIsTemplate()) {
                        translationValue = translationDto.getTranslationValue().replace("{{username}}", finalUsername);
                    }
                    response.put(translationKey, translationValue);
                } else {
                    translationRepo.findByTranslationKeyAndLanguage(translationKey, highestLanguage)
                            .ifPresent(translation -> {
                                String value = translation.getTranslationValue();
                                if (translation.getIsTemplate()) {
                                    value = translation.getTranslationValue().replace("{{username}}", finalUsername);
                                }
                                response.put(translationKey, value);
                                TranslationDto translationValue = new TranslationDto(translation.getTranslationValue(), translation.getIsTemplate());
                                redisTemplate.opsForValue().set(cacheKey, translationValue, Duration.ofMinutes(expiryTime));
                            });
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static String validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            return DEFAULT_USERNAME;
        }
        return username;
    }

    public static String getHighestValueLanguage(Map<String, Double> languages) {
        String highestLanguage = DEFAULT_LANGUAGE;
        if (languages != null && !languages.isEmpty()) {
            double highestValue = Double.MIN_VALUE;
            for (var language : languages.entrySet()) {
                if (language.getValue() > highestValue) {
                    highestValue = language.getValue();
                    highestLanguage = language.getKey();
                }
            }
        }
        return highestLanguage;
    }
}
