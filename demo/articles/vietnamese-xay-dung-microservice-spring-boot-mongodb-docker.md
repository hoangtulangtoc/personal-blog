---
id: 2159e589-c8d7-5d35-b3fa-b14f88cb5c86
title: 'Xây dựng microservice với Spring Boot, MongoDB và Docker'
slug: xay-dung-microservice-spring-boot-mongodb-docker
summary: 'Tách ứng dụng nguyên khối thành các dịch vụ nhỏ chạy trong container: tổ chức mã nguồn, cấu hình MongoDB và dùng Docker Compose.'
coverImage: /images/blog-1.png
category: Database
tags:
- mongodb
- docker
- spring
publishedAt: 2026-10-23T09:15
createdAt: '2026-10-23T06:38'
updatedAt: '2026-10-23T10:19'
---
## Vì sao nên dùng microservice?

Khi ứng dụng lớn dần, mỗi lần triển khai có thể trở nên rủi ro. Chia hệ thống thành các **dịch vụ nhỏ** giúp từng nhóm triển khai độc lập và mở rộng những phần chịu tải cao.

Microservice cũng có chi phí: một phần độ phức tạp được chuyển từ mã nguồn sang vận hành.

## Cấu trúc một dịch vụ

```java
@RestController
@RequestMapping("/orders")
class OrderController {

    private final OrderRepository orders;

    OrderController(OrderRepository orders) {
        this.orders = orders;
    }

    @GetMapping("/{id}")
    Order find(@PathVariable String id) {
        return orders.findById(id).orElseThrow();
    }
}
```

## Dùng Docker Compose khi phát triển

```yaml
services:
  mongo:
    image: mongo:7
    ports: ["27017:27017"]
  orders:
    build: ./orders
    environment:
      SPRING_DATA_MONGODB_URI: mongodb://mongo:27017/orders
```

## Trước khi tách dịch vụ

- [x] Ranh giới nghiệp vụ đã rõ ràng.
- [x] Đã có log tập trung và theo dõi phân tán.
- [ ] Mỗi dịch vụ sở hữu dữ liệu của riêng mình.

> Đừng tách dịch vụ chỉ vì microservice nghe có vẻ hiện đại. Hãy tách khi ứng dụng nguyên khối thật sự gây ra vấn đề.
