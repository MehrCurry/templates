package de.gzockoll.prototype.templates.control;

import de.gzockoll.prototype.templates.entity.JiraTicket;
import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class JiraConnectorTest {

    @Test
    public void testConnect() {
        Collection<JiraTicket> result = new JiraConnector().findAll();
        assertThat(result.size()).isGreaterThan(0);
    }

}