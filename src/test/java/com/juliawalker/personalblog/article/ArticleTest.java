package com.juliawalker.personalblog.article;

import static com.juliawalker.personalblog.article.ArticleFixtures.NOW;
import static com.juliawalker.personalblog.article.ArticleFixtures.article;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class ArticleTest {

    @Test
    void isPublishedAt_publishedInPast_returnsTrue() {
        Article article = article("past", NOW.minusMinutes(1));

        assertThat(article.isPublishedAt(NOW)).isTrue();
        assertThat(article.isDraft()).isFalse();
        assertThat(article.isScheduledAt(NOW)).isFalse();
    }

    @Test
    void isPublishedAt_publishedExactlyNow_returnsTrue() {
        assertThat(article("now", NOW).isPublishedAt(NOW)).isTrue();
    }

    @Test
    void isPublishedAt_draft_returnsFalse() {
        Article article = article("draft", null);

        assertThat(article.isPublishedAt(NOW)).isFalse();
        assertThat(article.isDraft()).isTrue();
    }

    @Test
    void isPublishedAt_scheduledInFuture_returnsFalse() {
        Article article = article("future", NOW.plusDays(1));

        assertThat(article.isPublishedAt(NOW)).isFalse();
        assertThat(article.isScheduledAt(NOW)).isTrue();
    }

    @Test
    void hasCategoryAndHasTag_matchingSlugs_returnTrue() {
        Article article = article("terms", NOW);

        assertThat(article.hasCategory(new Slug("java"))).isTrue();
        assertThat(article.hasCategory(new Slug("kotlin"))).isFalse();
        assertThat(article.hasTag(new Slug("spring"))).isTrue();
        assertThat(article.hasTag(new Slug("react"))).isFalse();
    }

    @Test
    void constructor_duplicateTags_keepsDistinctBySlug() {
        Article article = new Article(ArticleId.generate(), new ArticleTitle("T"), new Slug("t"), null,
                new ArticleContent("c"), null,
                List.of(ArticleTerm.of("Spring"), ArticleTerm.of("spring"), ArticleTerm.of("Java")),
                null, NOW, NOW);

        assertThat(article.getTags()).extracting(ArticleTerm::getName).containsExactly("Spring", "Java");
    }

    @Test
    void getExcerpt_withoutSummary_usesPlainTextOfContent() {
        Article article = article("excerpt", NOW);

        String excerpt = article.getExcerpt();

        assertThat(excerpt).isEqualTo("Heading Body of excerpt");
    }

    @Test
    void getExcerpt_longContent_truncatesWithEllipsis() {
        Article article = new Article(ArticleId.generate(), new ArticleTitle("T"), new Slug("t"), null,
                new ArticleContent("word ".repeat(100)), null, List.of(), null, NOW, NOW);

        String excerpt = article.getExcerpt();

        assertThat(excerpt).endsWith("…");
        assertThat(excerpt.length()).isLessThanOrEqualTo(Article.EXCERPT_LENGTH + 1);
    }

    @Test
    void getExcerpt_withSummary_returnsSummary() {
        Article article = article("with-summary", NOW).revise(new ArticleTitle("T"), new Slug("t"),
                new ArticleSummary("Short summary"), new ArticleContent("Long body"), null, List.of(), NOW, NOW);

        assertThat(article.getExcerpt()).isEqualTo("Short summary");
    }

    @Test
    void revise_changesFields_keepsIdAndCreatedAt() {
        Article original = article("original", NOW);

        Article revised = original.revise(new ArticleTitle("New"), new Slug("new"), null,
                new ArticleContent("New body"), null, List.of(), null, NOW.plusHours(1));

        assertThat(revised.getId()).isEqualTo(original.getId());
        assertThat(revised.getCreatedAt()).isEqualTo(original.getCreatedAt());
        assertThat(revised.getUpdatedAt()).isEqualTo(NOW.plusHours(1));
        assertThat(revised.isDraft()).isTrue();
        assertThat(original.getTitle().asString()).isEqualTo("Title of original");
    }

    @Test
    void statusAt_eachPublicationState_returnsMatchingStatus() {
        assertThat(article("draft", null).statusAt(NOW)).isEqualTo(ArticleStatus.DRAFT);
        assertThat(article("future", NOW.plusMinutes(1)).statusAt(NOW)).isEqualTo(ArticleStatus.SCHEDULED);
        assertThat(article("now", NOW).statusAt(NOW)).isEqualTo(ArticleStatus.PUBLISHED);
    }

    @Test
    void getExcerpt_contentWithHtml_dropsTags() {
        Article article = new Article(ArticleId.generate(), new ArticleTitle("T"), new Slug("t"), null,
                new ArticleContent("Hello <script>x</script> world"), null, List.of(), null, NOW, NOW);

        assertThat(article.getExcerpt()).isEqualTo("Hello x world");
    }

    @Test
    void getReadingMinutes_countsWordsAt200PerMinute() {
        Article shortArticle = article("short", NOW);
        Article longArticle = new Article(ArticleId.generate(), new ArticleTitle("T"), new Slug("t"), null,
                new ArticleContent("word ".repeat(401)), null, List.of(), null, NOW, NOW);

        assertThat(shortArticle.getReadingMinutes()).isEqualTo(1);
        assertThat(longArticle.getReadingMinutes()).isEqualTo(3);
    }

}
