package Data.Translation_Service.service.impl;

import Data.Translation_Service.dto.TranslationDto;
import Data.Translation_Service.dto.TranslationRequestDto;
import Data.Translation_Service.model.Translation;
import Data.Translation_Service.repository.TranslationRepo;
import Data.Translation_Service.service.TranslationService;
import Data.Translation_Service.util.TemplateUtil;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepo translationRepo;
    private static final String DEFAULT_LANGUAGE = "en";

    ModelMapper modelMapper = new ModelMapper();

    @Override
    @Cacheable(value = "translations", key = "T(java.util.Objects).hash(#request.getTranslationKeys().stream().sorted().collect(T(java.util.stream.Collectors).toList()).toString() + '_' + #language + '_' + #request.getTemplateData())")
    public Map<String, String> getTranslation(TranslationRequestDto request, String language) {
        Map<String, String> response = new HashMap<>();

        Set<String> keys = request.getTranslationKeys();

        List<Translation> translations = translationRepo.findAllByTranslationKeyInAndLanguage(keys, language);

        for (Translation translation : translations) {
            String value = translation.getTranslationValue();
            if (Boolean.TRUE.equals(translation.getIsTemplate())) {
                try {
                    value = TemplateUtil.renderTemplateText(value, request.getTemplateData());
                } catch (IOException | TemplateException e) {
                    log.error("Error rendering template for translation key: " + translation.getTranslationKey(), e);
                }
            }
            response.put(translation.getTranslationKey(), value);
        }
        return response;
    }

    @Override
    public TranslationDto updateTranslation(TranslationDto translationDto) {
        Translation translation = translationRepo.findById(translationDto.getId()).orElse(null);
        if (translation != null) {
            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(translationDto, translation);
            translationRepo.save(translation);
        }
        return translationDto;
    }
}
