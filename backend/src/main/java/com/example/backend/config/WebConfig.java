package com.example.backend.config;  // 可以根据实际包结构调整

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map the request path：/uploads/** to E:/shopping_master/backend/uploads/
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/E:/shopping_master/backend/uploads/"); // Adjust according to your actual file path

        // Map the request path：/pictures/** to E:/shopping-master/frontend/src/public/pictures/
        registry.addResourceHandler("/public/pictures/**")
                .addResourceLocations("file:/E:/shopping-master/frontend/src/public/pictures/");
    }

}

