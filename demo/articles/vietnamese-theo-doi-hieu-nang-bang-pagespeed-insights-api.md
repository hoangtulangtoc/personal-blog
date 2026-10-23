---
id: 788b0184-94c5-56f6-8cec-e6fe6b3efd04
title: 'Theo dõi hiệu năng bằng PageSpeed Insights API'
slug: theo-doi-hieu-nang-bang-pagespeed-insights-api
summary: 'Đo Core Web Vitals mỗi ngày để phát hiện suy giảm hiệu năng trước khi độc giả phản ánh.'
coverImage: /images/blog-8.png
category: Web Performance
tags:
- webperf
- lighthouse
- monitoring
publishedAt: 2026-10-23T08:45
createdAt: '2026-10-23T06:34'
updatedAt: '2026-10-23T10:17'
---
## Gọi API

```bash
curl "https://www.googleapis.com/pagespeedonline/v5/runPagespeed?url=https://example.com&strategy=mobile"
```

Phản hồi gồm **dữ liệu phòng thí nghiệm** từ Lighthouse và **dữ liệu thực tế** từ CrUX, được thu thập từ người dùng thật.

## Các chỉ số cần theo dõi

- **LCP** — thời gian để nội dung chính xuất hiện.
- **INP** — tốc độ trang phản hồi khi người dùng tương tác.
- **CLS** — mức độ bố cục dịch chuyển bất ngờ.

## Quy trình báo cáo đơn giản

Chạy một tập lệnh mỗi đêm, lưu kết quả và vẽ biểu đồ theo thời gian. Một điểm số riêng lẻ ít cho biết điều gì; **xu hướng** mới là điều đáng quan tâm.
