package de.techdev.portal;

import de.techdev.portal.core.PortalEmbeddedServletContainerCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TechdevPortal {

    @Bean
    public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
        return new PortalEmbeddedServletContainerCustomizer();
    }

    public static void main(String[] args) {
        SpringApplication.run(TechdevPortal.class, args);
    }
}
