package Data.Translation_Service.controller;

import Data.Translation_Service.config.AppConfig;
import Data.Translation_Service.dto.TranslationDto;
import Data.Translation_Service.dto.TranslationRequestDto;
import Data.Translation_Service.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContext;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(AppConfig.API)
@RequiredArgsConstructor
public class TranslationRoute {

    private final TranslationService translationService;
    private RequestContext requestContext;

    @PostMapping(AppConfig.GET_TRANSLATION)
    public Map<String, String> getTranslation(@RequestBody TranslationRequestDto request,
                                              @RequestHeader(name = "Accept-Language", required = false) String languageHeader) {
        final Locale locale = requestContext.getLocale();
        return translationService.getTranslation(request, locale.getLanguage());

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
