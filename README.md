# Personal Blog

A server-rendered personal blog built with Spring Boot for the [roadmap.sh Personal Blog](https://roadmap.sh/projects/personal-blog) project. Articles are stored as Markdown files, and the admin area is protected by form login.

![Home page](docs/screenshots/english-01-home.jpg)

## Overview

* **Public site:** latest published articles, category and tag filters, pagination, and Markdown article pages.
* **Admin area:** authenticated dashboard for published, scheduled, and draft articles, with create, edit, and delete flows.
* **File-based storage:** each article is a Markdown file with YAML front matter; no database is required.
* **Progressive enhancement:** server-rendered pages and forms handle the main flows. JavaScript adds the theme toggle and mobile navigation.
* **Bilingual interface:** English is the default, with Vietnamese available from the **EN / VI** language switch. The selected language is kept for the session.
* **Responsive themes:** light and dark modes are supported from mobile through desktop.

## Contents

* [Screenshots](#screenshots)
  * [English interface](#english-interface)
  * [Vietnamese interface](#vietnamese-interface)
* [Features](#features-in-detail)
* [Tech stack](#tech-stack)
* [Project structure](#project-structure)
* [Article storage](#article-storage)
* [Configuration](#configuration)
* [Running locally](#running-locally)
  * [Demo data](#demo-data)
* [Deploying the demo to Render](#deploying-the-demo-to-render)
* [Build and test](#build-and-test)

## Screenshots

The English interface is documented first, followed by a dedicated Vietnamese gallery. All 52 screenshots were captured from `demo/articles/` on 2026-10-23 (Asia/Ho_Chi_Minh). See [Demo data](#demo-data) for the sample article dates. Images are stored in [`docs/screenshots/`](docs/screenshots).

### English interface

#### Guest pages

##### Home

![Home: hero](docs/screenshots/english-01-home.jpg)

The home page opens with a hero section: a greeting with the author's name and role (`SITE_AUTHOR`, `SITE_AUTHOR_ROLE`), a tagline, a **Read articles** button that jumps to the list, a **Contact me** button, and the portrait (`SITE_HERO_IMAGE`). The header has the logo, the navigation and the theme toggle.

![Home: article list and sidebar](docs/screenshots/english-02-home-articles.jpg)

Below the hero, **Latest articles** lists published articles, newest first. Each card shows:

* the cover image
* a topic chip (the category)
* the title
* an excerpt (the summary, or the start of the content when there is no summary)
* the author, the publication date and an estimated reading time

The sidebar lists every **Topic** that has at least one published article, with a stable icon for each topic, and a **Tags** cloud.

![Home: pagination](docs/screenshots/english-03-pagination.jpg)

The home page shows 6 articles per page. The pager shows the range ("1 – 6 of 16"), page numbers, and Newer / Older links. Only published articles are counted: the demo has 20 files, but the 2 drafts and 2 scheduled articles are hidden from guests.

![Footer](docs/screenshots/english-04-footer.jpg)

The footer has three columns:

* the site name and description with contact links (e-mail, GitHub, Facebook, LinkedIn, X; blank values are hidden)
* the topics
* quick links, including **Admin login**

##### Category and tag filters

| Category | Tag |
|---|---|
| ![Category filter](docs/screenshots/english-05-category.jpg) | ![Tag filter](docs/screenshots/english-06-tag.jpg) |

`/category/{slug}` and `/tag/{slug}` reuse the home layout with a heading ("Topic: Web Performance", "Tag: #spring") and a **Show all articles** link. The active topic is highlighted in the sidebar. Slugs are built from the free-text names, so "Web Performance" becomes `web-performance`. A category or tag that no published article uses returns 404.

##### Article

![Article: header](docs/screenshots/english-07-article.jpg)

`/articles/{slug}` shows a breadcrumb (Home › category › title), the category badge, the title, and a meta line (full publication date, reading time, author), followed by the cover image.

![Article: body](docs/screenshots/english-08-article-body.jpg)

The body is Markdown rendered with Flexmark (headings, emphasis, lists, tables, fenced code blocks) and styled with Tailwind Typography. The HTML is sanitized with a jsoup allowlist before it reaches the template. An aside shows an author card with contact links and an **Article info** box (topic and publication date).

##### Not found

![404 page](docs/screenshots/english-12-not-found.jpg)

Unknown URLs, malformed ids or slugs, and **drafts or scheduled articles requested by a guest** all return the same 404 page. The admin can still see those articles, so a guest cannot tell whether an unpublished article exists.

#### Admin pages

##### Login

| Login | Wrong credentials |
|---|---|
| ![Login](docs/screenshots/english-13-login.jpg) | ![Login error](docs/screenshots/english-14-login-error.jpg) |

`/login` is a Spring Security form login with a **Remember me** option and the theme toggle. A wrong username or password returns to the form with a generic error message. There is a single admin account, configured with `ADMIN_USERNAME` and a BCrypt `ADMIN_PASSWORD_HASH`. Everything under `/admin/**` requires `ROLE_ADMIN`, and every form carries a CSRF token.

##### Dashboard

![Dashboard](docs/screenshots/english-15-dashboard.jpg)

`/admin` lists **every** article, including drafts and scheduled ones, most recently updated first, 10 per page. Each row shows:

* a cover thumbnail, the title and the slug
* a status badge: **Published**, **Scheduled** or **Draft**
* the category, the publication date and the last update

Every row has **Edit** and **Delete** actions. Published articles also get **View**, which opens the public page. The status comes from `ArticleService`, not from the template.

##### Add article

![New article form](docs/screenshots/english-16-new-article.jpg)

`/admin/articles/new` is a two-column form:

* **Main column**: title (required), optional summary, optional cover image (a `/path` or an `http(s)://` URL, with a list of built-in images), and the Markdown content (required).
* **Side column**:
  * **Publishing**: the publication date (`datetime-local`). Empty keeps a draft, a future date schedules the article.
  * **Details**: slug, category, and comma-separated tags. An empty slug is generated from the title, with Vietnamese diacritics removed.

![Validation errors](docs/screenshots/english-17-validation.jpg)

The server runs Bean Validation in group sequences and re-renders the form with a summary banner and per-field errors. Duplicate titles or slugs are reported on the matching field.

![Created message](docs/screenshots/english-18-created.jpg)

After a successful save, the app redirects to the dashboard with a flash message. The new article appears in the list, and the total goes from 20 to 21.

##### Edit article

![Edit article form](docs/screenshots/english-19-edit-article.jpg)

`/admin/articles/{id}/edit` uses the same form, pre-filled. It also shows the current status badge and a preview of the cover image.

##### Delete article

| Confirmation | After deleting |
|---|---|
| ![Delete confirmation](docs/screenshots/english-20-delete-confirm.jpg) | ![Deleted message](docs/screenshots/english-21-deleted.jpg) |

Deleting always goes through a confirmation page (`GET /admin/articles/{id}/delete`). It shows the title and status and warns that the file is removed permanently. **Delete permanently** posts the form, and the dashboard confirms with a flash message. No JavaScript `confirm()` dialog is involved.

#### Dark mode and language switch

| Dark mode (guest) | Dark mode (article) |
|---|---|
| ![Home in dark mode](docs/screenshots/english-09-home-dark.jpg) | ![Article in dark mode](docs/screenshots/english-10-article-dark.jpg) |

| Dark mode (admin) | Bilingual demo articles |
|---|---|
| ![Dashboard in dark mode](docs/screenshots/english-22-dashboard-dark.jpg) | ![Bilingual articles](docs/screenshots/english-11-bilingual-articles.jpg) |

The theme toggle switches a `dark` class on `<html>`. The choice is kept in `localStorage` and applied before the first paint, and the default follows the operating system. Every element has light and dark variants built from the same color tokens.

The language switch changes interface text, validation errors, flash messages, and date formats between English and Vietnamese. The selected language is kept in the session. Translations live in `messages.properties` and `messages_vi.properties`; the demo includes English and Vietnamese articles.

#### Mobile layout

| Home | Menu | Cards | Admin |
|---|---|---|---|
| ![Mobile home](docs/screenshots/english-23-mobile-home.jpg) | ![Mobile menu](docs/screenshots/english-24-mobile-menu.jpg) | ![Mobile cards](docs/screenshots/english-25-mobile-cards.jpg) | ![Mobile admin](docs/screenshots/english-26-mobile-admin.jpg) |

The layout is mobile-first:

* On small screens the hero stacks, the portrait is hidden, and cards drop their cover image.
* The navigation moves into a slide-in menu with the main links and topics.
* The admin header keeps navigation links visible without JavaScript, and the article table scrolls horizontally.

### Vietnamese interface

The Vietnamese interface follows the English gallery. It includes the same guest pages, admin flows, theme states, and mobile layouts, with Vietnamese labels, validation errors, and messages. Article lists contain both English and Vietnamese demo articles. Use the **VI** control to switch languages; the selection stays active as you navigate between guest and admin pages.

#### Guest pages

| Home | Article list |
|---|---|
| ![Vietnamese home page](docs/screenshots/vietnamese-01-home.jpg) | ![Vietnamese home article list](docs/screenshots/vietnamese-02-home-articles.jpg) |

| Pagination | Footer |
|---|---|
| ![Vietnamese pagination](docs/screenshots/vietnamese-03-pagination.jpg) | ![Vietnamese footer](docs/screenshots/vietnamese-04-footer.jpg) |

| Category filter | Tag filter |
|---|---|
| ![Vietnamese category filter](docs/screenshots/vietnamese-05-category.jpg) | ![Vietnamese tag filter](docs/screenshots/vietnamese-06-tag.jpg) |

| Article | Article body |
|---|---|
| ![Vietnamese article](docs/screenshots/vietnamese-07-article.jpg) | ![Vietnamese article body](docs/screenshots/vietnamese-08-article-body.jpg) |

| Dark home | Dark article |
|---|---|
| ![Vietnamese home in dark mode](docs/screenshots/vietnamese-09-home-dark.jpg) | ![Vietnamese article in dark mode](docs/screenshots/vietnamese-10-article-dark.jpg) |

| Bilingual demo articles | Not found |
|---|---|
| ![Vietnamese interface with bilingual articles](docs/screenshots/vietnamese-11-bilingual-articles.jpg) | ![Vietnamese not found page](docs/screenshots/vietnamese-12-not-found.jpg) |

#### Admin pages

| Login | Wrong credentials |
|---|---|
| ![Vietnamese admin login](docs/screenshots/vietnamese-13-login.jpg) | ![Vietnamese login error](docs/screenshots/vietnamese-14-login-error.jpg) |

| Dashboard | New article |
|---|---|
| ![Vietnamese admin dashboard](docs/screenshots/vietnamese-15-dashboard.jpg) | ![Vietnamese new article form](docs/screenshots/vietnamese-16-new-article.jpg) |

| Validation | Article created |
|---|---|
| ![Vietnamese validation errors](docs/screenshots/vietnamese-17-validation.jpg) | ![Vietnamese article created](docs/screenshots/vietnamese-18-created.jpg) |

| Edit article | Delete confirmation |
|---|---|
| ![Vietnamese edit article form](docs/screenshots/vietnamese-19-edit-article.jpg) | ![Vietnamese delete confirmation](docs/screenshots/vietnamese-20-delete-confirm.jpg) |

| Article deleted | Dark dashboard |
|---|---|
| ![Vietnamese article deleted message](docs/screenshots/vietnamese-21-deleted.jpg) | ![Vietnamese admin dashboard in dark mode](docs/screenshots/vietnamese-22-dashboard-dark.jpg) |

#### Mobile pages

| Home | Menu |
|---|---|
| ![Vietnamese mobile home](docs/screenshots/vietnamese-23-mobile-home.jpg) | ![Vietnamese mobile menu](docs/screenshots/vietnamese-24-mobile-menu.jpg) |

| Article cards | Admin |
|---|---|
| ![Vietnamese mobile article cards](docs/screenshots/vietnamese-25-mobile-cards.jpg) | ![Vietnamese mobile admin](docs/screenshots/vietnamese-26-mobile-admin.jpg) |

## Features in detail

* **Publication rules** (in `ArticleService`, never in templates):
  * an empty publication date means a **draft**
  * a date in the future means **scheduled**
  * a date now or in the past means **published**

  Guests only ever see published articles, so a scheduled article appears on its own once its date passes.
* **Slugs**: generated from the title (Vietnamese diacritics and `đ` stripped, ASCII only) or entered by hand. Titles and slugs are unique, compared case-insensitively.
* **Categories and tags**: free text stored in the article file. Their URL segments are slugs, and names that differ only in case are merged.
* **Cover images**: only site-relative paths (`/images/...`) or `http(s)://` URLs are accepted. Other schemes such as `javascript:` or `data:` are rejected.
* **Safe Markdown**: the HTML from Flexmark is sanitized by jsoup. It is the only content printed with `th:utext`.
* **Storage**:
  * writes go to a temp file first and are then moved into place atomically, so a file is never half-written
  * a read/write lock guards the in-memory index
  * a malformed file is logged and skipped instead of crashing the app
  * YAML is parsed with SnakeYAML's `SafeConstructor`
* **Security**:
  * form login with session and remember-me
  * CSRF protection on every form
  * a BCrypt password hash only, never a plain password
  * the app refuses to start without admin configuration

## Tech stack

Java 17, Spring Boot 3.5, Spring MVC, Spring Security, Thymeleaf + Layout Dialect, Bean Validation, SnakeYAML, Flexmark + jsoup (sanitized Markdown), Tailwind CSS 3 (+ Typography), PostCSS, npm scripts.

## Project structure

```text
src/main/java/com/juliawalker/personalblog/
├── article/          Article domain, ArticleStore, FileSystemArticleStore, ArticleFileFormat, ArticleService
│   └── web/          Client and admin controllers, form data, converters
└── infrastructure/   Security, web helpers, exceptions, validation groups, Markdown rendering
src/main/resources/
├── i18n/             messages.properties (English) and messages_vi.properties (Vietnamese UI strings)
├── static/           CSS source, js/script.js (dark mode / mobile menu), SVG, images
└── templates/        client/, admin/, shared/fragments/, error/
demo/articles/        20 readable filenames: english-{slug}.md and vietnamese-{slug}.md
docs/screenshots/     screenshots used in this README
```

## Article storage

Each article is a Markdown file with YAML front matter. Its UUID in `id` stays stable regardless of the filename. Demo files use readable language-prefixed names such as `english-building-microservices-with-spring-boot-mongodb-and-docker.md` and `vietnamese-xay-dung-microservice-spring-boot-mongodb-docker.md`. Newly created articles use `{id}.md`; when an existing article is edited or deleted, the store keeps using its original filename.

```text
---
id: 7f3c0b9e-1d2a-4c3b-9f8e-0a1b2c3d4e5f
title: Hello world
slug: hello-world
summary: Optional short description
coverImage: /images/blog-1.png   # optional: a /path or an http(s) URL
category: Java
tags: [spring, thymeleaf]
publishedAt: 2026-10-23T09:00   # empty = draft
createdAt: 2026-10-23T08:00
updatedAt: 2026-10-23T09:00
---
Markdown body...
```

Back up the `ARTICLES_DIR` directory: it is the only data store.

## Configuration

| Variable | Purpose |
|---|---|
| `ARTICLES_DIR` | Article directory (default `./data/articles`) |
| `ADMIN_USERNAME` | Admin login name |
| `ADMIN_PASSWORD_HASH` | BCrypt hash of the admin password (never the plain password) |
| `SITE_NAME`, `SITE_DESCRIPTION`, `SITE_AUTHOR` | Site identity shown in the layout |
| `SITE_AUTHOR_ROLE`, `SITE_TAGLINE` | Hero text ("Web Developer", one-line tagline) |
| `SITE_HERO_IMAGE`, `SITE_AUTHOR_AVATAR` | Hero portrait and author avatar (default `/images/hero.png`, `/images/author.png`) |
| `SITE_EMAIL`, `SOCIAL_GITHUB`, `SOCIAL_FACEBOOK`, `SOCIAL_LINKEDIN`, `SOCIAL_X` | Contact links in the sidebar and footer (blank = hidden) |
| `REMEMBER_ME_KEY` | Remember-me signing key |
| `SESSION_COOKIE_SECURE` | `true` behind HTTPS, `false` for `http://localhost` |

The app refuses to start without `ADMIN_USERNAME` and a valid `ADMIN_PASSWORD_HASH`. For local development, copy `.env.example` to `.env` (not committed) and fill it in.

To create a BCrypt hash, for example: `python3 -c "import bcrypt; print(bcrypt.hashpw(b'your-password', bcrypt.gensalt()).decode())"` (needs the `bcrypt` package), or `htpasswd -bnBC 10 "" 'your-password' | tr -d ':\n'`.

## Running locally

```bash
cp .env.example .env     # then set ADMIN_PASSWORD_HASH
npm install
npm run build            # compile CSS/JS/templates into target/classes
mvn spring-boot:run      # http://localhost:8080, admin at /login
npm run watch            # optional: live reload on http://localhost:3000
```

### Demo data

`demo/articles/` contains 20 sample articles with cover images: 10 English articles and their 10 Vietnamese editions. All `createdAt` and `updatedAt` timestamps fall on `2026-10-23`. Sixteen articles have a `publishedAt` date of `2026-10-23`, two are scheduled for `2030-10-24`, and the remaining two are drafts (`publishedAt: null`). Run against a copy so the sample files stay untouched:

```bash
cp -r demo/articles /tmp/blog-demo
ARTICLES_DIR=/tmp/blog-demo mvn spring-boot:run
```

With Docker:

```bash
docker build -t personal-blog .
docker run -p 8080:8080 --env-file .env -v "$PWD/data/articles:/data/articles" personal-blog
```

Only run one instance, because the article index lives in memory.

## Deploying the demo to Render

The public demo runs on [Render](https://render.com) from [`render.yaml`](render.yaml) and [`Dockerfile.demo`](Dockerfile.demo). The demo image ships `demo/articles/` and copies it into a fresh `ARTICLES_DIR` on every start, so the demo resets whenever the service restarts or redeploys. Only the public pages are meant for visitors; the admin credentials are not published.

1. In the Render dashboard, choose **New > Blueprint** and connect this repository.
2. Enter `ADMIN_USERNAME` and `ADMIN_PASSWORD_HASH` (a BCrypt hash, see [Configuration](#configuration)) when prompted. `REMEMBER_ME_KEY` is generated by Render.
3. Apply the Blueprint. Pushes to `main` redeploy the demo.

On the free plan the service sleeps after a period without traffic, so the first request afterwards can take up to a minute while it starts.

## Build and test

```bash
mvn test                 # no database or Docker needed
mvn -P release package   # production frontend build
```
