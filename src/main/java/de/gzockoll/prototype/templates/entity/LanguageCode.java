package de.gzockoll.prototype.templates.entity;

import de.gzockoll.prototype.templates.validation.ValidISOLanguageCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode @Getter
public class LanguageCode extends ValidateableObject {
    @ValidISOLanguageCode
    private String code;

    private LanguageCode() {
    }

    public LanguageCode(@ValidISOLanguageCode String code) {
        this.code = code;
    }
}
