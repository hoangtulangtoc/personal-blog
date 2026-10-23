---
id: a8641441-16a9-5c81-af44-b2fbd30c6c8e
title: 'Kết nối frontend React với backend Spring Boot'
slug: ket-noi-frontend-react-voi-backend-spring-boot
summary: 'Bản nháp về CORS, proxy khi phát triển và cách chia sẻ kiểu dữ liệu giữa frontend với backend.'
coverImage: /images/blog-10.png
category: Java
tags:
- react
- spring
- cors
publishedAt: null
createdAt: '2026-10-23T07:26'
updatedAt: '2026-10-23T10:58'
---
## Dùng proxy khi phát triển

```json
{
  "proxy": "http://localhost:8080"
}
```

## CORS khi triển khai trên các tên miền khác nhau

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

*(Bài viết này vẫn đang được hoàn thiện.)*
