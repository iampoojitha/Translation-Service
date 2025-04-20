package Data.Translation_Service.controller;

import Data.Translation_Service.config.AppConfig;
import Data.Translation_Service.service.TranslationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(AppConfig.API)
@RequiredArgsConstructor
public class TranslationRoute {

    private final TranslationService translationService;

    @PostMapping(AppConfig.GET_TRANSLATION)
    public ResponseEntity<Map<String, String>> getTranslation(@RequestBody List<String> translationKeys,
                                                              @RequestHeader(name = "Language", required = false) String languageHeader,
                                                              @RequestHeader(name = "username", required = false) String username) {
        try {
            Map<String, Double> languages = new HashMap<>();
            if (languageHeader != null && !languageHeader.trim().isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                languages = mapper.readValue(languageHeader, new TypeReference<>() {});
            }
            return ResponseEntity.ok(translationService.getTranslation(translationKeys, languages, username));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }

}
