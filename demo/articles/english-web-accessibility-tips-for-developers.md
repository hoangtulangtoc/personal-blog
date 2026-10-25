---
id: d7e4cad3-f733-5b19-914f-0658d7531276
title: 'Web Accessibility Tips for Developers'
slug: web-accessibility-tips-for-developers
summary: 'Building for everyone is easier than it may seem. Start with semantic HTML and keyboard support.'
coverImage: /images/blog-3.png
category: Accessibility
tags:
- a11y
- inclusion
- html
publishedAt: 2026-10-23T09:15
createdAt: '2026-10-23T06:18'
updatedAt: '2026-10-23T10:09'
---
## Start with semantic HTML

A `<button>` gives you keyboard focus, Enter and Space activation, and the right screen reader role. A `<div onclick>` gives you none of these by default.

## Three checks that take five minutes

- Press **Tab** through the whole page. Can you always see where focus is?
- Zoom to 200%. Does the layout still work?
- Turn off CSS. Does the content still appear in a sensible order?

## Label your forms

```html
<label for="email">Email</label>
<input id="email" type="email" autocomplete="email">
```

> Accessibility is not a feature to add at the end of a project. It is part of product quality.
