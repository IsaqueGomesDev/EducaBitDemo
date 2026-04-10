package com.educabit.educabit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AccessLoggingInterceptor accessLoggingInterceptor;

    public WebMvcConfig(@NonNull AccessLoggingInterceptor accessLoggingInterceptor) {
        this.accessLoggingInterceptor = accessLoggingInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(accessLoggingInterceptor)
                .addPathPatterns("/**") // Log everything
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/uploads/**"); // Exclude static assets
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
    }
}
