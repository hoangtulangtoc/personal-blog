package com.juliawalker.personalblog.infrastructure.web;

import java.util.Locale;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@EnableConfigurationProperties(SiteProperties.class)
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor() {
            @Override
            protected Locale parseLocaleValue(String localeValue) {
                return switch (localeValue.toLowerCase(Locale.ROOT)) {
                    case "en" -> Locale.ENGLISH;
                    case "vi" -> Locale.forLanguageTag("vi");
                    default -> throw new IllegalArgumentException("Unsupported language");
                };
            }
        };
        interceptor.setParamName("lang");
        interceptor.setHttpMethods("GET");
        interceptor.setIgnoreInvalidLocale(true);
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}
