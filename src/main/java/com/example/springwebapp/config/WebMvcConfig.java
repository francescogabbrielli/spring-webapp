package com.example.springwebapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired private RequestInterceptor requestInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //risorse statiche (perché è necessario??? dovrebbe essere di default)
        registry
            .addResourceHandler("/static/**")
            .addResourceLocations("classpath:static/");

        //bootstrap e jquery (parte di webjars)
        registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("/webjars/");
            

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(requestInterceptor);
    }

}
