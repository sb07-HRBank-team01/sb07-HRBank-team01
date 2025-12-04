package com.codeit_team01.sb07_hrbank_team01.common.config;

import com.codeit_team01.sb07_hrbank_team01.common.interceptor.ClientIpInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  private ClientIpInterceptor clientIpInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(clientIpInterceptor)
        .addPathPatterns("/api/backup"); // backup 경로
  }
}
