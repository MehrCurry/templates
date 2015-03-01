package de.gzockoll.prototype.templates.entity;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Entity @EqualsAndHashCode @ToString
public class TemplateGroup {
    @EmbeddedId
    private TemplateGroupPK id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Template> templates=new ArrayList<>();

    @OneToOne
    private Template activeTemplate=null;

    private TemplateGroup() {};

    public TemplateGroup(long tenantId,String language, String qualifier) {
        this.id=new TemplateGroupPK(tenantId,language,qualifier);
    }

    public void addTemplate(Template t) {
        Preconditions.checkArgument(hasSameLanguageAs(t));
        templates.add(t);
    }

    public void activate(Template t) {
        Preconditions.checkArgument(templates.contains(t), "Template does not belong to this group");
        Preconditions.checkArgument(t.isApproved(),"Template must have been approved before activating");
        activeTemplate=t;
    }

    public Optional<Template> getActiveTemplate() {
        return Optional.ofNullable(activeTemplate);
    }

    public boolean hasSameLanguageAs(Template t) {
        Preconditions.checkNotNull(t);
        return t.getLanguage().equals(id.getLanguage());
    }
}
