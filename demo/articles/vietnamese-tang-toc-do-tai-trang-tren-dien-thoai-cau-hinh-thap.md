---
id: 2f3745f1-d404-5a14-94c4-85f074992885
title: 'Tăng tốc độ tải trang trên điện thoại cấu hình thấp'
slug: tang-toc-do-tai-trang-tren-dien-thoai-cau-hinh-thap
summary: 'Hàng triệu người truy cập web bằng thiết bị có cấu hình thấp. Một vài lựa chọn thực tế giúp trang luôn phản hồi nhanh.'
coverImage: /images/blog-2.png
category: Web Performance
tags:
- webperf
- mobile
- optimize
publishedAt: 2026-10-23T09:00
createdAt: '2026-10-23T06:32'
updatedAt: '2026-10-23T10:16'
---
## Thiết bị thật, mạng thật

Máy tính phát triển thường nhanh hơn nhiều so với điện thoại phổ thông. Hãy thử nghiệm với **CPU bị giới hạn 4 lần** và kết nối *Slow 4G*.

## Ngân sách hiệu năng

| Tài nguyên | Giới hạn |
|---|---|
| JavaScript đã nén | ≤ 170 KB |
| CSS | ≤ 50 KB |
| Ảnh trong màn hình đầu tiên | ≤ 200 KB |
| LCP | ≤ 2,5 giây |

## Những thay đổi có thể áp dụng ngay

1. Kết xuất nội dung ở máy chủ và dùng JavaScript như một lớp **nâng cao dần trải nghiệm**.
2. Khai báo kích thước ảnh để tránh xê dịch bố cục, đồng thời dùng `loading="lazy"` cho nội dung phía dưới màn hình.
3. Đặt `font-display: swap` và chỉ tải những độ đậm của phông chữ thật sự cần thiết.

```html
<img src="/images/cover.webp" width="800" height="450" loading="lazy" alt="">
```
