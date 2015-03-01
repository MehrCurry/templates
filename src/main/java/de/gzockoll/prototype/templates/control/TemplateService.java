package de.gzockoll.prototype.templates.control;

import de.gzockoll.prototype.templates.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;

@Transactional
@Controller
public class TemplateService {

    @Autowired
    private TemplateRepository repository;

    @Autowired
    private TemplateGroupRepository groupRepository;

    public void requestApproval(long templateId) {
        Template t=repository.findOne(templateId);
        t.requestApproval();
        repository.save(t);
    }

    public TemplateGroup addTemplate(long tenantId, String language, String qualifier, Template t) {
        TemplateGroupPK pk = new TemplateGroupPK(tenantId, language, qualifier);
        TemplateGroup group = groupRepository.findOne(pk);
        if (group==null) {
            group=new TemplateGroup(tenantId,language, qualifier);
        }

        group.addTemplate(t);
        return groupRepository.save(group);
    }
}
