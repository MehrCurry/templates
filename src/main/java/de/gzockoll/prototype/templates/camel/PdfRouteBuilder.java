package de.gzockoll.prototype.templates.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@Service
public class PdfRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:xml2pdf").
                recipientList(simple("xslt:http://localhost:9090/assets/raw/${header.templateId}?contentCache=false")).
                to("fop:application/pdf");
    }
}
