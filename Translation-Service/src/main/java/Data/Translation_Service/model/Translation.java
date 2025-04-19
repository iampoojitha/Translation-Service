package Data.Translation_Service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "translations")
@Entity
public class Translation {

    private static final String ID = "id";
    private static final String TRANSLATION_KEY = "translation_key";
    private static final String TRANSLATION_VALUE = "translation_value";
    private static final String LANGUAGE = "language";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = TRANSLATION_KEY)
    private String translationKey;

    @Column(name = TRANSLATION_VALUE)
    private String translationValue;

    @Column(name = LANGUAGE)
    private String language;

    @Column(name = CREATED_AT)
    private LocalDateTime createdAt;

    @Column(name = UPDATED_AT)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onPrePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
