---
id: 81a8927e-6f75-5ae2-882b-516151015555
title: 'An Accessibility Checklist for Developers'
slug: accessibility-checklist-for-developers
summary: 'It is easy to build for people like ourselves without noticing. Use this short checklist to consider a wider range of readers.'
coverImage: /images/blog-6.png
category: Accessibility
tags:
- a11y
- checklist
- ux
publishedAt: 2026-10-23T05:45
createdAt: '2026-10-23T06:00'
updatedAt: '2026-10-23T10:00'
---
## Before you release

- [x] Every image has useful `alt` text; decorative images use `alt=""`.
- [x] Text has a contrast ratio of at least 4.5:1.
- [x] Color is not the only way information is conveyed.
- [x] Form errors are written in text and associated with the right field.
- [ ] Test with a screen reader such as NVDA or VoiceOver.

## Dark mode is an accessibility choice, too

Some people are sensitive to bright light; others find it easier to read on a light background. Let readers **choose** and remember their preference.

## Motion

```css
@media (prefers-reduced-motion: reduce) {
  * { animation: none !important; transition: none !important; }
}
```
