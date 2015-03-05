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

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TemplatesApplication.class)   // 2
@WebAppConfiguration   // 3
@IntegrationTest("server.port:9090")
public class TemplateServiceIntegrationTest {

    @Autowired
    private TemplateService service;

    @Autowired
    private TemplateRepository repository;

    @Autowired
    private AssetRepository assets;

    @Autowired
    private AssetController assetController;


    @Test
    public void testGroup() {
        Asset anAsset=new Asset("JUnit".getBytes(),"junit.txt");
        assets.save(anAsset);
        Template t=new Template("de").assignTransform(anAsset).assignStationary(anAsset);
        TemplateGroup group = service.addTemplate(1, "de", "junit", t);
        assertThat(group).isNotNull();
    }

    @Test
    public void testDetachedApproval() {
        Template t = new Template("de");
        final Asset a = new Asset("junit".getBytes(),"junit.txt");
        assets.save(a);
        t.assignTransform(a).assignStationary(a).requestApproval().approve();
        service.updateTemplate(t);
    }

    @Test
    public void testPreview() throws IOException {
        Asset a1=new Asset("camel/vorlage/template.xsl");
        assets.save(a1);
        Asset a2 = new Asset("camel/vorlage/stationery.pdf");
        assets.save(a2);
        Template t=new Template("de").assignTransform(a1).assignStationary(a2).requestApproval().approve();
        byte[] result = service.preview(t);
        Tika tika=new Tika();
        assertThat(tika.detect(result)).isEqualTo("application/pdf");
    }
}