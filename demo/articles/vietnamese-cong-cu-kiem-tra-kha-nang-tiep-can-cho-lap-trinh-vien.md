---
id: c78024f7-b263-516a-9a32-a190dd93cf7c
title: 'Các công cụ kiểm tra khả năng tiếp cận tốt nhất cho lập trình viên'
slug: cong-cu-kiem-tra-kha-nang-tiep-can-cho-lap-trinh-vien
summary: 'Phát hiện sớm vấn đề về khả năng tiếp cận bằng công cụ trên trình duyệt và CI. Bài viết này được lên lịch xuất bản.'
coverImage: /images/blog-9.png
category: Accessibility
tags:
- a11y
- tools
- devtools
publishedAt: 2030-10-24T09:30
createdAt: '2026-10-23T06:52'
updatedAt: '2026-10-23T10:41'
---
## Trên trình duyệt

- **Chrome DevTools** → ngăn Accessibility giúp kiểm tra cây trợ năng và độ tương phản màu.
- **axe DevTools** quét trang và giải thích từng vấn đề tìm thấy.

## Trong CI

```bash
npx @axe-core/cli http://localhost:8080 --exit
```

## Kiểm tra thủ công vẫn rất quan trọng

Công cụ tự động chỉ phát hiện được một phần vấn đề về khả năng tiếp cận. Người dùng vẫn cần kiểm tra bằng bàn phím và trình đọc màn hình.
