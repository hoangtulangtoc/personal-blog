---
id: fecff141-edc0-5206-911e-26f7ebd7c88c
title: 'Track Performance with the PageSpeed Insights API'
slug: track-performance-with-pagespeed-insights-api
summary: 'Measure Core Web Vitals every day and catch performance regressions before readers report them.'
coverImage: /images/blog-8.png
category: Web Performance
tags:
- webperf
- lighthouse
- monitoring
publishedAt: 2026-10-23T07:00
createdAt: '2026-10-23T06:16'
updatedAt: '2026-10-23T10:08'
---
## Call the API

```bash
curl "https://www.googleapis.com/pagespeedonline/v5/runPagespeed?url=https://example.com&strategy=mobile"
```

The response includes **lab data** from Lighthouse and **field data** from CrUX, collected from real users.

## Metrics to watch

- **LCP** — how long it takes the main content to appear.
- **INP** — how quickly the page responds to interaction.
- **CLS** — how much the layout shifts unexpectedly.

## A simple reporting routine

Run a script each night, save the results, and graph them over time. One score tells you little; the **trend** is what matters.
