package de.gzockoll.prototype.templates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class TemplatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplatesApplication.class, args);
    }
}
