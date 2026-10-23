---
id: 7e4847cd-5795-5185-a14a-0d20d005cdbe
title: 'Secure Database Connections with HashiCorp Vault'
slug: secure-database-connections-with-hashicorp-vault
summary: 'Let Vault issue short-lived database credentials to each application instead of keeping long-lived passwords in configuration files.'
coverImage: /images/blog-4.png
category: Database
tags:
- security
- vault
- postgresql
publishedAt: 2026-10-23T06:45
createdAt: '2026-10-23T06:14'
updatedAt: '2026-10-23T10:07'
---
## The problem with static passwords

Passwords in `.env` files or environment variables can remain in use for years, get shared across services, and become difficult to rotate.

## Dynamic secrets

Vault can **create a database user on demand** with a time-to-live (TTL). When the lease expires, Vault revokes the user automatically.

```bash
vault secrets enable database
vault write database/roles/blog-app \
    db_name=blog \
    default_ttl=1h max_ttl=24h
```

## Benefits

- Each instance gets its own credentials, making access easier to trace.
- A leaked credential is useful for only a short time.
- Credentials rotate automatically without another deployment.
