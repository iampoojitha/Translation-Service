package Data.Translation_Service.dto;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationRequestDto {

    @NotNull(message = "translation keys should be there")
    private Set<String> translationKeys;

    @Nullable
    private Map<String, String> templateData;
}
