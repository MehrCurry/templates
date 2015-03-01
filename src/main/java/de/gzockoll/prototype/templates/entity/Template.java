package de.gzockoll.prototype.templates.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity @EqualsAndHashCode(exclude = "id") @ToString
public class Template {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Size(min = 2,max = 2)
    private String language;

    private Template() {
    }

    public Template(@Size(min = 2,max = 2) String language) {
        this.language = language;
    }

    public TemplateState state=TemplateState.EDITABLE;

    public long getId() {
        return id;
    }

    public Template requestApproval() {
        state=state.requestApproval();
        return this;
    }

    public boolean isApproved() {
        return state==TemplateState.APPROVED;
    }

    public Template approve() {
        state=state.approve();
        return this;
    }

    public String getLanguage() {
        return language;
    }
}
