---
id: a7217786-c0c5-55af-9334-78c041add7a2
title: 'REST API CRUD with Spring Boot and PostgreSQL'
slug: rest-api-crud-with-spring-boot-and-postgresql
summary: 'Build a straightforward create, read, update, and delete API with entities, repositories, validation, and consistent error handling.'
coverImage: /images/blog-7.png
category: Java
tags:
- spring
- rest
- postgresql
publishedAt: 2026-10-23T06:30
createdAt: '2026-10-23T06:12'
updatedAt: '2026-10-23T10:06'
---
## Entity and repository

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

## Useful status codes

| Action | Success | Common error |
|---|---|---|
| Create | `201 Created` | `400 Bad Request` |
| Read | `200 OK` | `404 Not Found` |
| Update | `200 OK` | `409 Conflict` |
| Delete | `204 No Content` | `404 Not Found` |
