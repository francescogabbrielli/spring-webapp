package com.example.springwebapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //risorse statiche (perché necessario???)
        registry
            .addResourceHandler("/static/**")
            .addResourceLocations("classpath:static/");

        //bootstrap e jquery (parte di webjars)
        registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("/webjars/");
            

    }
}
