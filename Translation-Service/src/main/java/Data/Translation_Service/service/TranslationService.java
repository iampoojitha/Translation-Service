package Data.Translation_Service.service;

import Data.Translation_Service.dto.TranslationDto;
import Data.Translation_Service.dto.TranslationRequestDto;

import java.util.List;
import java.util.Map;

public interface TranslationService {

    public Map<String, String> getTranslation(TranslationRequestDto request, String languageHeader);

    public TranslationDto updateTranslation(TranslationDto translationDto);
}
