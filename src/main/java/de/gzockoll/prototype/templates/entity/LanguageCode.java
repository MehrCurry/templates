package de.gzockoll.prototype.templates.entity;

import de.gzockoll.prototype.templates.validation.ValidISOLanguageCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Locale;

@Embeddable
@EqualsAndHashCode @Getter
public class LanguageCode extends ValidateableObject {
    @ValidISOLanguageCode
    private String code;

    public LanguageCode() {
        this.code= Locale.getDefault().getLanguage();
    }

    public LanguageCode(@ValidISOLanguageCode String code) {
        this.code = code;
    }
}
