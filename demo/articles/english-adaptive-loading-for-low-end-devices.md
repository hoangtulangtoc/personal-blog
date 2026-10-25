---
id: 1f7d8180-9570-5f42-a69c-4224f44ad8c1
title: 'Adaptive Loading for Low-End Devices'
slug: adaptive-loading-for-low-end-devices
summary: 'Responsive design adapts to screen size. Adaptive loading also responds to a device’s processing power and network connection.'
coverImage: /images/blog-5.png
category: Web Performance
tags:
- webperf
- performance
- javascript
publishedAt: 2026-10-23T06:15
createdAt: '2026-10-23T06:02'
updatedAt: '2026-10-23T10:01'
---
## The idea

Serve the full experience to a fast device and a lighter version to a slower one, while keeping every version **usable**.

## Signals you can use

```js
const slow = navigator.connection?.effectiveType?.includes("2g");
const lowMemory = (navigator.deviceMemory ?? 8) <= 2;

if (slow || lowMemory) {
  document.documentElement.classList.add("lite");
}
```

## What to scale back

- Replace background video with a still image.
- Turn off complex animations.
- Replace an interactive map with a screenshot and a link.

Above all, the lighter version must still provide the full **content**.
