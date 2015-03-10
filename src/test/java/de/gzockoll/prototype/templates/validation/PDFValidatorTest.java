package de.gzockoll.prototype.templates.validation;

import de.gzockoll.prototype.templates.entity.Asset;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PDFValidatorTest {
    @Test
    public void testIsValid() throws Exception {
        Asset asset=new Asset();
        asset.setData("blabla".getBytes());

        assertThat(new PDFValidator().isValid(asset,null)).isFalse();
        asset.setData(PDFHelper.getValidPDFContent());
        assertThat(new PDFValidator().isValid(asset,null)).isTrue();
    }
}