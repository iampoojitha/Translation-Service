package Data.Translation_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationDto implements Serializable {
    private String translationValue;
    private Boolean isTemplate;
}
