package com.example.mailserver.config;


import com.example.mailserver.dao.mapper.RoleMapper;
import com.example.mailserver.dao.mapper.RoleRouteMapper;
import com.example.mailserver.dao.mapper.RouteMapper;
import com.example.mailserver.dao.mapper.UserMapper;
import com.example.mailserver.interceptor.AuthHandler;
import com.example.mailserver.utils.CurrentUserHandlerMethodArgResolver;
import com.example.mailserver.utils.Token;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@Configuration
public class MVCConfig implements WebMvcConfigurer {

  @Resource
  UserMapper userMapper;

  @Resource
  RoleMapper roleMapper;

  @Resource
  RouteMapper routeMapper;

  @Resource
  RoleRouteMapper roleRouteMapper;

  @Resource
  Token token;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(
            new AuthHandler(userMapper, roleMapper, routeMapper, roleRouteMapper, token))
        .addPathPatterns("/**")
        .excludePathPatterns("/login", "/user/register", "/user/forgot_password/auth_code",
            "/user/forgot_password/reset");
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new CurrentUserHandlerMethodArgResolver());
  }
}