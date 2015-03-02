package de.gzockoll.prototype.templates.control;

import com.google.common.collect.ImmutableMap;
import de.gzockoll.prototype.templates.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Transactional
@Controller
@Slf4j
public class TemplateService {

    @Autowired
    private TemplateRepository repository;

    @Autowired
    private TemplateGroupRepository groupRepository;

    @EndpointInject(uri="direct:xml2pdf")
    ProducerTemplate producer;

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

    public void updateTemplate(Template t) {
        repository.save(t);
    }
    public byte[] preview(Template t) throws IOException {
        String data= new String(Files.readAllBytes(Paths.get("camel/vorlage/dataset.xml")), Charset.forName("UTF-8"));
        final Map<String, Object> headers = ImmutableMap.<String, Object>builder()
                .put("templateId", 1L)
                .put("stationeryId", 2L)
                .build();
        return (byte[]) producer.requestBodyAndHeaders(data, headers);
    }

}
