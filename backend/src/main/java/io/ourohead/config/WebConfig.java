package io.ourohead.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Registers a resource handler that serves static resources under the `/ourohead/**` URL path.
     *
     * Maps requests matching `/ourohead/**` to resources located at `classpath:/static/ourohead/`.
     *
     * @param registry the registry used to add resource handlers and locations
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/ourohead/**")
                .addResourceLocations("classpath:/static/ourohead/");
    }
}