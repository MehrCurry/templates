package de.gzockoll.prototype.templates.control;

import de.gzockoll.prototype.templates.TemplatesApplication;
import de.gzockoll.prototype.templates.entity.*;
import org.apache.tika.Tika;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TemplatesApplication.class)   // 2
@WebAppConfiguration   // 3
@IntegrationTest("server.port:0")   // 4
public class TemplateServiceIntegrationTest {

    @Autowired
    private TemplateService service;

    @Autowired
    private TemplateRepository repository;

    @Autowired
    private AssetRepository assets;

    @Test
    public void testRequestApproval() throws Exception {
        repository.save(new Template("de"));

        Optional<Template> opt=repository.findByLanguage("de").stream().findFirst();
        assertThat(opt.isPresent()).isTrue();
        service.addContent(opt.get(),new ByteArrayInputStream("junit".getBytes()));
        service.requestApproval(opt.get().getId());
    }

    @Test
    public void testGroup() {
        Template t=new Template("de");
        TemplateGroup group = service.addTemplate(1, "de", "junit", t);
        assertThat(group).isNotNull();
    }

    @Test
    public void testDetachedApproval() {
        Template t = repository.save(new Template("de"));
        t.saveContent(new ByteArrayInputStream("junit".getBytes())).requestApproval().approve();
        service.updateTemplate(t);
    }

    @Test
    public void testPreview() throws IOException {
        Asset a=new Asset(new ByteArrayInputStream(Files.readAllBytes(Paths.get("camel/vorlage/template.xsl"))));
        assets.save(a);
        byte[] result = service.preview(a);
        Tika tika=new Tika();
        assertThat(tika.detect(result)).isEqualTo("application/pdf");
    }
}