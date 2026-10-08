package com.acxiom.acxiomcrm.config;

import com.acxiom.acxiomcrm.security.AuthFilter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {
    @Bean public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthFilter authFilterBean(com.acxiom.acxiomcrm.security.TokenStore tokens,
                                     com.acxiom.acxiomcrm.repository.UserRepository users) {
        return new AuthFilter(tokens, users);
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterRegistration(AuthFilter authFilterBean) {
        FilterRegistrationBean<AuthFilter> bean = new FilterRegistrationBean<>(authFilterBean);
        bean.addUrlPatterns("/api/*");
        bean.setOrder(1);
        return bean;
    }

    @Bean
    CommandLineRunner startup(com.acxiom.acxiomcrm.repository.UserRepository users,
                               BCryptPasswordEncoder encoder) {
        return args -> {
            if (users.findByUsername("admin").isEmpty()) {
                var u = new com.acxiom.acxiomcrm.model.User();
                u.username = "admin";
                u.passwordHash = encoder.encode("Admin@123");
                u.role = "ADMIN";
                u.active = true;
                users.save(u);
            }
        };
    }
}
