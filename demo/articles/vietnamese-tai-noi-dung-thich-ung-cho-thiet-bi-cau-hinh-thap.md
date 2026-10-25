---
id: 040badfe-e541-51fd-a4a1-1216f85b36bc
title: 'Tải nội dung thích ứng cho thiết bị cấu hình thấp'
slug: tai-noi-dung-thich-ung-cho-thiet-bi-cau-hinh-thap
summary: 'Thiết kế đáp ứng thay đổi theo kích thước màn hình. Tải nội dung thích ứng còn xét đến sức xử lý và kết nối mạng của thiết bị.'
coverImage: /images/blog-5.png
category: Web Performance
tags:
- webperf
- performance
- javascript
publishedAt: 2026-10-23T08:00
createdAt: '2026-10-23T06:30'
updatedAt: '2026-10-23T10:15'
---
## Ý tưởng

Cung cấp trải nghiệm đầy đủ cho thiết bị nhanh và phiên bản nhẹ hơn cho thiết bị chậm, đồng thời đảm bảo mọi phiên bản đều **dễ sử dụng**.

## Các tín hiệu có thể dùng

```js
const slow = navigator.connection?.effectiveType?.includes("2g");
const lowMemory = (navigator.deviceMemory ?? 8) <= 2;

if (slow || lowMemory) {
  document.documentElement.classList.add("lite");
}
```

## Những phần có thể giảm tải

- Thay video nền bằng ảnh tĩnh.
- Tắt các hiệu ứng chuyển động phức tạp.
- Thay bản đồ tương tác bằng ảnh chụp kèm liên kết.

Quan trọng nhất, phiên bản nhẹ vẫn phải cung cấp đầy đủ **nội dung**.
