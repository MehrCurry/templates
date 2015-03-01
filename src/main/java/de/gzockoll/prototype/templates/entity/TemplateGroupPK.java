package de.gzockoll.prototype.templates.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable @EqualsAndHashCode @ToString
public class TemplateGroupPK implements Serializable {
    private long tenantId;
    private String language;
    private String qualifier;

    private TemplateGroupPK() {

    }

    public TemplateGroupPK(long tenantId, String language, String qualifier) {
        this.tenantId = tenantId;
        this.language = language;
        this.qualifier = qualifier;
    }

    public long getTenantId() {
        return tenantId;
    }

    public String getLanguage() {
        return language;
    }

    public String getQualifier() {
        return qualifier;
    }
}
