package de.gzockoll.prototype.templates.entity;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Entity @EqualsAndHashCode(exclude = "id") @ToString
public class Template {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Size(min = 2,max = 2)
    private String language;

    private Content content;

    private Template() {
    }

    public Template(@Size(min = 2, max = 2) String language) {
        this.language = language;
    }

    private TemplateState state=TemplateState.EDITABLE;

    private TemplateType type;


    public long getId() {
        return id;
    }

    public Template requestApproval() {
        Preconditions.checkState(content!=null);
        state=state.requestApproval();
        return this;
    }

    public boolean isApproved() {
        return state==TemplateState.APPROVED;
    }

    public Template approve() {
        Preconditions.checkState(content!=null);
        state=state.approve();
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public Template saveContent(InputStream is) {
        Preconditions.checkNotNull(is);
        state=state.saveContent(this,is);
        return this;
    }

    void setContent(InputStream input) {
        content=new Content(input);
    }

    public Template saveContent(String s) {
        return saveContent(new ByteArrayInputStream(s.getBytes()));
    }
}
