package com.example.mailserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 配置跨域访问
@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // 项目中所有接口都支持跨域
        .allowedOriginPatterns("*") // 所有地址都可以访问，也可以配置具体地址
        .allowCredentials(true)
        .allowedMethods("*")
        .maxAge(3600); // 跨域允许时间
  }
}

