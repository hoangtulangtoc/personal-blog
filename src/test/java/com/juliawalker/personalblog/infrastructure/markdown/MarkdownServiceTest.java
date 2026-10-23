package com.juliawalker.personalblog.infrastructure.markdown;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarkdownServiceTest {

    private MarkdownService markdownService;

    @BeforeEach
    void setUp() {
        markdownService = new MarkdownService();
    }

    @Test
    void toHtml_nullMarkdown_returnsEmptyString() {

        String result = markdownService.toHtml(null);

        assertThat(result).isEmpty();
    }

    @Test
    void toHtml_blankMarkdown_returnsEmptyString() {

        String result = markdownService.toHtml(" \n\t ");

        assertThat(result).isEmpty();
    }

    @Test
    void toHtml_basicMarkdown_rendersHeadingAndEmphasis() {
        String markdown = "# Title\n\nThis is **important**.";

        String result = markdownService.toHtml(markdown);

        assertThat(result)
                .contains("<h1")
                .contains(">Title</a></h1>")
                .contains("<strong>important</strong>");
    }

    @Test
    void toHtml_softLineBreak_rendersConfiguredBreakElement() {
        String markdown = "first line\nsecond line";

        String result = markdownService.toHtml(markdown);

        assertThat(result).contains("first line<br>\nsecond line");
    }

    @Test
    void toHtml_tableMarkdown_rendersTableExtension() {
        String markdown = "| Name | Value |\n| --- | --- |\n| Java | 17 |";

        String result = markdownService.toHtml(markdown);

        assertThat(result)
                .contains("<table>")
                .contains("<th>Name</th>")
                .contains("<td>Java</td>");
    }

    @Test
    void toHtml_strikethroughMarkdown_rendersStrikethroughExtension() {
        String markdown = "~~deprecated~~";

        String result = markdownService.toHtml(markdown);

        assertThat(result).contains("<del>deprecated</del>");
    }

    @Test
    void toHtml_taskListMarkdown_rendersTaskListExtension() {
        String markdown = "- [x] completed\n- [ ] pending";

        String result = markdownService.toHtml(markdown);

        assertThat(result)
                .contains("type=\"checkbox\"")
                .contains("checked disabled")
                .contains("completed")
                .contains("pending");
    }

    @Test
    void toHtml_plainUrl_rendersAutolinkExtension() {
        String markdown = "Visit https://example.com";

        String result = markdownService.toHtml(markdown);

        assertThat(result).contains("<a href=\"https://example.com\">https://example.com</a>");
    }

    @Test
    void toHtml_heading_rendersAnchorLinkExtension() {
        String markdown = "## Project Overview";

        String result = markdownService.toHtml(markdown);

        assertThat(result)
                .contains("href=\"#project-overview\"")
                .contains("id=\"project-overview\"")
                .contains(">Project Overview</a>");
    }

    @Test
    void toHtml_scriptTag_isRemoved() {
        String markdown = "Hello <script>alert('x')</script> world";

        String result = markdownService.toHtml(markdown);

        assertThat(result).doesNotContain("<script").doesNotContain("alert");
    }

    @Test
    void toHtml_eventHandlerAttribute_isRemoved() {
        String markdown = "<img src=\"https://example.com/a.png\" onerror=\"alert(1)\">";

        String result = markdownService.toHtml(markdown);

        assertThat(result).contains("src=\"https://example.com/a.png\"").doesNotContain("onerror");
    }

    @Test
    void toHtml_javascriptLink_dropsHref() {
        String markdown = "[click](javascript:alert(1))";

        String result = markdownService.toHtml(markdown);

        assertThat(result).contains("click").doesNotContain("javascript:");
    }

    @Test
    void toHtml_relativeLink_isKept() {
        String markdown = "[other](/articles/other-post)";

        String result = markdownService.toHtml(markdown);

        assertThat(result).contains("<a href=\"/articles/other-post\">other</a>");
    }

    @Test
    void toHtml_fencedCode_keepsLanguageClass() {
        String markdown = "```java\nint x = 1;\n```";

        String result = markdownService.toHtml(markdown);

        assertThat(result).contains("<pre><code class=\"language-java\">int x = 1;");
    }

}
