package org.practice.emailservice.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:[*]", "https://*.koyeb.app") // Adjust these patterns as needed
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type", "X-Auth-Token", "Origin", "Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers")
                .exposedHeaders("X-Auth-Token")
                .allowCredentials(true)
                .maxAge(3600); // 1 hour
    }
}
