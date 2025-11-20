package com.example.Study_board.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 실제 저장된 폴더: C:\Users\admin\Desktop\프로젝트\image\
        registry.addResourceHandler("/image/**")
                .addResourceLocations(
                        "classpath:/static/image/",
                        "file:///C:/프로젝트/image/");
    }
}