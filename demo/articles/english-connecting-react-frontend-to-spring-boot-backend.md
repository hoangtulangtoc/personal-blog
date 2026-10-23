---
id: 1c5fe839-9734-576f-95b9-0ea591945414
title: 'Connecting a React Frontend to a Spring Boot Backend'
slug: connecting-react-frontend-to-spring-boot-backend
summary: 'A draft on CORS, development proxies, and sharing types between the frontend and backend.'
coverImage: /images/blog-10.png
category: Java
tags:
- react
- spring
- cors
publishedAt: null
createdAt: '2026-10-23T07:08'
updatedAt: '2026-10-23T10:49'
---
## Proxy during development

```json
{
  "proxy": "http://localhost:8080"
}
```

## CORS when deploying to separate domains

```java
@Bean
WebMvcConfigurer cors() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/api/**").allowedOrigins("https://app.example.com");
        }
    };
}
```

*(This article is still a work in progress.)*
