package Data.Translation_Service.controller;

import Data.Translation_Service.config.AppConfig;
import Data.Translation_Service.dto.TranslationDto;
import Data.Translation_Service.dto.TranslationRequestDto;
import Data.Translation_Service.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(AppConfig.API)
@RequiredArgsConstructor
public class TranslationRoute {

    private final TranslationService translationService;

    @PostMapping(AppConfig.GET_TRANSLATION)
    public Map<String, String> getTranslation(@RequestBody TranslationRequestDto request,
                                              @RequestHeader(name = "Accept-Language", required = false) String languageHeader) {
        try {
            return translationService.getTranslation(request, languageHeader);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(AppConfig.UPDATE_TRANSLATION)
    public TranslationDto updateTranslation(@RequestBody TranslationDto translationDto) {
        try {
            return translationService.updateTranslation(translationDto);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
