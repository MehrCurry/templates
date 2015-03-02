package de.gzockoll.prototype.templates.camel;

import com.google.common.io.ByteStreams;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.pdfbox.multipdf.Overlay;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;

public class OverlayProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String inputFileName=exchange.getIn().getHeader("CamelFileAbsolutePath",String.class);
        String overlayFileName=exchange.getIn().getHeader("Overlay", String.class);
        File out= File.createTempFile("out", ".pdf");
        Overlay overlay=new Overlay();
        overlay.setInputFile(inputFileName);
        overlay.setDefaultOverlayFile(overlayFileName);
        overlay.setOutputFile(out.getAbsolutePath());
        overlay.overlay(Collections.EMPTY_MAP);
        byte[] bytes= ByteStreams.toByteArray(new FileInputStream(out));
        out.delete();
        exchange.getIn().setBody(bytes);
    }
}
