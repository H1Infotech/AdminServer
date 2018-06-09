package com.h1infotech.smarthive.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class Application implements WebMvcConfigurer {
  
    @Override  
    public void addCorsMappings(CorsRegistry registry) {  
  
    	registry.addMapping("/**")    
        .allowedOrigins("*")    
        .allowCredentials(true)    
        .allowedMethods("GET", "POST", "DELETE", "PUT")    
        .maxAge(3600);    
    }  
}