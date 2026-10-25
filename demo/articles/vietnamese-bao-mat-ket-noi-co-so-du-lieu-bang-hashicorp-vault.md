---
id: daae3658-381b-5cf2-9727-5f149dc00f3f
title: 'Bảo mật kết nối cơ sở dữ liệu bằng HashiCorp Vault'
slug: bao-mat-ket-noi-co-so-du-lieu-bang-hashicorp-vault
summary: 'Để Vault cấp thông tin đăng nhập cơ sở dữ liệu có thời hạn ngắn cho từng ứng dụng, thay vì lưu mật khẩu dài hạn trong cấu hình.'
coverImage: /images/blog-4.png
category: Database
tags:
- security
- vault
- postgresql
publishedAt: 2026-10-23T06:00
createdAt: '2026-10-23T06:20'
updatedAt: '2026-10-23T10:10'
---
## Vấn đề của mật khẩu tĩnh

Mật khẩu trong tệp `.env` hoặc biến môi trường có thể được dùng suốt nhiều năm, bị chia sẻ giữa các dịch vụ và khó thay đổi định kỳ.

## Bí mật động

Vault có thể **tạo người dùng cơ sở dữ liệu theo yêu cầu** với thời hạn sử dụng (TTL). Khi thời hạn thuê kết thúc, Vault tự động thu hồi người dùng đó.

```bash
vault secrets enable database
vault write database/roles/blog-app \
    db_name=blog \
    default_ttl=1h max_ttl=24h
```

## Lợi ích

- Mỗi phiên bản ứng dụng có thông tin đăng nhập riêng, giúp truy vết quyền truy cập dễ hơn.
- Nếu bị lộ, thông tin đăng nhập chỉ có giá trị trong thời gian ngắn.
- Thông tin được luân chuyển tự động mà không cần triển khai lại.
