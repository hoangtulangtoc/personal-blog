---
id: 43235713-5238-5e1e-a4f1-a530904f49a8
title: 'Danh sách kiểm tra khả năng tiếp cận dành cho lập trình viên'
slug: danh-sach-kiem-tra-kha-nang-tiep-can
summary: 'Chúng ta thường vô tình xây dựng sản phẩm cho những người giống mình. Hãy dùng danh sách ngắn này để quan tâm đến nhiều độc giả hơn.'
coverImage: /images/blog-6.png
category: Accessibility
tags:
- a11y
- checklist
- ux
publishedAt: 2026-10-23T07:30
createdAt: '2026-10-23T06:24'
updatedAt: '2026-10-23T10:12'
---
## Trước khi phát hành

- [x] Mỗi ảnh có văn bản `alt` hữu ích; ảnh trang trí dùng `alt=""`.
- [x] Tỷ lệ tương phản của văn bản đạt ít nhất 4,5:1.
- [x] Màu sắc không phải cách duy nhất để truyền tải thông tin.
- [x] Lỗi biểu mẫu được mô tả bằng văn bản và gắn với đúng trường.
- [ ] Kiểm tra bằng trình đọc màn hình như NVDA hoặc VoiceOver.

## Chế độ tối cũng là một lựa chọn về khả năng tiếp cận

Một số người nhạy cảm với ánh sáng mạnh; số khác lại thấy nền sáng dễ đọc hơn. Hãy để độc giả **tự chọn** và ghi nhớ tùy chọn đó.

## Chuyển động

```css
@media (prefers-reduced-motion: reduce) {
  * { animation: none !important; transition: none !important; }
}
```
