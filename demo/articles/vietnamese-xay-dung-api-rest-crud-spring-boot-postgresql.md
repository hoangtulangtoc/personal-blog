---
id: 2ece66e8-94fa-59cd-9f97-f6ac8a9661b8
title: 'Xây dựng API REST CRUD với Spring Boot và PostgreSQL'
slug: xay-dung-api-rest-crud-spring-boot-postgresql
summary: 'Tạo một API thêm, đọc, cập nhật và xóa với entity, repository, kiểm tra dữ liệu và xử lý lỗi nhất quán.'
coverImage: /images/blog-7.png
category: Java
tags:
- spring
- rest
- postgresql
publishedAt: 2026-10-23T09:00
createdAt: '2026-10-23T06:36'
updatedAt: '2026-10-23T10:18'
---
## Entity và repository

```java
@Entity
class Note {
    @Id @GeneratedValue
    Long id;

    @NotBlank
    String title;
}

interface NoteRepository extends JpaRepository<Note, Long> {
}
```

## Controller

```java
@PostMapping("/notes")
ResponseEntity<Note> create(@Valid @RequestBody Note note) {
    Note saved = notes.save(note);
    return ResponseEntity.created(URI.create("/notes/" + saved.id)).body(saved);
}
```

## Các mã trạng thái hữu ích

| Thao tác | Thành công | Lỗi thường gặp |
|---|---|---|
| Tạo | `201 Created` | `400 Bad Request` |
| Đọc | `200 OK` | `404 Not Found` |
| Cập nhật | `200 OK` | `409 Conflict` |
| Xóa | `204 No Content` | `404 Not Found` |
