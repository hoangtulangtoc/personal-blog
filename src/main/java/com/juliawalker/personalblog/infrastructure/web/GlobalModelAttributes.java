package com.juliawalker.personalblog.infrastructure.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    private final SiteProperties siteProperties;

    public GlobalModelAttributes(SiteProperties siteProperties) {
        this.siteProperties = siteProperties;
    }

    @ModelAttribute("site")
    public SiteProperties site() {
        return siteProperties;
    }

    @ModelAttribute("currentPath")
    public String currentPath(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return path.isBlank() ? "/" : path;
    }

}
