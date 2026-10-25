---
id: c12d8202-e363-50f9-942e-27873a82bd6c
title: 'Fast Page Loads on Budget Phones'
slug: fast-page-loads-on-budget-phones
summary: 'Millions of people browse the web on low-powered devices. A few practical choices can help your pages stay responsive for them.'
coverImage: /images/blog-2.png
category: Web Performance
tags:
- webperf
- mobile
- optimize
publishedAt: 2026-10-23T07:15
createdAt: '2026-10-23T06:10'
updatedAt: '2026-10-23T10:05'
---
## Real devices, real networks

Our development machines are often much faster than an average phone. Test with **4× CPU throttling** and a *Slow 4G* connection.

## A performance budget

| Resource | Budget |
|---|---|
| Minified JavaScript | ≤ 170 KB |
| CSS | ≤ 50 KB |
| Above-the-fold images | ≤ 200 KB |
| LCP | ≤ 2.5 s |

## Changes you can make today

1. Render on the server and use JavaScript as **progressive enhancement**.
2. Set image dimensions to prevent layout shifts, and use `loading="lazy"` below the fold.
3. Set `font-display: swap` and load only the font weights you need.

```html
<img src="/images/cover.webp" width="800" height="450" loading="lazy" alt="">
```
